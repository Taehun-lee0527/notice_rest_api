package com.api.user.repository.impl;

import com.api.user.repository.CustomUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.api.user.entity.AccountEntity;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends QuerydslRepositorySupport implements CustomUserRepository {
    private final JPAQueryFactory queryFactory;
    public UserRepositoryImpl(EntityManager entityManager){
        super(AccountEntity.class);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


}
