package com.dmdev.spring.integration.database.repository;

import com.dmdev.spring.database.entity.Company;
import com.dmdev.spring.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@IT
@RequiredArgsConstructor
// Важно использовать именно спринговскую аннотацию
// использовать readOnly если будем только читать
@Transactional

// используется по умолчанию
//@Rollback
@Commit
class CompanyRepositoryTest {

    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;

    @Test
    void findById() {
        // используется когда нужно ручное управление транзакциями
        // вместо испоьзования transactionTemplate можно просто поставить
        // аннотацию @Transactional
        transactionTemplate.executeWithoutResult(tx -> {
            // но должны быть только RuntimeExceptions
            // иначе надо делать идок try-catch
            Company company = entityManager.find(Company.class, 1);
            assertNotNull(company);
            assertThat(company.getLocales()).hasSize(2);
        });
    }
}