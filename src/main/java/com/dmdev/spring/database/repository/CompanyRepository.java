package com.dmdev.spring.database.repository;

import com.dmdev.spring.bpp.Auditing;
import com.dmdev.spring.bpp.Transaction;
import com.dmdev.spring.database.entity.Company;
import com.dmdev.spring.database.pool.ConnectionPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;


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
    Optional<Company> findByName(String name);

    // Containing - аналог LIKE, только с % слева и справа
    // All писать не обязательно, но желательно
    // Collection, Stream (batch, close) - можно получать не все записи сразу,
    // а постепенно в зависимости от batch size'а, и нужно обязательно закрывать
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
