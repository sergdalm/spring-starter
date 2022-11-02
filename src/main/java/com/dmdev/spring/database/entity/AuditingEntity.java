package com.dmdev.spring.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
// для того чтобы поля были унаследованы сущностью
@MappedSuperclass
// В Спринге не надо писать своего Listener'а, достаточно использовать существующий
@EntityListeners(AuditingEntityListener.class)
// Все сущности, которые наследуются от этой, будут автоматически аудироваться.
// Для того чтобы это работало необходима аннотация @EnableJpaAuditing
// в ApplicationRunner или конфигурации
public abstract class AuditingEntity<T extends Serializable> implements BaseEntity<T> {

    // Эти аннотации указывают Спрингу что именно нужно указывать в этих полях
    // в listener'е
    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant modifiedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;
}
