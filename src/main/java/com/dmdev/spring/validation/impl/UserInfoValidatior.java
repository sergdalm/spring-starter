package com.dmdev.spring.validation.impl;

import com.dmdev.spring.dto.UserCreateEditDto;
import com.dmdev.spring.validation.UserInfo;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.hasText;

// Вместо того чтобы указывать конкретный класс UserCreateEditDto можно сделать интерфейс,
// у которого были бы методы getFirstName() и getLastName() и поставить туда интерфейс.
// Все валидарторы являются Спрнг бинами. Можно поставить анотацию @Component чтобы было видно,
// но и без этой аннотации можно инжектить другие бины
public class UserInfoValidatior implements ConstraintValidator<UserInfo, UserCreateEditDto> {

    @Override
    public boolean isValid(UserCreateEditDto value, ConstraintValidatorContext constraintValidatorContext) {
        return hasText(value.getFirstname()) || hasText(value.getLastname());
    }
}
