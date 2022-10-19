package com.dmdev.spring.service;

import com.dmdev.spring.database.repositery.CompanyRepository;
import com.dmdev.spring.database.repositery.UserRepository;

public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public UserService(UserRepository userRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }
}
