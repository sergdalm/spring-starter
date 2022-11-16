package com.dmdev.spring.database.repository;

import com.dmdev.spring.database.entity.User;
import com.dmdev.spring.dto.UserFilter;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
// Имплементация должна совпадать названием интерфейса + постфикс Impl
public class FilterUserRepositoryImpl implements FilterUserRepository{

    private final EntityManager entityManager;

    // Динамическая реализация запроса - в зависимости от фильтра
    @Override
    public List<User> findAllByFilter(UserFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);
        criteria.select(user);

        Predicate[] predicates = CriteriaPredicate.builder()
                .add(filter.firstname(), obj -> cb.like(user.get("firstname"), obj))
                .add(filter.lastname(), obj -> cb.like(user.get("lastname"), obj))
                .add(filter.birthDate(), obj -> cb.lessThan(user.get("birthDate"), obj))
                .build();

        criteria.where(predicates);

        return entityManager.createQuery(criteria).getResultList();
    }
}
