package com.example.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "`Book`") // 對應資料庫的 Book 表
@Data
public class Book {

    @Id
    @Column(name = "isbn")
    private String isbn;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "author")
    private String author;

    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;
}