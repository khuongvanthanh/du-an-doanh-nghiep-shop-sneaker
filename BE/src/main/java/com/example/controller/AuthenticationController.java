package com.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.dto.requests.authRequests.SignInRequest;
import com.example.dto.response.ResponseData;
import com.example.dto.response.auth.TokenResponse;
import com.example.enums.UserRole;
import com.example.repositories.auth.UserAuthRepository;
import com.example.service.AuthenticationService;

@Slf4j
@RestController
@RequestMapping("/api/${api.version}/auth")
@Tag(name = "Authentication Controller", description = "Handles authentication-related operations such as login, logout, and token management.")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final UserAuthRepository userAuthRepository;

    /**
     * @param request {'username': 'your_username', 'password': 'your_password', 'platform': 'WEB|ANDROID|IOS', 'deviceToken': 'your_deviceToken', 'version': 'your_version'}
     */
    @Operation(summary = "Access token", description = "Enter a valid username and password to generate the access_token and refresh_token used to log in")
    @PostMapping("/access")
    public ResponseEntity<TokenResponse> access(@Valid @RequestBody SignInRequest request) {
        return ResponseEntity.ok(this.authenticationService.authenticate(request));
    }

    /**
     * @param request Header: Authorization
     */
    @Operation(
            summary = "Refresh Access Token",
            description = "Accepts a refresh_token and generates a new access_token."
    )
    @PostMapping("/refresh")
    public ResponseData<?> refresh(HttpServletRequest request) {
        log.info("Request refresh token");
        return new ResponseData<>(HttpStatus.OK.value(), "Success", authenticationService.refresh(request));
    }

    /**
     * @param request Header: Authorization
     */
    @Operation(
            summary = "Logout and Remove Token",
            description = "Accepts a refresh_token and removes the associated access_token from the database, effectively logging out the user."
    )
    @PostMapping("/remove")
    public ResponseData<?> logout(HttpServletRequest request) {
        log.info("Request remove token");
        authenticationService.logout(request);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Success");
    }

    /**
     * @param request Header: Authorization
     */
    @Operation(
            summary = "Get User",
            description = "Find user for get info to display data."
    )
    @GetMapping("/user")
    public ResponseData<?> getUser(HttpServletRequest request, UserRole role) {
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Success", this.userAuthRepository.getUser(request, role));
    }
}
