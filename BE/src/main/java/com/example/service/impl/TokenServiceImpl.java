package com.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.exception.AuthenticationExceptionCustom;
import com.example.model.redis_model.Token;
import com.example.repositories.auth.TokenRepository;
import com.example.service.TokenService;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public void deleteToken(String id) {
        Token token = getToken(id);
        this.tokenRepository.delete(token);
    }

    @Override
    public void saveToken(Token token) {
        this.tokenRepository.save(token);
    }

    @Override
    public Token getToken(String id) {
        return this.tokenRepository.findById(id).orElseThrow(() -> new AuthenticationExceptionCustom("Something went wrong!"));
    }
}
