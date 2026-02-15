package com.kay.company.controller;

import com.kay.company.constants.AppConstant;
import com.kay.company.dto.CompanyRequestDto;
import com.kay.company.dto.CompanyResponseDto;
import com.kay.company.dto.PaginationCompanyDto;
import com.kay.company.service.CompanyServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RefreshScope
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyServiceImpl companyService;

    @GetMapping
    public ResponseEntity<PaginationCompanyDto> findAllCompanies(
            @RequestParam(name = "pageNumber",required = false,defaultValue = AppConstant.PAGENUMBER) String pageNumber,
            @RequestParam(name = "pageSize",required = false,defaultValue = AppConstant.PAGESIZE) String pageSize,
            @RequestParam(name = "sortBy",required = false,defaultValue = AppConstant.SORTBY) String sortBy,
            @RequestParam(name = "sortOrder",required = false,defaultValue = AppConstant.SORTORDER) String sortOrder

    ){
        PaginationCompanyDto companyDto = companyService.findAllCompanies(pageNumber,pageSize,sortBy,sortOrder);

        return new ResponseEntity<>(companyDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CompanyResponseDto> createCompany(@RequestBody CompanyRequestDto requestDto){
        CompanyResponseDto response =  companyService.createCompanies(requestDto);

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponseDto> findACompany(@PathVariable Long companyId){
        log.info("Find company with id:{}",companyId);
        CompanyResponseDto dtoResponse = companyService.findACompany(companyId);

        return  new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyResponseDto> updateCompany(@PathVariable Long companyId,@RequestBody CompanyRequestDto requestDto){
        CompanyResponseDto responseDto = companyService.updateCompanies(companyId,requestDto);

        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long companyId){
        String response = companyService.deleteCompanies(companyId);

        return ResponseEntity.ok(response);
    }

}
