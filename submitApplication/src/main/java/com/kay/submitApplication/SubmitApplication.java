package com.kay.submitApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SubmitApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubmitApplication.class, args);
	}

}
