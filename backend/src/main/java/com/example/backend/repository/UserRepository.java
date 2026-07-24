package com.example.backend.repository;

import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // Spring Data JPA 的魔法：只要按照命名規則，它會自動幫你寫出「用手機號碼找使用者」的 SQL
    Optional<User> findByPhoneNumber(String phoneNumber);

    // 檢查手機號碼是否已經被註冊過
    boolean existsByPhoneNumber(String phoneNumber);
}