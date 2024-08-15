package com.api.user.service.impl;

import com.api.user.entity.AccountEntity;
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
        Optional<AccountEntity> userOptional = userRepository.findByLoginId(loginId);

        AccountEntity accountEntity = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("User not found with username: " + loginId));

        return new org.springframework.security.core.userdetails.User(
                accountEntity.getUsername(),
                accountEntity.getPassword(),
//                passwordEncoder.encode(userEntity.getPassword()),
                accountEntity.getAuthorities() // 권한 설정
        );
    }
}