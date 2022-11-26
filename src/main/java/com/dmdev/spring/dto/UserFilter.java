package com.dmdev.spring.dto;

import com.dmdev.spring.database.entity.Role;

import java.time.LocalDate;
import java.util.List;

public record UserFilter(String firstname,
                         String lastname,
                         LocalDate birthDate,
                         List<Role> roles) {
}
