package com.dmdev.spring.config;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

// Названия полей должны совпадать с полями в yml файле
// Класс должен быть POJO (getters, setters)
//@Value

// можно валидирующие аннотации ставить над dto, которые мы используем для конфигурации.
// В таком случае если валидация не прошла - приложение просто не поднимется.
@Validated

// Для того чтобы эта аннотация работала, нужно класс сделать бином
// Т.е. либо поставить @Component,
// либо @ConfigurationPropertiesScan y ApplicationRunner
@ConfigurationProperties(prefix = "db")
// чтобы можно было инициализировать через конструктор
// Или можено сделать класс record'ом
//@ConstructorBinding
public record DatabaseProperties(String username,
                                 String password,
                                 String driver,
                                 String url,
                                 String hosts,
                                 PoolProperties pool,
                                 List<PoolProperties> pools,
                                 Map<String, Object> properties) {

    public record PoolProperties(Integer size,
                                 Integer timeout) {
    }
}
