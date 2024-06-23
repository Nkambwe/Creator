package com.pbu.agents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.pbu.agents.repositories")
@EntityScan(basePackages = "com.pbu.agents.models")
public class AgentApplication {
    public static void main( String[] args ) {
        SpringApplication.run(AgentApplication.class, args);
    }
}