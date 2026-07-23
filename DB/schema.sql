-- 建立資料庫
CREATE DATABASE IF NOT EXISTS library_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE library_db;

-- 1. 使用者表 User
CREATE TABLE `User` (
    `user_id` VARCHAR(50) PRIMARY KEY,
    `phone_number` VARCHAR(20) UNIQUE NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `user_name` VARCHAR(100) NOT NULL,
    `registration_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `last_login_time` DATETIME NULL
);

-- 2. 書籍表 Book
CREATE TABLE `Book` (
    `isbn` VARCHAR(20) PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `author` VARCHAR(100),
    `introduction` TEXT
);

-- 3. 庫存表 Inventory
CREATE TABLE `Inventory` (
    `inventory_id` VARCHAR(50) PRIMARY KEY,
    `isbn` VARCHAR(20) NOT NULL,
    `store_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `status` VARCHAR(20) NOT NULL COMMENT '在庫, 出借中, 整理中, 遺失, 損毀, 廢棄',
    FOREIGN KEY (`isbn`) REFERENCES `Book`(`isbn`)
);

-- 4. 借閱紀錄表 Borrowing Record
CREATE TABLE `Borrowing_Record` (
    `record_id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` VARCHAR(50) NOT NULL,
    `inventory_id` VARCHAR(50) NOT NULL,
    `borrowing_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `return_time` DATETIME NULL,
    INDEX idx_user (`user_id`),
    INDEX idx_inventory (`inventory_id`),
    FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`),
    FOREIGN KEY (`inventory_id`) REFERENCES `Inventory`(`inventory_id`)
);
