package com.example.backend.repository;

import com.example.backend.model.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Integer> {

    // 找尋某個使用者所有「尚未歸還」的書籍 (returnTime 為 null)
    List<BorrowingRecord> findByUserIdAndReturnTimeIsNull(String userId);

    // 找尋某個使用者的所有借閱紀錄
    List<BorrowingRecord> findByUserId(String userId);

    // 找尋特定庫存的借閱紀錄（用來還書時確認）
    Optional<BorrowingRecord> findByInventoryIdAndReturnTimeIsNull(String inventoryId);
}