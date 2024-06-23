package com.pbu.ui.configurations;

import com.pbu.utils.common.AppLoggerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppLoggerConfig {
    @Bean
    public AppLoggerService appLoggerService() {
        return new AppLoggerService();
    }
}
