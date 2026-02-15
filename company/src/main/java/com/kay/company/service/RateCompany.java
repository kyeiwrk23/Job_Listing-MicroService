package com.kay.company.service;

import com.kay.company.dto.ReviewRateDto;
import com.kay.company.model.Company;
import com.kay.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateCompany {

    private final CompanyRepository companyRepository;

    @Bean
    public Consumer<ReviewRateDto>  companyRating(){

        return rate->{
            Optional<Company> company = companyRepository.findById(rate.getCompanyId());
            log.info("company found : {}", company);
            company.ifPresent(comp -> {
                comp.setRating(rate.getRate());
                Company savedCompany = companyRepository.save(comp);
                log.info("company updated : {}", savedCompany);
            });
        };
    }

}
