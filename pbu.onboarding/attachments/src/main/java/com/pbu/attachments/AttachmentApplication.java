package com.pbu.attachments;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.pbu.attachments.repositories")
@EntityScan(basePackages = "com.pbu.attachments")
public class AttachmentApplication {
    public static void main( String[] args ) {
        SpringApplication.run(AttachmentApplication.class, args);
    }
}

