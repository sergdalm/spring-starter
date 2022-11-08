package com.dmdev.spring.integration.service;

import com.dmdev.spring.database.entity.Role;
import com.dmdev.spring.database.pool.ConnectionPool;
import com.dmdev.spring.dto.UserCreateEditDto;
import com.dmdev.spring.dto.UserReadDto;
import com.dmdev.spring.integration.annotation.IT;
import com.dmdev.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.mock.mockito.SpyBeans;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
// эта аннотация будет помечать context как dirty
// - контекст будет помечен как dirty и мы не сможем переиспользовать уже существующий,
// каждый рза будет новый контекст
// можно ставить такую аннотацию над методом.
// лучше не использовать эту аннотацию, только если мы вручную портим ApplicationContext
// по умолчанию - действует автоматический механизм определения dirty контекста
// в идеале как можно реже делать его dirty
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceIT {

    private static final Long USER_1 = 1L;
    private static final Integer COMPANY_1 = 1;
    private static final UserCreateEditDto userDto = new UserCreateEditDto(
            "test@gmail.com",
            LocalDate.now(),
            "Test",
            "Test",
            Role.ADMIN,
            COMPANY_1
    );

    private final UserService userService;

    @Test
    void findAll() {
        List<UserReadDto> result = userService.findAll();
        assertThat(result).hasSize(5);
    }

    @Test
    void findById() {
        Optional<UserReadDto> maybeUser = userService.findById(USER_1);
        assertTrue(maybeUser.isPresent());
        maybeUser.ifPresent(user ->
                assertEquals("ivan@gmail.com", user.getUsername()));
    }

    @Test
    void create() {

        UserReadDto actualResult = userService.create(userDto);

        assertEquals(userDto.getUsername(), actualResult.getUsername());
        assertEquals(userDto.getBirthDate(), actualResult.getBirthDate());
        assertEquals(userDto.getFirstname(), actualResult.getFirstname());
        assertEquals(userDto.getLastname(), actualResult.getLastname());
        assertEquals(userDto.getCompanyId(), actualResult.getCompany().id());
        // Enum нужно сравнивать на == (assertSame())
        assertSame(userDto.getRole(), actualResult.getRole());
    }

    @Test
    void update() {
        Optional<UserReadDto> actualResult = userService.update(USER_1, userDto);
        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(user -> {
            assertEquals(userDto.getUsername(), user.getUsername());
            assertEquals(userDto.getBirthDate(), user.getBirthDate());
            assertEquals(userDto.getFirstname(), user.getFirstname());
            assertEquals(userDto.getLastname(), user.getLastname());
            assertEquals(userDto.getCompanyId(), user.getCompany().id());
            // Enum нужно сравнивать на == (assertSame())
            assertSame(userDto.getRole(), user.getRole());
        });
    }

    @Test
    void delete() {
        // ВОПРОС: "в случае Хибернейт нужно предпочитать каскадное удаление на уровне маппинга сущностей, но более быстро работает on delete cascade на уровне БД"
        // - зачем делать через Хибернейет если on delete cascade на уровне БД работает быстрее? И не лучше ли когда у нас маппинг Хибернейта совпаает с БД - то есть
        // делать и on delete cascade и cascadeType.ALL?
        assertTrue(userService.delete(USER_1));
        assertFalse(userService.delete(-135L));
    }

}
