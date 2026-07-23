USE library_db;

-- 插入測試書籍
INSERT INTO `Book` (isbn, name, author, introduction) VALUES 
('978-986-123-456', 'Spring Boot 實戰', 'Java大師', '這是一本學Java必看的書'),
('978-986-987-654', 'Vue.js 前端開發', '前端之神', '掌握前端框架的鑰匙');

-- 插入測試庫存
INSERT INTO `Inventory` (inventory_id, isbn, status) VALUES 
('INV-001', '978-986-123-456', '在庫'),
('INV-002', '978-986-123-456', '在庫'),
('INV-003', '978-986-987-654', '在庫');