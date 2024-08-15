package com.api.user.repository;

import com.api.user.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AccountEntity, String>, CustomUserRepository {
    Optional<AccountEntity> findByLoginId(String loginId);
}
