package com.dmdev.spring.integration.database.repository;

import com.dmdev.spring.bpp.Transaction;
import com.dmdev.spring.database.entity.Role;
import com.dmdev.spring.database.entity.User;
import com.dmdev.spring.database.repository.UserRepository;
import com.dmdev.spring.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
class UserRepositoryTest {

    private final UserRepository userRepository;

    @Test
    void checkPageable() {
        // Позволяет создавать limit и offset для запросов динамически, а также сортировка.
        // PageRequest это основная реализация Pageable
        // of() - номер страницы (offset) и размер страницы (limit), сортировка (опционально);
        // тут page это то, сколько раз будет пропущено записей (каждый раз будет пропускаться столько записей, сколько указано limit)
        // (записи разделяются по limit и из них формируются page, мы указываем с какой по счёт page начинать (отсчёт идёт с 0)
        PageRequest pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
        Slice<User> slice = userRepository.findAllBy(pageable);
        System.out.println(slice);
        assertThat(slice).hasSize(2);
        slice.forEach(user -> System.out.println(user.getId()));

        while (slice.hasNext()) {
            slice = userRepository.findAllBy(slice.nextPageable());
            slice.forEach(user -> System.out.println(user.getId()));
        }
    }

    @Test
    void checkSOrt() {
        // В Sort.by() по умолчанию используется сортировка ascending
        Sort sort = Sort.by("firstname").descending().and(Sort.by("lastname"));
        // Вариант без захардкоженных строк
        Sort.TypedSort<User> sortBy = Sort.sort(User.class);
        Sort sort2 = sortBy.by(User::getFirstname).and(sortBy.by(User::getLastname));
        List<User> allUsers = userRepository.findTop3ByBirthDateBefore(LocalDate.now(), sort2);
        assertThat(allUsers).hasSize(3);
    }

    @Test
    void checkFirstTop() {
        Optional<User> topUser = userRepository.findTopByOrderByIdDesc();
        assertTrue(topUser.isPresent());
        topUser.ifPresent(user -> assertEquals(38L, topUser.get().getId()));
    }

    @Test
    void checkUpdate() {
        User ivan = userRepository.getById(34L);
        assertSame(Role.ADMIN, ivan.getRole());
        LocalDate birthday = LocalDate.now();
        ivan.setBirthDate(birthday);

        int resultCount = userRepository.updateRole(Role.USER, 34L, 38L);
        assertEquals(2, resultCount);

        // тут ошибка, т.к. LAZY, а контекст почищен
        System.out.println(ivan.getCompany().getName());

        User theSameIvan = userRepository.getById(34L);
        // это сравнивание ссылок (==)
        assertSame(Role.USER, theSameIvan.getRole());
        assertEquals(birthday, theSameIvan.getBirthDate());
    }

    @Test
    void checkQueries() {
        final List<User> users = userRepository.findAllBy("a", "ov");
        assertThat(users).hasSize(3);
        System.out.println(users);
    }
}