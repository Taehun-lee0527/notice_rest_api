package com.api.user.service.impl;

import com.api.user.entity.UserEntity;
import com.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepository.findByLoginId(loginId);

        UserEntity userEntity = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("User not found with username: " + loginId));

        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
//                passwordEncoder.encode(userEntity.getPassword()),
                userEntity.getAuthorities() // 권한 설정
        );
    }
}