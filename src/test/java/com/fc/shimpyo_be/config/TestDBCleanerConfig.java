package com.fc.shimpyo_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestDBCleanerConfig {

    @Bean
    public DatabaseCleanUp databaseCleanUp() {
        return new DatabaseCleanUp();
    }
}
