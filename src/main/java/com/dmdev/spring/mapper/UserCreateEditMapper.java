package com.dmdev.spring.mapper;

import com.dmdev.spring.database.entity.Company;
import com.dmdev.spring.database.entity.User;
import com.dmdev.spring.database.repository.CompanyRepository;
import com.dmdev.spring.dto.UserCreateEditDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User> {

    // это ОК когда в mapper есть репозиторий чтобы
    private final CompanyRepository companyRepository;

    @Override
    public User map(UserCreateEditDto object) {
        User user = new User();
        copy(object, user);
        return user;
    }

    @Override
    public User map(UserCreateEditDto fromObject, User toObject) {
        // копируем в
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(UserCreateEditDto object, User user) {
        user.setUsername(object.getUsername());
        user.setFirstname(object.getFirstname());
        user.setLastname(object.getLastname());
        user.setBirthDate(object.getBirthDate());
        // Если бы мы не передавали роль в dto, то устанавливали бы роль по умолчанию
        user.setRole(object.getRole());
        user.setCompany(getCompanyById(object.getCompanyId()));
    }

    private Company getCompanyById(Integer companyId) {
        // Тут мы страхуемся от двух вещей:
        // 1) companyId может быть null
        // 2) по такой companyId может не быть записи в БД
        // (в зависимости от бизнес-логики тут можно пробрасывать exception)
        return Optional.ofNullable(companyId)
                .flatMap(companyRepository::findById)
                .orElse(null);
    }

}
