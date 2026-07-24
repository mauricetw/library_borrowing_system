package com.example.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "`Inventory`") // 對應資料庫的 Inventory 表
@Data
public class Inventory {

    @Id
    @Column(name = "inventory_id")
    private String inventoryId;

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @Column(name = "store_time")
    private LocalDateTime storeTime;

    @Column(name = "status", nullable = false)
    private String status;
}