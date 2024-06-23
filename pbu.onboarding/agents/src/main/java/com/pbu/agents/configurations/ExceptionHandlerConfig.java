package com.pbu.agents.configurations;

import com.pbu.utils.exceptions.ApplicationExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionHandlerConfig {
    @Bean
    public ApplicationExceptionHandler exceptionHandlerBean() {

        return new ApplicationExceptionHandler();
    }
}
