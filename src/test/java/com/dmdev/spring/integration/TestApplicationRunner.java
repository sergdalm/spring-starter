package com.dmdev.spring.integration;

import com.dmdev.spring.database.pool.ConnectionPool;
import com.dmdev.spring.service.UserService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;

// ищет аннотацию SpringBootApplication
@TestConfiguration
public class TestApplicationRunner {

    // делает бин spy'ем
    // теперь во всех тестах этот бин будет spy'ем
    @SpyBean(name = "pool1")
    private ConnectionPool pool1;
}
