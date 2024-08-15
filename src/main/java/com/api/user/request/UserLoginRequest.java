package com.api.user.request;

import lombok.Data;
import lombok.Getter;

@Data
public class UserLoginRequest {
    private String loginId;
    private String password;
}
