package com.example.backend.Service;

import com.example.backend.Repository.UserRepository;
import com.example.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    // Get users by status (ONLINE, IDLE, OFFLINE)
    public List<User> getUsersByStatus(String status) {
        return userRepository.findByStatus(status);
    }
    // Get users by username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
