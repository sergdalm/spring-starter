package com.dmdev.spring.database.repository;

import com.dmdev.spring.bpp.Auditing;
import com.dmdev.spring.bpp.Transaction;
import com.dmdev.spring.database.entity.Company;
import com.dmdev.spring.database.pool.ConnectionPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;


import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// Эти аннотация убрана после наследования от интерфейса org.springframework.data.repository.Repository
//import org.springframework.stereotype.@Repository
//@Transaction
//@Auditing
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    // Optional, Entity, Future
    // В аннотации Query мы указываем имя именованного запроса
    // чтобы сразу было понятно какой запрос будет выполнятся,
    // тогда это выглядит явно и более читабельно
//    @Query(name = "Company.findByName")
    // Также в аннотации Query можно писать обычный hql/sql запрос.
    // По умолчанию запрос пишется в hql, чтобы написать в sql
    // надо поставить nativeQuery - true
    // Если В @NamedQuery параметр назван отлично от параметра в методе, то можно указать
    // нужный параметр в аннотации @Param - она используется чтобы не допускать ошибки и
    // не оперировать названием параметров, а также в более старых версиях Спринга и Джавы
    // у нас не было возможности сохранять название параметров когда мы создаём прокси..
    // Только с Java 8 и Spring 4 появилась возможность сохранять параметры как есть,
    // до этог еободимо было использовать @Param чтобы ссылаться на название параметра.
    @Query("select c from Company c join fetch c.locales cl where c.name = :name2")
    Optional<Company> findByName(@Param("name2")String name);

    // Containing - аналог LIKE, только с % слева и справа
    // All писать не обязательно, но желательно
    // Collection, Stream (batch, close) - можно получать не все записи сразу,
    // а постепенно в зависимости от batch size'а, и нужно обязательно закрывать.

    List<Company> findAllByNameContainingIgnoreCase(String fragment);

//    private final ConnectionPool connectionPool;
//    private final List<ConnectionPool> connectionPools;
//    private final Integer poolSize;
//
//    public CompanyRepository(ConnectionPool pool1,
//                             List<ConnectionPool> connectionPools,
//                             @Value("${db.pool.size}") Integer poolSize) {
//        // посмотреть не рухнет ли приложение из-за то что поле названо не как id бина
//        this.connectionPool = pool1;
//        this.connectionPools = connectionPools;
//        this.poolSize = poolSize;
//    }
//
//    @PostConstruct
//    private void init() {
//        log.warn("init company repository");
//    }
//
//    @Override
//    public Optional<Company> findById(Integer id) {
//        System.out.println("findById method..");
//        return Optional.of(new Company(id, null, Collections.emptyMap()));
//    }
//
//    @Override
//    public void delete(Company entity) {
//        log.info("delete method..");
//    }
}
