package com.kay.company.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaginationCompanyDto {
    private List<CompanyResponseDto> company;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private Long totalElements;
    private boolean lastPage;
}
