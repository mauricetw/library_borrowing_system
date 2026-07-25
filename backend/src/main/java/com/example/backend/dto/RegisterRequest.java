package com.example.backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String phone;
    private String password;
    private String name;
}
