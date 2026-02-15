package com.example.gateway.routesconfig;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {


    @RequestMapping(value = "/users",method = {RequestMethod.POST,RequestMethod.GET})
    public ResponseEntity<String> fallBackUsers(){

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("User Service is unavailable try again later");
    }


    @RequestMapping(value = "/company",method = {RequestMethod.POST,RequestMethod.GET})
    public ResponseEntity<String> fallBackCompany(){

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Company Service is unavailable try again later");
    }

    @RequestMapping(value = "/jobs",method = {RequestMethod.POST,RequestMethod.GET})
    public ResponseEntity<String> fallBackJob(){

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Job Service is unavailable try again later");
    }


    @RequestMapping(value = "/application",method = {RequestMethod.POST,RequestMethod.GET})
    public ResponseEntity<String> fallBackApplication(){

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Job Application Service is unavailable try again later");
    }

    @RequestMapping(value = "/review",method = {RequestMethod.POST,RequestMethod.GET})
    public ResponseEntity<String> fallBackReview(){

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Review Service is unavailable try again later");
    }
}
