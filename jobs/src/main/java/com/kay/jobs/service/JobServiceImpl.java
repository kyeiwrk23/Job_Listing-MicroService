package com.kay.jobs.service;

import com.kay.jobs.dto.*;
import com.kay.jobs.exception.NoJobExistException;
import com.kay.jobs.external.CompanyResponseDto;
import com.kay.jobs.external.ReviewResponseDto;
import com.kay.jobs.mapper.JobMapper;
import com.kay.jobs.mapper.PageMapper;
import com.kay.jobs.model.Job;
import com.kay.jobs.repository.JobRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;



@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService{
    private final JobRepository jobRepository;
    private final ModelMapper modelMapper;


    private final WebClient webClient;



    // Apply Circuit breaker & retry
    @Retry(name = "companyReviewRetry")
    @CircuitBreaker(name = "companyReviewCircuitBreaker", fallbackMethod = "findAllFallback")
    @Override
    public PaginatedJobResponse findAll(String pageNumber,String pageSize,String sortBy,String sortOrder) {

        Pageable pageable = pagination(pageNumber,pageSize,sortBy,sortOrder);

        Page<Job> jobPage = jobRepository.findAll(pageable);

        return paginatedJobDetails(jobPage);
    }

    @Override
    public JobResponseDto jobById(Long id) {
        Job job = jobRepository.findById(id).orElseThrow(()->
                new NoJobExistException(String.valueOf(id)));

        return modelMapper.map(job,JobResponseDto.class);
    }


    @Override
    public String createJob(JobRequestDto requestDto) {
        log.info("createJob");
//        Job job = modelMapper.map(requestDto,Job.class);
        Job job = new Job();
        job.setTitle(requestDto.getTitle());
        job.setDescription(requestDto.getDescription());
        job.setCompanyId(requestDto.getCompanyId());
        job.setMaxSalary(requestDto.getMaxSalary());
        job.setMinSalary(requestDto.getMinSalary());
        job.setContractType(requestDto.getContractType());
        job.setLocation(requestDto.getLocation());
        jobRepository.save(job);
        return "Job Added Successfully";
    }


    @Override
    public String deleteJob(Long id) {
        Job job = jobRepository.findById(id).orElseThrow(()->
                new NoJobExistException(String.valueOf(id)));
        jobRepository.delete(job);
        return "Deleted Successfully";
    }


    @Override
    public JobResponseDto updateJob(Long id, JobRequestDto requestDto) {
        Job job = jobRepository.findById(id).orElseThrow(()->
                new NoJobExistException(String.valueOf(id)));

        job.setTitle(requestDto.getTitle());
        job.setDescription(requestDto.getDescription());
        job.setLocation(requestDto.getLocation());
        job.setCompanyId(requestDto.getCompanyId());
        job.setContractType(requestDto.getContractType());
        job.setMinSalary(requestDto.getMinSalary());
        job.setMaxSalary(requestDto.getMaxSalary());

        jobRepository.save(job);

        return modelMapper.map(job,JobResponseDto.class);
    }

    @Override
    public String JobByIdByCompany(Long jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(()->
                new NoJobExistException(String.valueOf(jobId)));
        
        return String.valueOf(job.getJobId());
    }

    // Apply Circuit breaker & retry
    @Retry(name = "companyReviewRetry")
    @CircuitBreaker(name = "companyReviewCircuitBreaker", fallbackMethod = "jobsPublishedByCompanyFallback")
    @Override
    public PaginatedJobResponse jobsPublishedByCompany(Long companyId,String pageNumber,String pageSize,String sortBy,String sortOrder) {

        Pageable pageable = pagination(pageNumber,pageSize,sortBy,sortOrder);

        Page<Job> page = jobRepository.findAllByCompanyId(companyId,pageable);

        return paginatedJobDetails(page);
    }


    @Override
    public List<JobResponseDto> searchJobs(String title, String location, String jobType) {
        List<Job> jobs = jobRepository.searchJobByKeywords(title,location,jobType);

        return  jobs.stream()
                .map(dtoJobs-> modelMapper.map(dtoJobs,JobResponseDto.class))
                .toList();
    }

    private Pageable pagination(String pageNumber,String pageSize,String sortBy,String sortOrder){
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return PageRequest.of(Integer.parseInt(pageNumber),Integer.parseInt(pageSize),sort);
    }

    private JobDto jobReviewAndCompanyDetails(JobResponseDto jobResponseDto){

        CompanyReviewDto companyReview =  getCompanyAndReview(jobResponseDto.getCompanyId());

        return JobMapper.jobMapper(jobResponseDto,companyReview);
    }

    private PaginatedJobResponse paginatedJobDetails(Page<Job> page){
        List<JobDto> jResponse = page.getContent()
                .stream()
                .map(job-> {
                    JobResponseDto jobResponseDto = modelMapper.map(job, JobResponseDto.class);
                    return jobReviewAndCompanyDetails(jobResponseDto);
                }).toList();

        return PageMapper.pageMapper(jResponse,page);
    }

    private CompanyReviewDto getCompanyAndReview(Long companyId) {
        CompanyReviewDto companyReviewDto = new CompanyReviewDto();

        Mono<CompanyResponseDto> comp = webClient.get()
                .uri("http://COMPANY/api/companies/" + companyId)
                .retrieve()
                .bodyToMono(CompanyResponseDto.class);


        Mono<List<ReviewResponseDto>> review = webClient.get()
                .uri("http://REVIEW/api/reviews?companyId=" + companyId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ReviewResponseDto>>() {});


        companyReviewDto.setCompany(comp.block());
        companyReviewDto.setReviews(review.block());
        return companyReviewDto;
    }

    public PaginatedJobResponse jobsPublishedByCompanyFallback(Long companyId,String pageNumber,String pageSize,String sortBy,String sortOrder,Exception ex) {
        log.info("companyReviewFallback method called...");
        Pageable pageable = pagination(pageNumber,pageSize,sortBy,sortOrder);

        Page<Job> page = jobRepository.findAllByCompanyId(companyId,pageable);
        return  fallbackMethod(page);
    }

    public PaginatedJobResponse findAllFallback(String pageNumber,String pageSize,String sortBy,String sortOrder, Exception ex) {
        log.info("findAllFallback method called...");
        Pageable pageable = pagination(pageNumber,pageSize,sortBy,sortOrder);
        Page<Job> page = jobRepository.findAll(pageable);
        return  fallbackMethod(page);
    }

    private PaginatedJobResponse fallbackMethod(Page<Job> page){

        List<JobDto> dto = page.getContent()
                .stream()
                .map(job -> {
                    CompanyResponseDto comp = new CompanyResponseDto();
                    comp.setCompanyId(job.getCompanyId());
                    JobDto dt = new JobDto();
                    dt.setJobId(job.getJobId());
                    dt.setDescription(job.getDescription());
                    dt.setTitle(job.getTitle());
                    dt.setLocation(job.getLocation());
                    dt.setMinSalary(job.getMinSalary());
                    dt.setMaxSalary(job.getMaxSalary());
                    dt.setContractType(job.getContractType());
                    dt.setCompany(comp);
                    return dt;
                }).toList();

                return PageMapper.pageMapper(dto,page);
    }




}
