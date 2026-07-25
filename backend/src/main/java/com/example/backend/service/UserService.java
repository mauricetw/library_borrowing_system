package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    // 註冊邏輯
    public void register(String phone, String rawPassword, String name) {
        // 1. 檢查手機號碼是否已存在
        if (userRepository.existsByPhoneNumber(phone)) {
            throw new RuntimeException("該手機號碼已被註冊！");
        }

        // 2. 建立新使用者
        User user = new User();
        user.setUserId(UUID.randomUUID().toString()); // 自動產生唯一 ID
        user.setPhoneNumber(phone);
        user.setUserName(name);
        user.setRegistrationTime(LocalDateTime.now());

        // 3. 密碼加鹽與雜湊後再存入 (防止明碼外洩)
        user.setPassword(passwordEncoder.encode(rawPassword));

        userRepository.save(user);
    }

    // 登入邏輯
    public String login(String phone, String rawPassword) {
        // 1. 依手機號碼尋找使用者
        User user = userRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new RuntimeException("找不到此使用者，請先註冊。"));

        // 2. 比對密碼 (將前端傳來的明碼與資料庫的雜湊密碼比對)
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("密碼錯誤！");
        }

        // 3. 更新最後登入時間
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        // 4. 產生並回傳 JWT Token
        return jwtUtils.generateToken(phone);
    }
}
