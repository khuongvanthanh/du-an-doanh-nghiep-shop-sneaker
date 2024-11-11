package com.example.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private String username;

    private String fullName;

    private String email;

    private String avatar;
}
