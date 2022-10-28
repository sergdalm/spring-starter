package com.dmdev.spring;

import com.dmdev.spring.config.ApplicationConfiguration;
import com.dmdev.spring.config.DatabaseProperties;
import com.dmdev.spring.database.pool.ConnectionPool;
import com.dmdev.spring.database.repository.CrudRepository;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.Entity;

// это дб едиснтвенной аннотацией во всём проекте
// теперь ComponentScan не нужен, он будет автоматически сканировать бины в пакете с этим файлом
// @PropertySource тоже не нужен, файл application будет сканироваться автоматом
// файл spring.properties используется для конфигурации внутренних классов и бинов спринга
@SpringBootApplication

// в этом пакете будут искаться все классы, помеченные аннотацией @ConfigurationProperties
@ConfigurationPropertiesScan

// Если сущности Хибернейта леажт не в той же папке, а в другом месте
// Внутри аннотации нужно указать путь где лежит пакет со всеми сущностями
//@EntityScan
public class ApplicationRunner {
    public static void main(String[] args) {
//        String value = "hello";
//        System.out.println(CharSequence.class.isAssignableFrom(value.getClass()));
//        System.out.println(BeanFactoryPostProcessor.class.isAssignableFrom(value.getClass()));
//        // для получения контекста из xml
////        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application.xml")) {
//        // для получения контекста из Java-based конфигурации
//        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class)) {
//            ConnectionPool connectionPool = context.getBean("pool1", ConnectionPool.class);
//            System.out.println(connectionPool);
//            CrudRepository companyRepository = context.getBean("companyRepository", CrudRepository.class);
//            System.out.println(companyRepository.findById(1));
//        }
        // Это метод создаём application context
        ConfigurableApplicationContext context = SpringApplication.run(ApplicationRunner.class, args);
        System.out.println(DatabaseProperties.class);
    }
}
