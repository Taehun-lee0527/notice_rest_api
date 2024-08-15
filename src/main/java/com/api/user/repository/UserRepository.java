package com.api.user.repository;

import com.api.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String>, CustomUserRepository {


    Optional<UserEntity> findByLoginId(String loginId);
    Optional<UserEntity> findByLoginIdAndPassword(String loginId, String password);
}
