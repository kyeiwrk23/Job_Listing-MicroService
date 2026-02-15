package com.kay.jobs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedJobResponse {
    private List<JobDto> jobs;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private boolean lastPage;
}
