package com.dmdev.spring.dto;

import com.dmdev.spring.database.entity.Role;
import lombok.Value;

import java.time.LocalDate;

@Value
// record не подойдёт, потому что в EL будут неявно вызываться геттеры, а в record'ах не тот формат get методов
public class UserReadDto {
    Long id;
    String username;
    LocalDate birthDate;
    String firstname;
    String lastname;
    Role role;
    CompanyReadDto company;
}
