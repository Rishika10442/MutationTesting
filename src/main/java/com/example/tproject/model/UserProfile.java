package com.example.tproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private Long userId;
    private String country;
    private List<String> usualLocations;
    private List<String> usualDevices;

    // constructor, getters, setters
}
