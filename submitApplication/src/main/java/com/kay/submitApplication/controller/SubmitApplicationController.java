package com.kay.submitApplication.controller;
import com.kay.submitApplication.constant.AppConstant;
import com.kay.submitApplication.dto.*;
import com.kay.submitApplication.service.SubmitApplicationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RefreshScope
@RequiredArgsConstructor
@RequestMapping("/api/application")
public class SubmitApplicationController {
    private final SubmitApplicationServiceImpl applicationService;

    // Used by: Job Seeker
    @PostMapping
    public ResponseEntity<ApplicationResponseDto> createApplication(@RequestBody SubmitApplicationRequestDto requestDto){
        ApplicationResponseDto jResponse = applicationService.createApplication(requestDto);

        return  new ResponseEntity<>(jResponse, HttpStatus.CREATED);
    }

    //Used by: Applicant, Employer, Admin (authorization required)
    @GetMapping("/{applicationId}")
    public ResponseEntity<SubmitApplicationDto> findApplicationById(@PathVariable Long applicationId){
        SubmitApplicationDto applicationResponseDto = applicationService.applicationById(applicationId);

        return new ResponseEntity<>(applicationResponseDto,HttpStatus.OK);
    }

    // Used by: Employer
    @GetMapping
    public ResponseEntity<PaginatedSubmitResponse> listAllApplicationsPerJob(
            @RequestParam(name = "jobId") Long jobId,
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstant.PAGENUMBER) String pageNumber,
            @RequestParam(name = "pageSize",   required = false, defaultValue = AppConstant.PAGESIZE) String pageSize,
            @RequestParam(name = "sortBy",     required = false, defaultValue = AppConstant.SORTBY) String sortBy,
            @RequestParam(name = "sortOrder",  required = false, defaultValue = AppConstant.SORTORDER) String sortOrder
        ){

        PaginatedSubmitResponse applicationResponse =
                applicationService.findAllApplicationsPerJob(jobId,pageNumber,pageSize,sortBy,sortOrder);

        return new ResponseEntity<>(applicationResponse,HttpStatus.OK);
    }

    //Used by: Job Seeker
    @GetMapping("/jobseeker")
    public ResponseEntity<PaginatedSubmitResponse> listAllApplicationsPerApplicant(
            @RequestParam(name = "applicantId") String applicantId,
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstant.PAGENUMBER) String pageNumber,
            @RequestParam(name = "pageSize",   required = false, defaultValue = AppConstant.PAGESIZE) String pageSize,
            @RequestParam(name = "sortBy",     required = false, defaultValue = AppConstant.SORTBY) String sortBy,
            @RequestParam(name = "sortOrder",  required = false, defaultValue = AppConstant.SORTORDER) String sortOrder
    )
    {
        PaginatedSubmitResponse applicantResponse =
                applicationService.findAllApplicationsByApplicant(applicantId,pageNumber,pageSize,sortBy,sortOrder);

        return new ResponseEntity<>(applicantResponse,HttpStatus.OK);
    }

    //Used by: Employer / Admin
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationStatusResponseDto> updateStatus(
            @PathVariable  Long applicationId, @RequestBody SubmitApplicationRequestDto statusDto){

        ApplicationStatusResponseDto statusResponse = applicationService.updateStatus(applicationId,statusDto);

        return  new ResponseEntity<>(statusResponse,HttpStatus.OK);
    }

    //Used by: Job Seeker
    @PutMapping("/{applicationId}")
    public ResponseEntity<String> withDrawApplication(@PathVariable Long applicationId){
        String setWithDraw = applicationService.withDrawApplication(applicationId);

        return new ResponseEntity<>(setWithDraw,HttpStatus.OK);
    }

    //Used by: Employer / Admin
    @GetMapping("/stats")
    public ResponseEntity<ApplicationStatisticsDto> applicationStatistics(@RequestParam Long jobId){
        ApplicationStatisticsDto statisticsDto = applicationService.appStatistics(jobId);

        return new ResponseEntity<>(statisticsDto,HttpStatus.OK);
    }

   //Used by:  Admin
    @DeleteMapping("/{applicationId}")
    public ResponseEntity<String> deleteApplication(@PathVariable Long applicationId){
        String deleteApp = applicationService.deleteApplication(applicationId);

        return new ResponseEntity<>(deleteApp,HttpStatus.OK);
    }


}
