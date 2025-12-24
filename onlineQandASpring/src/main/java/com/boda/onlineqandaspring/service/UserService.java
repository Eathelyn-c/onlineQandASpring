package com.boda.onlineqandaspring.service;

import com.boda.onlineqandaspring.model.User;
import com.boda.onlineqandaspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void registerUser(String username, String password) {
        userRepository.addUser(username, password);
    }
}
