package com.example.backend.service;

import com.example.backend.model.Book;
import com.example.backend.model.BorrowingRecord;
import com.example.backend.model.Inventory;
import com.example.backend.repository.BookRepository;
import com.example.backend.repository.BorrowingRecordRepository;
import com.example.backend.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LibraryService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private BorrowingRecordRepository recordRepository;

    // 取得所有書籍
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // 取得特定書籍的所有庫存狀態
    public List<Inventory> getBookInventories(String isbn) {
        return inventoryRepository.findByIsbn(isbn);
    }

    // 借書邏輯 (依賴資料庫的 Stored Procedure)
    public String borrowBook(String userId, String inventoryId) {
        // 呼叫我們在 InventoryRepository 中定義好的 sp_BorrowBook 預存程序
        // 它會回傳 'SUCCESS', 'FAIL_UNAVAILABLE' 或 'FAIL_ERROR'
        return inventoryRepository.callBorrowBookSp(userId, inventoryId);
    }

    // 取得某使用者的所有借閱紀錄
    public List<BorrowingRecord> getUserRecords(String userId) {
        return recordRepository.findByUserId(userId);
    }

    // 還書邏輯 (使用 Transaction 確保資料一致性)
    @Transactional(rollbackFor = Exception.class)
    public void returnBook(String userId, Integer recordId) {
        // 1. 找出這筆借閱紀錄
        BorrowingRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("找不到此借閱紀錄"));

        // 2. 安全檢查：確認這本書是這個人借的，且尚未歸還
        if (!record.getUserId().equals(userId)) {
            throw new RuntimeException("您無權歸還此書籍！");
        }
        if (record.getReturnTime() != null) {
            throw new RuntimeException("此書籍已經歸還過了！");
        }

        // 3. 找出對應的庫存
        Inventory inventory = inventoryRepository.findById(record.getInventoryId())
                .orElseThrow(() -> new RuntimeException("找不到對應庫存資料"));

        // 4. 更新借閱紀錄的還書時間
        record.setReturnTime(LocalDateTime.now());
        recordRepository.save(record);

        // 5. 更新庫存狀態為 '在庫'
        inventory.setStatus("在庫");
        inventoryRepository.save(inventory);

        // 備註：因為方法上方加了 @Transactional，
        // 如果步驟 4 或步驟 5 發生任何錯誤（如資料庫連線中斷），
        // 整個過程會自動 Rollback (回退)，保證不會出現「紀錄已還，但狀態還是出借中」的錯亂情況。
    }
}
