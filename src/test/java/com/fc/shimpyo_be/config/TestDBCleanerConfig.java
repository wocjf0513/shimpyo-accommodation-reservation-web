package com.fc.shimpyo_be.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestDBCleanerConfig {

    @Bean
    public DatabaseCleanUp databaseCleanUp() {
        return new DatabaseCleanUp();
    }
}
