package com.api.user.service.impl;

import com.api.user.repository.UserRepository;
import com.api.user.request.UserJoinRequest;
import com.api.user.request.UserLoginRequest;
import com.api.user.entity.UserEntity;
import com.api.user.response.UserJoinResponse;
import com.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(UserEntity userEntity) {
        if(userRepository.findByLoginId(userEntity.getLoginId()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 회원입니다");
        }
        else{
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setCreator(userEntity.getLoginId());
            userEntity.setCreatedAt(LocalDateTime.now());
            userRepository.save(userEntity);
        }
    }
}
