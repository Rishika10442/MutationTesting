package com.example.tproject.repository;

import com.example.tproject.model.UserProfile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private static final String FILE_PATH = "src/main/resources/data/users.json";

    private List<UserProfile> users = new ArrayList<>();

    private final ObjectMapper mapper =
            new ObjectMapper().registerModule(new JavaTimeModule());

    @PostConstruct
    public void loadUsers() {
        try {
            users = mapper.readValue(
                    new File(FILE_PATH),
                    new TypeReference<List<UserProfile>>() {}
            );

            users = new ArrayList<>(users);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load users.json", e);
        }
    }

    public Optional<UserProfile> findById(Long id) {
        return users.stream().filter(u -> u.getUserId().equals(id)).findFirst();
    }

    public List<UserProfile> findAll() {
        return users;
    }
}
