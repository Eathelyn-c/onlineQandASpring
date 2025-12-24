package com.boda.onlineqandaspring.repository;

import com.boda.onlineqandaspring.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class UserRepository {

    private final List<User> users = new ArrayList<>();
    private final AtomicInteger nextUserId = new AtomicInteger(1);

    public User findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public void addUser(String username, String password) {
        int userId = nextUserId.getAndIncrement();
        User user = new User(username, password, userId);
        users.add(user);
    }
}
