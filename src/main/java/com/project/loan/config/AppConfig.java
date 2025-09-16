package com.project.loan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public AppConfig restTemplate() {
        return new AppConfig();
    }

}
