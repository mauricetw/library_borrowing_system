USE library_db;
DELIMITER //

CREATE PROCEDURE `sp_BorrowBook`(
    IN p_user_id VARCHAR(50),
    IN p_inventory_id VARCHAR(50),
    OUT p_result VARCHAR(50)
)
BEGIN
    DECLARE v_status VARCHAR(20);
    
    -- 發生錯誤時的例外處理 (Rollback Transaction)
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN
        ROLLBACK;
        SET p_result = 'FAIL_ERROR';
    END;

    START TRANSACTION;
    
    -- 檢查庫存狀態 (FOR UPDATE 會鎖定該行，防止併發借閱)
    SELECT status INTO v_status FROM `Inventory` WHERE inventory_id = p_inventory_id FOR UPDATE;
    
    IF v_status = '在庫' THEN
        -- 1. 更新庫存狀態
        UPDATE `Inventory` SET status = '出借中' WHERE inventory_id = p_inventory_id;
        -- 2. 新增借閱紀錄
        INSERT INTO `Borrowing_Record` (user_id, inventory_id, borrowing_time) VALUES (p_user_id, p_inventory_id, NOW());
        
        COMMIT;
        SET p_result = 'SUCCESS';
    ELSE
        ROLLBACK;
        SET p_result = 'FAIL_UNAVAILABLE';
    END IF;
    
END //

DELIMITER ;