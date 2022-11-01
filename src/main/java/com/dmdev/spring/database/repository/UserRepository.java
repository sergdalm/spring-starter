package com.dmdev.spring.database.repository;

import com.dmdev.spring.database.entity.Role;
import com.dmdev.spring.database.entity.User;
import com.dmdev.spring.database.pool.ConnectionPool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // В Spring Data можно ставить % перед и после параметров - это не hql стандарт,
    // он поддерживается только в Spring Data, - тогда это аналогично Containing
    @Query("select u from User u where u.firstname like %:firstname% and u.lastname like %:lastname%")
    List<User> findAllBy(String firstname, String lastname);

    // Для native queries также нужно возвращать в resultSet поля,
    // относящиеся к сущности Repository (в нашем случае - это User)
    // или использовать проекции (с ними чаще используются).
    // Так как это SQL, принято все ключевые слова писать в uppercase.
    // Но SQL использовать не стоит потому что там нет fetch и EntityGraph.
    @Query(value = "SELECT * FROM users u WHERE u.username = :username",
            nativeQuery = true)
    List<User> findAllByUsername(String username);

    // По умолчанию @Query используется только для select запросов,
    // для того чтобы делать другие запросы и операции нужно использовать аннотацию @Modifying.
    // clearAutomatically - чистит весь persistence context после выполнения запрос.
    // Но в таком случае нельзя пользоваться старыми сущностями, иначе можно словить LazyInitializationException.
    // flushAutomatically - flush и так делается автоматически, но можно поставить true
    // чтобы обезопасить себя (на случай если настройки по умолчанию изменили)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User u set u.role = :role where u.id in (:ids)")
    int updateRole(Role role, Long... ids);

    // методы findTopBy, findFirstBy, findDistinctFirstBy, findDistinctTopBy - идентичны
    // находят первый элемент из выборки
    Optional<User> findTopByOrderByIdDesc();

    List<User> findTop3ByBirthDateBefore(LocalDate birthDat, Sort sort);


    // countQuery - hql запрос для того чтобы получить count
    @Query(value = "select u from User u", countQuery = "select count(distinct u.firstname) from User u")
    // Также Pageable можно передавать в запрос дополнительным параметром
    // Pageable позволяет возвращать Streamable (по сути обертка вокруг итератора),
    // (на практике Streamable редко используется потому что у него ограниченный API)
    // Slice (наследник от Streamable), Page (наследуется от Slice)
    // Особенность Slice: мы можем получить следующий offset с тем же лимитом,
    // в нём хранится следующий Pageable.
    // На практике Slice не так часто используется потому что его недостаточно
    // потому что мы не знаем общее количество страниц - нужен count,
    // для этого нужен объект Page - там есть дополнительный запрос на count.
    // Поэтому чаще всего используется Page, он добавляет методы getTotalElements() и getTotalPages()
    Page<User> findAllBy(Pageable pageable);


}
