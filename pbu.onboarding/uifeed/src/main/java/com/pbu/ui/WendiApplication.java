package com.pbu.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.pbu.ui.models")

public class WendiApplication {

    public static void main( String[] args ) {
        SpringApplication.run(WendiApplication.class, args);
    }

}
