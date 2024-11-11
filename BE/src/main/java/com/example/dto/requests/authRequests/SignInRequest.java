package com.example.dto.requests.authRequests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.example.enums.Platform;

@Data
public class SignInRequest {
    @NotBlank(message = "Username must be not null")
    private String username;
    @NotBlank(message = "Password must be not blank")
    private String password;

    //No require
    @NotNull(message = "Platform must be not null")
    private Platform platform;
    private String deviceToken;
    private String version;
}