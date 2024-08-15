package com.api.user.service;

import com.api.user.entity.AccountEntity;

public interface UserService {
    //로그인
//    UserEntity getUser(UserLoginRequest userLoginRequest);

    //회원가입
    void join(AccountEntity accountEntity);
    //회원정보수정

    //회원탈퇴
}
