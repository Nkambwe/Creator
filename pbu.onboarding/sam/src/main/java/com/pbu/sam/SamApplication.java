package com.pbu.sam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.pbu.sam.repositories")
@EntityScan(basePackages = "com.pbu.sam.models")

public class SamApplication {

    public static void main( String[] args ) {
        SpringApplication.run(SamApplication.class, args);
    }
}
