package com.dmdev.spring.database.repository;

import com.dmdev.spring.database.entity.Role;
import com.dmdev.spring.database.entity.User;
import com.dmdev.spring.database.pool.ConnectionPool;
import com.dmdev.spring.dto.PersonalInfo;
import com.dmdev.spring.dto.PersonalInfo2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.NamedEntityGraph;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, FilterUserRepository, QuerydslPredicateExecutor<User> {

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

    // для установки оптимистических и пессимистических блокировок на строках
    @Lock(LockModeType.PESSIMISTIC_READ)
    List<User> findTop3ByBirthDateBefore(LocalDate birthDat, Sort sort);


    // value - название именованного графа,
    // attributePaths - динамически определяем ассоциации, которые нужно подтянуть (название полей)
    // type - по умолчанию FETCH, можно поставить LOAD
//    @EntityGraph("User.company")
    // Без NamedEntityGraph лаконичнее и удобнее делать запросы на subgraph.
    // Но с subgraph Pageable не будет работать или будет работать неправильно,
    // limit и offset не будут работать, плохо для перфоманса (будут доставаться все юзеры и
    // на уровне приложения будут отсекаться нужное количество)
    // Т.о. с маппингами OneToMay и ManyToMany нужно осторожнее работаться с
    // Pageable и limit & offset - они не будут работать корректно (ошибка или плохой перфоманс)
    @EntityGraph(attributePaths = {"company", "company.locales"})
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

    // Главное чтобы в dto название полей соответствовали названиям полей в сущностях
    // (запрос с проекцией)
    <T> List<T> findAllByCompanyId(Integer companyId, Class<T> clazz);

    // Пример проекции на основе интерфейса с нативным запросом.
    // Здесь нужно возвращать только те поля, на которые есть геттеры в интерфейсе.
    // Чтобы перевести название полей snake_case в camalCase нужно использовать альясы.
    // Таким образом в нативном запросе можно получать любые поля (агрегирующие функции, из left join'ов).
    // (у меня выкидывает ошибку при переносе строки в нативном запросе)
    @Query(nativeQuery = true,
    value = "SELECT firstname, lastname, birth_date birthDate FROM users WHERE company_id = :companyId")
    List<PersonalInfo2> findAllByCompanyId2(Integer companyId);

    Optional<User> findByUsername(String username);
}
