package com.kay.jobs.controller;

import com.kay.jobs.constant.AppConstants;
import com.kay.jobs.dto.JobRequestDto;
import com.kay.jobs.dto.JobResponseDto;
import com.kay.jobs.dto.PaginatedJobResponse;
import com.kay.jobs.service.JobServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RefreshScope
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobServiceImpl jobService;

    @GetMapping
    public ResponseEntity<PaginatedJobResponse> findAll(
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstants.PAGENUMBER) String pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.PAGESIZE) String pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SORTBY) String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.SORTDIR) String sortDir
            ){

        PaginatedJobResponse jResponse = jobService.findAll(pageNumber,pageSize,sortBy,sortDir);

        return ResponseEntity.ok(jResponse);
    }

    //work on this when we build the company service(we want the company name)
    @GetMapping("/{jobId}/companyName")
    public ResponseEntity<String> JobByIdByCompany(@PathVariable Long jobId){
       String company =  jobService.JobByIdByCompany(jobId);

        return ResponseEntity.ok(company);
    }

    @GetMapping("/{companyId}/joblist")
    public ResponseEntity<PaginatedJobResponse> jobsPublishedByCompany(
            @PathVariable Long companyId,
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstants.PAGENUMBER) String pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.PAGESIZE) String pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SORTBY) String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.SORTDIR) String sortDir
    )
    {

        PaginatedJobResponse jobResponse = jobService.jobsPublishedByCompany(companyId,pageNumber,pageSize,sortBy,sortDir);
        return  ResponseEntity.ok(jobResponse);
    }


    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponseDto> jobById(@PathVariable Long jobId){
        JobResponseDto jresponse = jobService.jobById(jobId);
        return ResponseEntity.ok(jresponse);
    }

    @GetMapping("/keywords")
    public ResponseEntity<List<JobResponseDto>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String jobType)
    {

        List<JobResponseDto> jbResponse = jobService.searchJobs(title,location,jobType);
        return ResponseEntity.ok(jbResponse);
    }

    @PostMapping
    public ResponseEntity<String> createJob(@RequestBody JobRequestDto requestDto){
        log.info("createJob controller");
        String str = jobService.createJob(requestDto);
        return new ResponseEntity<>(str,HttpStatus.CREATED);
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<JobResponseDto> updateJob(@PathVariable Long jobId,@RequestBody JobRequestDto requestDto){
        JobResponseDto jresponse = jobService.updateJob(jobId,requestDto);
        return ResponseEntity.ok(jresponse);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable Long jobId){
        String jresponse = jobService.deleteJob(jobId);
        return new ResponseEntity<>(jresponse,HttpStatus.OK);
    }
}
