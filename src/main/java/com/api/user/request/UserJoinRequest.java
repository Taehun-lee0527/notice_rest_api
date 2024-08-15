package com.api.user.request;

import lombok.Data;

@Data
public class UserJoinRequest {
    private String loginId;
    private String password;
}
