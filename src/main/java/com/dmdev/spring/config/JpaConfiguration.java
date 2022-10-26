package com.dmdev.spring.config;

import com.dmdev.spring.config.condition.JpaCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

// автоматически создать константу log
@Slf4j

@Conditional(JpaCondition.class)
@Configuration
public class JpaConfiguration {

//    @Bean
//    // для конвертации yml файла в объект java класса
//    @ConfigurationProperties(prefix = "db")
//    public DatabaseProperties databaseProperties() {
//        return new DatabaseProperties();
//    }

    @PostConstruct
    void init() {
        log.info("Jpa configuration is enabled");
    }
}
