package com.dmdev.spring.dto;

import org.springframework.beans.factory.annotation.Value;

public interface PersonalInfo2 {

    // Название должно совпадать с полем в ResultSet'е
    String getFirstname();

    String getLastname();

    String getBirthDate();

    // Используется SPeL.
    // Для того чтобы обратиться к объекту конкретной проекции используется target,
    // но можно использовать любой бин из Спринга и передать PersonalInfo2 туда
    // - на практики такое используется редко.
    @Value("#{target.firstname + ' ' + target.lastname}")
    String getFullName();
}
