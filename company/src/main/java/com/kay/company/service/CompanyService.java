package com.kay.company.service;

import com.kay.company.dto.CompanyRequestDto;
import com.kay.company.dto.CompanyResponseDto;
import com.kay.company.dto.PaginationCompanyDto;

public interface CompanyService {
    PaginationCompanyDto findAllCompanies(String pageNumber,String pageSize,String sortBy,String sortOrder);
    CompanyResponseDto findACompany(Long companyId);
    CompanyResponseDto createCompanies(CompanyRequestDto requestDto);
    CompanyResponseDto updateCompanies(Long companyId,CompanyRequestDto requestDto);
    String deleteCompanies(Long companyId);
}
