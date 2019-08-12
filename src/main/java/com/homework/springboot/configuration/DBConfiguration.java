package com.homework.springboot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("spring.datasource")
@SuppressWarnings("unused")
@ComponentScan("com.homework.springboot")
public class DBConfiguration {

    @Profile("dev")
    @Bean
    public String devDatabaseConnection() {
        System.out.println("DB connection for DEV - H2");
        return "DB connection for DEV - H2";
    }

    @Profile("it")
    @Bean
    public String itDatabaseConnection() {
        System.out.println("DB connection for IT - h2");
        return "DB connection for IT - H2";
    }
}
