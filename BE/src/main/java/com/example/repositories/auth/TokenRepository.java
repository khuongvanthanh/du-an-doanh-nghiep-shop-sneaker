package com.example.repositories.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.model.redis_model.Token;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {
}