package com.example.tproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private Long id;
    private Long userId;
    private double amount;
    private String location;      // e.g. "Delhi"
    private String merchant;      // name/id
    private String device;        // e.g. "Android", "Chrome"
    private LocalDateTime timestamp;

    // getters, setters, constructors
}
