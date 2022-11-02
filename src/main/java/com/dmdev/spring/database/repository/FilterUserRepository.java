package com.dmdev.spring.database.repository;

import com.dmdev.spring.database.entity.User;
import com.dmdev.spring.dto.UserFilter;

import java.util.List;

public interface FilterUserRepository {

    // Тут тоже можно работать с проекциями
    List<User> findAllByFilter(UserFilter userFilter);
}
