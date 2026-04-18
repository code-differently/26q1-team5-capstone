package com.scholarship.backend.config;

import com.scholarship.backend.services.ScholarshipSyncService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupConfig {

    @Bean
    CommandLineRunner init(ScholarshipSyncService service) {
        return args -> {
            service.syncIfNeeded();
        };
    }
}