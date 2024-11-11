package com.example.dto.response.clients.customer;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserInfoRes {
    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private String address;
}
