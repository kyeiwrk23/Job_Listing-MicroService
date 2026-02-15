package com.kay.submitApplication.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantAndJobDto {
    private UserResponseDto  user;
    private JobResponseDto job;
}
