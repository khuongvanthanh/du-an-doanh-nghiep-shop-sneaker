package com.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.example.exception.EntityNotFoundException;
import com.example.repositories.auth.UserRepository;
import com.example.service.UserService;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> this.userRepository.findUserByUsernameOrEmailIgnoreCase(username)
                .filter(user -> user.getUsername().equals(username) || user.getEmail().equals(username))
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with username '%s' not found!", username)));

    }
}
