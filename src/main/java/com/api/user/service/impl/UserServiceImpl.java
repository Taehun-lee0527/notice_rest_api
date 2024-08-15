package com.api.user.service.impl;

import com.api.user.entity.AccountEntity;
import com.api.user.repository.UserRepository;
import com.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(AccountEntity accountEntity) {
        if(userRepository.findByLoginId(accountEntity.getLoginId()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 회원입니다");
        }
        else{
            accountEntity.setPassword(passwordEncoder.encode(accountEntity.getPassword()));
            accountEntity.setCreator(accountEntity.getLoginId());
            accountEntity.setCreatedAt(LocalDateTime.now());
            userRepository.save(accountEntity);
        }
    }
}
