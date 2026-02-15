package com.kay.jobs.mapper;

import com.kay.jobs.dto.JobDto;
import com.kay.jobs.dto.PaginatedJobResponse;
import com.kay.jobs.model.Job;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageMapper {
    public static PaginatedJobResponse pageMapper(List<JobDto> jResponse, Page<Job> page){
        PaginatedJobResponse jobResponse = new PaginatedJobResponse();

        jobResponse.setJobs(jResponse);
        jobResponse.setTotalElements(page.getTotalElements());
        jobResponse.setPageNumber(page.getNumber());
        jobResponse.setTotalPages(page.getTotalPages());
        jobResponse.setPageSize(page.getSize());
        jobResponse.setLastPage(page.isLast());

        return jobResponse;
    }
}
