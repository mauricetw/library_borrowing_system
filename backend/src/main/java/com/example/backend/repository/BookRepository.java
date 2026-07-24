package com.example.backend.repository;

import com.example.backend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    // 繼承 JpaRepository 就會自動擁有 findAll(), findById() 等基本功能
}