package com.kay.submitApplication.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedSubmitResponse {
    private List<SubmitApplicationDto> job_Applied;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private Long totalElements;
    private boolean isLast;
}
