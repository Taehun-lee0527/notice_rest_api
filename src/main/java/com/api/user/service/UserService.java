package com.api.user.service;

import com.api.user.entity.UserEntity;
import com.api.user.request.UserJoinRequest;
import com.api.user.request.UserLoginRequest;
import com.api.user.response.UserJoinResponse;

public interface UserService {
    //로그인
//    UserEntity getUser(UserLoginRequest userLoginRequest);

    //회원가입
    void join(UserEntity userEntity);
    //회원정보수정

    //회원탈퇴
}
