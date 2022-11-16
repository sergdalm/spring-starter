package com.dmdev.spring.mapper;

import com.dmdev.spring.database.entity.User;
import com.dmdev.spring.dto.CompanyReadDto;
import com.dmdev.spring.dto.UserReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final CompanyReadMapper companyReadMapper;
    @Override
    // Можно делать это с помощью фреймворка MapStruct
    public UserReadDto map(User object) {
        // Если отсутствие компании у юзера это исключительная ситуация - мы пробрасываем exception,
        // иначе просто проставляем null
        CompanyReadDto companyReadDto = Optional.ofNullable(object.getCompany())
                .map(companyReadMapper::map)
                .orElse(null);
        return new UserReadDto(
                object.getId(),
                object.getUsername(),
                object.getBirthDate(),
                object.getFirstname(),
                object.getLastname(),
                object.getImage(),
                object.getRole(),
                companyReadDto
        );
    }
}
