package com.example.service;

import com.example.model.redis_model.Token;

public interface TokenService {

    void deleteToken(String id);

    void saveToken(Token token);

    Token getToken(String id);
}
