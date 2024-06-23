package com.pbu.attachments.configurations;

import com.pbu.utils.common.NetworkService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NetworkConfig {
    @Bean
    public NetworkService networkService() {
        return new NetworkService();
    }
}
