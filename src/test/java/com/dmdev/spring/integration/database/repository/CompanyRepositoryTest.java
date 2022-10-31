package com.dmdev.spring.integration.database.repository;

import com.dmdev.spring.database.entity.Company;
import com.dmdev.spring.database.repository.CompanyRepository;
import com.dmdev.spring.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
// Важно использовать именно спринговскую аннотацию
// использовать readOnly если будем только читать
@Transactional

// используется по умолчанию
//@Rollback
@Commit
class CompanyRepositoryTest {

    private static Integer APPLE_ID = 5;
    private final CompanyRepository companyRepository;
    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;

    @Test
    void checkFindByQueries() {
        companyRepository.findByName("Google");
        companyRepository.findAllByNameContainingIgnoreCase("a");
    }

    @Test
    void delete() {
        Optional<Company> maybeCompany = companyRepository.findById(APPLE_ID);
        assertTrue(maybeCompany.isPresent());
        maybeCompany.ifPresent(companyRepository::delete);
        // По умолчанию метод delete() не произойдет если не будет commit() или flush()
        entityManager.flush();
        assertTrue(companyRepository.findById(APPLE_ID).isEmpty());
    }

    @Test
    void findById() {
        // используется когда нужно ручное управление транзакциями
        // вместо испоьзования transactionTemplate можно просто поставить
        // аннотацию @Transactional
//        transactionTemplate.executeWithoutResult(tx -> {
//            // но должны быть только RuntimeExceptions
//            // иначе надо делать идок try-catch
//            Company company = entityManager.find(Company.class, 1);
//            assertNotNull(company);
//            assertThat(company.getLocales()).hasSize(1);
//        });
        Company company = entityManager.createQuery("select c from Company c where c.name =:companyName", Company.class)
                .setParameter("companyName", "Google")
                .getSingleResult();

        assertNotNull(company);
        assertThat(company.getLocales()).hasSize(2);
    }
}