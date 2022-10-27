package com.dmdev.spring.integration.service;

import com.dmdev.spring.database.pool.ConnectionPool;
import com.dmdev.spring.integration.annotation.IT;
import com.dmdev.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.mock.mockito.SpyBeans;
import org.springframework.test.annotation.DirtiesContext;

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

    private final UserService userService;
    private final ConnectionPool pool1;

    @Test
    void test() {
        System.out.println();
    }

    @Test
    void test1() {
        System.out.println();
    }

    @Test
    void test2() {
        System.out.println();
    }

}
