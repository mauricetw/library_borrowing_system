package com.example.backend.repository;

import com.example.backend.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {

    // 尋找某本書籍下的所有庫存
    List<Inventory> findByIsbn(String isbn);

    // 呼叫我們在 MySQL 寫好的 Stored Procedure (處理併發借書與 Transaction)
    @Procedure(procedureName = "sp_BorrowBook")
    String callBorrowBookSp(@Param("p_user_id") String userId, @Param("p_inventory_id") String inventoryId);
}