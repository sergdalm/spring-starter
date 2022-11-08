package com.dmdev.spring.dto;

import com.dmdev.spring.database.entity.Role;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Value
@FieldNameConstants
public class UserCreateEditDto {
     String username;
     LocalDate birthDate;
     String firstname;
     String lastname;
     Role role;
     Integer companyId;
}
