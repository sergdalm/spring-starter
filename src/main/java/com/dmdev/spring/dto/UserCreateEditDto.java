package com.dmdev.spring.dto;

import com.dmdev.spring.database.entity.Role;
import com.dmdev.spring.validation.UserInfo;
import com.dmdev.spring.validation.group.CreateAction;
import com.dmdev.spring.validation.group.UpdateAction;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
@FieldNameConstants
// указываем группу чтобы проверять только при создании, а при обновлении не проверять
@UserInfo(groups = UpdateAction.class)
public class UserCreateEditDto {

    @Email
    String username;

    @NotBlank(groups = CreateAction.class)
    String rowPassword;

    LocalDate birthDate;

    // для строк лучше подойдёт аннотации @NotBlank или @NotEmpty, а не @NotNull
    @Size(min = 3, max = 64)
    String firstname;

    @NotNull
    String lastname;

    Role role;
    Integer companyId;

    // Название этого поля должно соответствовать атрибуту name в теге input
    MultipartFile image;
}
