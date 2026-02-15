package com.kay.submitApplication.mapper;

import com.kay.submitApplication.dto.SubmitApplicationDto;
import com.kay.submitApplication.external.JobResponseDto;
import com.kay.submitApplication.external.UserResponseDto;
import com.kay.submitApplication.model.SubmitApp;
import lombok.Data;


@Data
public class SubmitMapper {

    public static SubmitApplicationDto submitMapper(
            JobResponseDto jobResponseDto, UserResponseDto userResponseDto,
            SubmitApp app ) {
        SubmitApplicationDto submitApplicationDto = new SubmitApplicationDto();
        submitApplicationDto.setApplicationId(app.getApplicationId());
        submitApplicationDto.setStatus(app.getStatus());
        submitApplicationDto.setApplicantId(app.getApplicantId());
        submitApplicationDto.setResumeUrl(app.getResumeUrl());
        submitApplicationDto.setCoverLetter(app.getCoverLetter());
        submitApplicationDto.setApplicant(userResponseDto);
        submitApplicationDto.setJob(jobResponseDto);

        return submitApplicationDto;
    }
}
