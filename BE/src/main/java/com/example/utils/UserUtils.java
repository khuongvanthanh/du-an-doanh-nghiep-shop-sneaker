package com.example.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.exception.EntityNotFoundException;
import com.example.model.User;
import com.example.repositories.auth.UserRepository;

@Component
public class UserUtils {

    private  static UserRepository userRepository;

    @Autowired
    public UserUtils(UserRepository userRepository) {
        UserUtils.userRepository = userRepository;
    }

    public static User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
