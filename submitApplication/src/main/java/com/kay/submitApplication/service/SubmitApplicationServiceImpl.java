package com.kay.submitApplication.service;



import com.kay.submitApplication.dto.*;
import com.kay.submitApplication.exception.NoSubmitResourceExistException;
import com.kay.submitApplication.exception.SubmitResourceExistException;
import com.kay.submitApplication.external.ApplicantAndJobDto;
import com.kay.submitApplication.external.JobResponseDto;
import com.kay.submitApplication.external.UserResponseDto;
import com.kay.submitApplication.mapper.SubmitMapper;
import com.kay.submitApplication.model.StatusEnum;
import com.kay.submitApplication.model.SubmitApp;
import com.kay.submitApplication.repository.SubmitApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmitApplicationServiceImpl implements SubmitApplicationService {
    private final SubmitApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;
    private final WebClient webClient;

    // Used by: Job Seeker
    @Override
    public ApplicationResponseDto createApplication(SubmitApplicationRequestDto requestDto) {
        if(checkIfApplied(requestDto.getJobId(),requestDto.getApplicantId())){
            throw new SubmitResourceExistException("User " + requestDto.getApplicantId() + " Already applied for the Job" + requestDto.getJobId()  );
        }

        SubmitApp app = new SubmitApp();

        app.setApplicantId(requestDto.getApplicantId());
        app.setJobId(requestDto.getJobId());
        app.setStatus(StatusEnum.SUBMITTED);
        app.setCoverLetter(requestDto.getCoverLetter());
        app.setResumeUrl(requestDto.getResumeUrl());


        SubmitApp savedApp = applicationRepository.save(app);

        return modelMapper.map(savedApp,ApplicationResponseDto.class);
    }

    //Used by: Applicant, Employer, Admin (authorization required)
    @Override
    public SubmitApplicationDto applicationById(Long applicationId) {
        SubmitApp applied = applicationRepository.findById(applicationId).orElseThrow(()->
                new NoSubmitResourceExistException("Application with " + String.valueOf(applicationId) +  "doesn't"));

        ApplicantAndJobDto appJob = getApplicantAndJobDetails(applied);
        return SubmitMapper.submitMapper(appJob.getJob(),appJob.getUser(),applied);

    }

    // Used by: Employer
    @Override
    public PaginatedSubmitResponse findAllApplicationsPerJob(Long jobId,String pageNumber,String pageSize,String sortBy,String sortOrder) {
        Pageable pageable = pagination(pageNumber,pageSize,sortBy,sortOrder);

        Page<SubmitApp> applicationList = applicationRepository.findAllByJobId(jobId,pageable);
        return getPaginatedApplicationResponse(applicationList);

    }


    //Used by: Job Seeker
    @Override
    public PaginatedSubmitResponse findAllApplicationsByApplicant(String applicantId,String pageNumber,String pageSize,String sortBy,String sortOrder) {
        Pageable pageable = pagination(pageNumber,pageSize,sortBy,sortOrder);

        Page<SubmitApp> applicantPage = applicationRepository.findApplicationsByApplicantId(applicantId,pageable);

        return getPaginatedApplicationResponse(applicantPage);
    }

    //Used by: Employer / Admin
    @Override
    public ApplicationStatusResponseDto updateStatus(Long applicationId, SubmitApplicationRequestDto statusDto) {
        SubmitApp application = applicationRepository.findById(applicationId).orElseThrow(
                ()-> new RuntimeException("Haven't applied"));

        switch(statusDto.getStatus().toUpperCase()){
            case "REVIEWING":
                application.setStatus(StatusEnum.REVIEWING);
                break;
            case "SHORTLISTED":
                application.setStatus(StatusEnum.SHORTLISTED);
                break;
            case "HIRED":
                application.setStatus(StatusEnum.HIRED);
                break;
            case "REJECTED":
                application.setStatus(StatusEnum.REJECTED);
        }

        application.setUpdatedAt(LocalDateTime.now());

        SubmitApp savedApp = applicationRepository.save(application);

        ApplicationStatusResponseDto statusResponse = new ApplicationStatusResponseDto();
        statusResponse.setApplicationId(savedApp.getApplicationId());
        statusResponse.setStatus(savedApp.getStatus().name());
        statusResponse.setUpdatedAt(savedApp.getUpdatedAt());

        return statusResponse;
    }

    //Used by: Job Seeker
    @Override
    public String withDrawApplication(Long applicationId) {
        SubmitApp jobApplied = applicationRepository.findById(applicationId).orElseThrow(
                ()-> new NoSubmitResourceExistException("Application with " + applicationId + "doesn't exist!!"));
        jobApplied.setStatus(StatusEnum.WITHDRAWN);
        applicationRepository.save(jobApplied);
        return "Application withdrawn successfully";
    }


    @Override
    public boolean checkIfApplied(Long jobId, String applicantId) {
        Optional<SubmitApp> app = applicationRepository.findByApplicantIdAndJobId(applicantId,jobId);

        return app.isPresent();
    }

    //Used by: Employer / Admin
    @Override
    public ApplicationStatisticsDto appStatistics(Long jobId) {
        List<SubmitApp>totalApplied = applicationRepository.findAllJobs(jobId);
        if(totalApplied.isEmpty()){
            throw new NoSubmitResourceExistException("No Application with Job Id " + jobId + "doesn't exist!!!");
        }

        ApplicationStatisticsDto statisticsDto = new ApplicationStatisticsDto();
        totalApplied.forEach(
                app -> {
                    switch (app.getStatus()){
                        case SUBMITTED -> {
                            int submitted = statisticsDto.getSubmitted() + 1;
                            statisticsDto.setSubmitted(submitted);
                        }
                        case SHORTLISTED -> {
                            int shortListed = statisticsDto.getShortListed() + 1;
                            statisticsDto.setShortListed(shortListed);
                        }
                        case REJECTED -> {
                            int rejected = statisticsDto.getRejected() + 1;
                            statisticsDto.setRejected(rejected);
                        }
                    }
                }
        );

        statisticsDto.setTotal(totalApplied.size());

        return statisticsDto;
    }

    @Override
    public String deleteApplication(Long applicationId) {

        if(applicationRepository.existsById(applicationId)){
            throw new NoSubmitResourceExistException("Application with " + applicationId + "doesn't exist!!");
        }


        applicationRepository.deleteById(applicationId);
        return "Application deleted Successfully";
    }


    private Pageable pagination(String pageNumber,String pageSize,String sortBy,String sortOrder){
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        return PageRequest.of(Integer.parseInt(pageNumber),Integer.parseInt(pageSize),sort);
    }
    private PaginatedSubmitResponse getPaginatedApplicationResponse(Page<SubmitApp> applicationList) {
        List<SubmitApplicationDto> responseDto =
                applicationList.getContent()
                .stream()
                .map(app -> {

                    ApplicantAndJobDto appJob = getApplicantAndJobDetails(app);

                    return SubmitMapper.submitMapper(appJob.getJob(),appJob.getUser(),app);
                }).toList();

        PaginatedSubmitResponse appResponse = new PaginatedSubmitResponse();
        appResponse.setJob_Applied(responseDto);
        appResponse.setPageNumber(applicationList.getNumber());
        appResponse.setPageSize(applicationList.getSize());
        appResponse.setTotalPages(applicationList.getTotalPages());
        appResponse.setTotalElements(applicationList.getTotalElements());
        appResponse.setLast(applicationList.isLast());
        return appResponse;
    }

    private ApplicantAndJobDto getApplicantAndJobDetails(SubmitApp app){
        Mono<UserResponseDto> applicant = webClient.get()
                .uri("http://USER/api/users/{userId}",app.getApplicantId())
                .retrieve()
                .bodyToMono(UserResponseDto.class);


        Mono<JobResponseDto> job = webClient.get()
                .uri("https://JOB/api/jobs/{jobId}",app.getJobId())
                .retrieve()
                .bodyToMono(JobResponseDto.class);

        return new ApplicantAndJobDto(applicant.block(),job.block());
    }
}
