package com.kay.company.dto;
import lombok.Data;

@Data
public class CompanyRequestDto {

    private String name;
    private String description;
    private String website;
    private String email;
    private String userId;
    private String location;
}
