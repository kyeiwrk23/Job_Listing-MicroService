package com.kay.company.service;

import com.kay.company.dto.CompanyRequestDto;
import com.kay.company.dto.CompanyResponseDto;
import com.kay.company.dto.PaginationCompanyDto;
import com.kay.company.exception.CompanyExistException;
import com.kay.company.exception.NoCompanyExistException;
import com.kay.company.model.Company;
import com.kay.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final ModelMapper modelMapper;
    private final CompanyRepository companyRepository;


    @Override
    public PaginationCompanyDto findAllCompanies(String pageNumber, String pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        Pageable pageable = PageRequest.of(Integer.parseInt(pageNumber),Integer.parseInt(pageSize),sort);

        Page<Company> page = companyRepository.findAll(pageable);

        List<CompanyResponseDto> companyList = page.getContent()
                .stream()
                .map(company-> modelMapper.map(company, CompanyResponseDto.class))
                .toList();
        PaginationCompanyDto companyDto =  new PaginationCompanyDto();
        companyDto.setCompany(companyList);
        companyDto.setPageNumber(page.getNumber());
        companyDto.setPageSize(page.getSize());
        companyDto.setTotalPages(page.getTotalPages());
        companyDto.setTotalElements(page.getTotalElements());
        companyDto.setLastPage(page.isLast());


        return companyDto;
    }

    @Override
    public CompanyResponseDto findACompany(Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(()->
                new NoCompanyExistException(String.valueOf(companyId)));
        return modelMapper.map(company,CompanyResponseDto.class);
    }


    @Override
    public CompanyResponseDto createCompanies(CompanyRequestDto requestDto) {
        if(companyRepository.existsCompanyByNameIgnoreCase(requestDto.getName())){
            throw new CompanyExistException(requestDto.getName());
        }

        Company company = new Company();
        company.setDescription(requestDto.getDescription());
        company.setName(requestDto.getName());
        company.setLocation(requestDto.getLocation());
        company.setEmail(requestDto.getEmail());
        company.setWebsite(requestDto.getWebsite());
        company.setUserId(requestDto.getUserId());

        Company savedCompany = companyRepository.save(company);
        return modelMapper.map(savedCompany,CompanyResponseDto.class);
    }

    @Override
    public CompanyResponseDto updateCompanies(Long companyId,CompanyRequestDto requestDto) {
        Company company = companyRepository.findById(companyId).orElseThrow(()->
                new NoCompanyExistException(String.valueOf(companyId)));

        log.info("Before update: {}", company.toString());
        log.info("Incoming DTO: {}", requestDto.toString());

        company.setName(requestDto.getName());
        company.setLocation(requestDto.getLocation());
        company.setEmail(requestDto.getEmail());
        company.setDescription(requestDto.getDescription());
        company.setWebsite(requestDto.getWebsite());
        company.setUserId(requestDto.getUserId());

        log.info("After update: {}", company.toString());

        Company savedCompany = companyRepository.save(company);




        return modelMapper.map(savedCompany,CompanyResponseDto.class);
    }

    @Override
    public String deleteCompanies(Long companyId) {
        if(!companyRepository.existsById(companyId))
        {
            throw new NoCompanyExistException(String.valueOf(companyId));
        }
        companyRepository.deleteById(companyId);
        return "Company Deleted Successfully";
    }


}
