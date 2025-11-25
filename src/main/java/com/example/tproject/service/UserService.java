package com.example.tproject.service;



import com.example.tproject.model.UserProfile;
import com.example.tproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public Optional<UserProfile> getUser(Long userId) {
        return userRepo.findById(userId);
    }

    public List<UserProfile> getAllUsers() {
        return userRepo.findAll();
    }
}
