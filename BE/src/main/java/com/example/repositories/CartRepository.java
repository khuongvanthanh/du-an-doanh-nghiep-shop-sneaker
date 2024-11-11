package com.example.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.model.redis_model.Cart;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends CrudRepository<Cart, String> {
    List<Cart> findByUsername(String username);

    Optional<Cart> findByIdAndUsername(String id, String username);
}
