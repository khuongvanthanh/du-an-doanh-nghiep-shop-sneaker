package com.example.repositories.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("FROM User WHERE LOWER(username) = LOWER(:username) OR LOWER(email) = LOWER(:username)")
    Optional<User> findUserByUsernameOrEmailIgnoreCase(String username);
}
