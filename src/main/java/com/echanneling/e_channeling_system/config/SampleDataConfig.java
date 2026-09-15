package com.echanneling.e_channeling_system.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleDataConfig {

    @Bean
    CommandLineRunner initData() {
        return args -> {
            // Let the application start cleanly without database constraint conflicts
        };
    }
}