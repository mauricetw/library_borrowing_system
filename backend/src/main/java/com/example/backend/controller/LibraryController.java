package com.example.backend.controller;

import com.example.backend.dto.BorrowRequest;
import com.example.backend.dto.ReturnRequest;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private UserRepository userRepository;

    // 輔助方法：從 Spring Security 的 Context 中獲取目前登入者的 userId
    private String getCurrentUserId() {
        // 在 JwtAuthenticationFilter 中，我們將 phone 存入 Context
        String phone = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new RuntimeException("找不到目前登入的使用者資訊"));
        return user.getUserId();
    }

    // 取得所有書籍
    @GetMapping("/books")
    public ResponseEntity<?> getAllBooks() {
        return ResponseEntity.ok(libraryService.getAllBooks());
    }

    // 取得特定書籍的所有庫存 (前端顯示用)
    @GetMapping("/books/{isbn}/inventories")
    public ResponseEntity<?> getBookInventories(@PathVariable String isbn) {
        return ResponseEntity.ok(libraryService.getBookInventories(isbn));
    }

    // 取得目前使用者的借閱紀錄
    @GetMapping("/records")
    public ResponseEntity<?> getUserRecords() {
        try {
            return ResponseEntity.ok(libraryService.getUserRecords(getCurrentUserId()));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 借書 API
    @PostMapping("/borrow")
    public ResponseEntity<?> borrowBook(@RequestBody BorrowRequest request) {
        try {
            String result = libraryService.borrowBook(getCurrentUserId(), request.getInventoryId());
            Map<String, String> response = new HashMap<>();
            response.put("status", result); // 回傳 SUCCESS, FAIL_UNAVAILABLE 或 FAIL_ERROR
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 還書 API
    @PostMapping("/return")
    public ResponseEntity<?> returnBook(@RequestBody ReturnRequest request) {
        try {
            libraryService.returnBook(getCurrentUserId(), request.getRecordId());
            Map<String, String> response = new HashMap<>();
            response.put("message", "還書成功！");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
