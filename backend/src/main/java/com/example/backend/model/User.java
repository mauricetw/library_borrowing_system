package com.example.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "`User`") // 對應資料庫的 User 表
@Data // Lombok 自動生成 Getter, Setter, toString 等
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "registration_time")
    private LocalDateTime registrationTime;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;
}