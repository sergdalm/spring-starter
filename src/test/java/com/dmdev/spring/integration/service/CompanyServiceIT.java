package com.dmdev.spring.integration.service;

import com.dmdev.spring.ApplicationRunner;
import com.dmdev.spring.config.DatabaseProperties;
import com.dmdev.spring.database.entity.Company;
import com.dmdev.spring.dto.CompanyReadDto;
import com.dmdev.spring.integration.annotation.IT;
import com.dmdev.spring.listener.entity.EntityEvent;
import com.dmdev.spring.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

//@ExtendWith(SpringExtension.class)

//// для того чтобы указать какой именно application context
//@ContextConfiguration(classes = ApplicationRunner.class,
//initializers = ConfigDataApplicationContextInitializer.class)

// вместо двух аннотаций выше
// он автоматически ищет класс помечанный аннотацией @SpringBootApplication
// можно его явно указать, но необязательно
//@SpringBootTest
// тут перечислить все активированный профайлы
//@ActiveProfiles("test")

@IT
@RequiredArgsConstructor
// вместо этой аннотации можно написать в тестовых spring properties такую настройку
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class CompanyServiceIT {

    private static final Integer COMPANY_ID = 1;

    private final CompanyService companyService;
    private final DatabaseProperties databaseProperties;

    @Test
    void findById() {
        Optional<CompanyReadDto> actualResult = companyService.findById(COMPANY_ID);

        assertTrue(actualResult.isPresent());

        CompanyReadDto expectedResult = new CompanyReadDto(COMPANY_ID, null);
        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));
    }

}
