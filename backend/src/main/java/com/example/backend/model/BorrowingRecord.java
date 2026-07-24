package com.example.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "`Borrowing_Record`") // 對應資料庫的 Borrowing_Record 表
@Data
public class BorrowingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 設定為自動遞增 (AUTO_INCREMENT)
    @Column(name = "record_id")
    private Integer recordId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "inventory_id", nullable = false)
    private String inventoryId;

    @Column(name = "borrowing_time")
    private LocalDateTime borrowingTime;

    @Column(name = "return_time")
    private LocalDateTime returnTime;
}