package com.dmdev.spring.integration.http.controller;

import com.dmdev.spring.database.entity.Role;
import com.dmdev.spring.dto.UserCreateEditDto;
import com.dmdev.spring.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static com.dmdev.spring.dto.UserCreateEditDto.Fields.companyId;
import static com.dmdev.spring.dto.UserCreateEditDto.Fields.firstname;
import static com.dmdev.spring.dto.UserCreateEditDto.Fields.lastname;
import static com.dmdev.spring.dto.UserCreateEditDto.Fields.role;
import static com.dmdev.spring.dto.UserCreateEditDto.Fields.username;
import static com.dmdev.spring.dto.UserCreateEditDto.Fields.birthDate;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RequiredArgsConstructor
// Это специальный объект для того чтобы иммитировать http-запросы в приложение
@AutoConfigureMockMvc
public class UserControllerTest extends IntegrationTestBase {

    private final MockMvc mockMvc;

//    @BeforeEach
//    void init() {
//        List<GrantedAuthority> roles = Arrays.asList(Role.ADMIN, Role.USER);
//        User testUser = new User("test@gmail.com", "test", roles);
//        TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken(testUser, testUser.getPassword(), roles);
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        securityContext.setAuthentication(authenticationToken);
//        SecurityContextHolder.setContext(securityContext);
//    }

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/users")
                        .with(SecurityMockMvcRequestPostProcessors.user("test@gmail.com")
                                .authorities(Role.ADMIN)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("users/users"))
                .andExpect(model().attributeExists("users"));
//                .andExpect(model().attribute("users", hasSize(5)));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/users")
//                        .queryParam() - если мы хотим чтобы параметры были в строке запроса
                                // обычный .param() - параметр в теле метода
                                .param(username, "test@gmail.com")
                                .param(firstname, "Test")
                                .param(lastname, "TestTest")
                                .param(role, "ADMIN")
                                .param(companyId, "1")
                // Три варианта как решить эту проблему конвертации дат:
                // 1) в application.yaml указать spring.mvc.format.date - указать iso
                // 2) Можно указать специфично для конкретного поля - аннотация @DateTimeFormat(pattern = "yyyy-MM-dd") над полем в dto
                // 3) Изменить конфигурацию: создаём свой класс WebConfiguration с аннотацией @Configuration, реализовать интерфейс WebMvcConfigurer,
                //    и переопределить метод addFormatters() - в registry можно добавить конвертер или форматтер
                // форматтер - преобразование с локализацией (есть локаль на различных языках)
                // конвертеры - нет локалий, только из чего в чего - это нам и нужно
                // (registry.addConverter(Jsr310Converters.StringToLocalDateConverter.INSTANCE);)
                        .param(birthDate, "2000-01-01")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        // здесь используется Ant паттерн
                        redirectedUrlPattern("/users/{\\d+}")
                );
    }
}
