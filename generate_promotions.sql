-- 🎓 SCRIPT TẠO DỮ LIỆU MẪU KHUYẾN MÃI (30 BẢN GHI)
-- Ngày tạo: 2026-03-30
-- Trạng thái: Đang chạy (Status = 1)
-- Thời gian: Tháng này và tháng sau

USE java_computer_store;

-- 1. XÓA DỮ LIỆU CŨ ĐỂ TRÁNH TRÙNG MÃ (Tùy chọn)
DELETE FROM PromotionCampaigns WHERE PromotionID LIKE 'KM_SAMPLE_%';

-- 2. NHÓM 1: KHUYẾN MÃI CHUNG (10 MẪU)
INSERT INTO PromotionCampaigns (PromotionID, PromotionName, StartDate, EndDate, Status, Description) VALUES
('KM_SAMPLE_GEN_1', 'Ngày Hội Công Nghệ', '2026-03-15 08:00:00', '2026-04-15 23:59:59', 1, 'Ưu đãi cho toàn bộ khách hàng'),
('KM_SAMPLE_GEN_2', 'Siêu Sale Cuối Tuần', '2026-03-20 00:00:00', '2026-04-20 23:59:59', 1, 'Sale đậm mỗi thứ 7, CN'),
('KM_SAMPLE_GEN_3', 'Chào Tháng Mới', '2026-04-01 00:00:00', '2026-04-30 23:59:59', 1, 'Mừng tháng 4 rực rỡ'),
('KM_SAMPLE_GEN_4', 'Quà Tặng Tri Ân', '2026-03-25 08:00:00', '2026-04-25 21:00:00', 1, 'Tặng quà cho khách hàng thân thiết'),
('KM_SAMPLE_GEN_5', 'Flash Sale 10h', '2026-03-10 10:00:00', '2026-04-10 12:00:00', 1, 'Giảm giá chớp nhoáng mỗi ngày'),
('KM_SAMPLE_GEN_6', 'Uu Đãi Mùa Hè', '2026-04-05 08:00:00', '2026-05-05 23:59:59', 1, 'Giải nhiệt mùa hè cùng Computer Store'),
('KM_SAMPLE_GEN_7', 'Sắm Đồ Học Tập', '2026-03-28 07:00:00', '2026-04-28 19:00:00', 1, 'Phục vụ nhu cầu học tập sinh viên'),
('KM_SAMPLE_GEN_8', 'Lễ Hội Gaming', '2026-03-18 10:00:00', '2026-04-18 22:00:00', 1, 'Dành riêng cho các game thủ'),
('KM_SAMPLE_GEN_9', 'Tuần Lễ Linh Kiện', '2026-04-10 08:00:00', '2026-04-17 17:00:00', 1, 'Giảm giá sâu các loại Main, Card'),
('KM_SAMPLE_GEN_10', 'Big Event April', '2026-04-01 00:00:00', '2026-04-30 23:59:59', 1, 'Sự kiện lớn nhất tháng 4');

-- 3. NHÓM 2: KHUYẾN MÃI SẢN PHẨM (10 MẪU)
-- Lưu ý: Nếu mã SP không tồn tại, bạn có thể chạy lại script sau khi đã thêm SP.
INSERT INTO PromotionCampaigns (PromotionID, PromotionName, StartDate, EndDate, Status, Description) VALUES
('KM_SAMPLE_PROD_1', 'Sale Laptop Dell 10%', '2026-03-20 08:00:00', '2026-04-20 23:59:59', 1, 'Giảm linh kiện Laptop'),
('KM_SAMPLE_PROD_2', 'Sale Chuột Gaming', '2026-03-22 08:00:00', '2026-04-22 23:59:59', 1, 'Logitech, Razer ưu đãi sóc'),
('KM_SAMPLE_PROD_3', 'Sale RAM Kingston', '2026-04-01 08:00:00', '2026-04-15 23:59:59', 1, 'Nâng cấp bộ nhớ giá rẻ'),
('KM_SAMPLE_PROD_4', 'Sale Màn hình 4K', '2026-03-25 08:00:00', '2026-04-25 23:59:59', 1, 'Trải nghiệm hình ảnh chân thực'),
('KM_SAMPLE_PROD_5', 'Sale CPU Intel Gen 14', '2026-03-15 08:00:00', '2026-04-15 23:59:59', 1, 'Cấu hình mạnh mẽ cho Designer'),
('KM_SAMPLE_PROD_6', 'Sale Bàn phím Cơ', '2026-04-05 08:00:00', '2026-04-20 23:59:59', 1, 'Trải nghiệm gõ phím mượt mà'),
('KM_SAMPLE_PROD_7', 'Sale SSD Samsung', '2026-03-28 08:00:00', '2026-04-10 23:59:59', 1, 'Tăng tốc máy tính của bạn'),
('KM_SAMPLE_PROD_8', 'Sale Nguồn Corsair', '2026-03-18 08:00:00', '2026-04-05 23:59:59', 1, 'Ổn định điện áp cho dàn PC'),
('KM_SAMPLE_PROD_9', 'Sale VGA RTX 4090', '2026-04-01 08:00:00', '2026-04-30 23:59:59', 1, 'Top 1 đồ họa hiện nay'),
('KM_SAMPLE_PROD_10', 'Sale Case Bể Cá', '2026-03-30 08:00:00', '2026-04-30 23:59:59', 1, 'Phong cách tối giản cho góc làm việc');

-- Chèn vào bảng phụ ProductPromotions (Giả sử bạn có các ProductID tương ứng)
INSERT INTO ProductPromotions (PromotionID, ProductID, DiscountPercent)
SELECT 'KM_SAMPLE_PROD_1', ProductID, 10.0 FROM Products LIMIT 1;
INSERT INTO ProductPromotions (PromotionID, ProductID, DiscountPercent)
SELECT 'KM_SAMPLE_PROD_2', ProductID, 15.0 FROM Products LIMIT 1 OFFSET 1;
INSERT INTO ProductPromotions (PromotionID, ProductID, DiscountPercent)
SELECT 'KM_SAMPLE_PROD_3', ProductID, 20.0 FROM Products LIMIT 1 OFFSET 2;
INSERT INTO ProductPromotions (PromotionID, ProductID, DiscountPercent)
SELECT 'KM_SAMPLE_PROD_4', ProductID, 25.0 FROM Products LIMIT 1 OFFSET 3;
INSERT INTO ProductPromotions (PromotionID, ProductID, DiscountPercent)
SELECT 'KM_SAMPLE_PROD_5', ProductID, 30.0 FROM Products LIMIT 1 OFFSET 4;
INSERT INTO ProductPromotions (PromotionID, ProductID, DiscountPercent)
SELECT 'KM_SAMPLE_PROD_6', ProductID, 35.0 FROM Products LIMIT 1 OFFSET 5;
INSERT INTO ProductPromotions (PromotionID, ProductID, DiscountPercent)
SELECT 'KM_SAMPLE_PROD_7', ProductID, 40.0 FROM Products LIMIT 1 OFFSET 6;
INSERT INTO ProductPromotions (PromotionID, ProductID, DiscountPercent)
SELECT 'KM_SAMPLE_PROD_8', ProductID, 45.0 FROM Products LIMIT 1 OFFSET 7;
INSERT INTO ProductPromotions (PromotionID, ProductID, DiscountPercent)
SELECT 'KM_SAMPLE_PROD_9', ProductID, 50.0 FROM Products LIMIT 1 OFFSET 8;
INSERT INTO ProductPromotions (PromotionID, ProductID, DiscountPercent)
SELECT 'KM_SAMPLE_PROD_10', ProductID, 5.0 FROM Products LIMIT 1 OFFSET 9;

-- 4. NHÓM 3: KHUYẾN MÃI HÓA ĐƠN (10 MẪU)
INSERT INTO PromotionCampaigns (PromotionID, PromotionName, StartDate, EndDate, Status, Description) VALUES
('KM_SAMPLE_PRICE_1', 'Giảm Hóa Đơn 500k', '2026-03-20 08:00:00', '2026-04-20 23:59:59', 1, 'Dành cho hóa đơn từ 5 triệu'),
('KM_SAMPLE_PRICE_2', 'Giảm Hóa Đơn 1 Triệu', '2026-03-25 08:00:00', '2026-04-25 23:59:59', 1, 'Dành cho hóa đơn từ 10 triệu'),
('KM_SAMPLE_PRICE_3', 'Ưu Đãi Đặc Biệt 200k', '2026-04-01 08:00:00', '2026-04-30 23:59:59', 1, 'Dành cho hóa đơn từ 2 triệu'),
('KM_SAMPLE_PRICE_4', 'Mừng Đại Lễ Giảm 800k', '2026-03-28 08:00:00', '2026-04-10 23:59:59', 1, 'Dành cho hóa đơn từ 8 triệu'),
('KM_SAMPLE_PRICE_5', 'Big Voucher 1.5 Triệu', '2026-03-15 08:00:00', '2026-04-15 23:59:59', 1, 'Dành cho hóa đơn từ 15 triệu'),
('KM_SAMPLE_PRICE_6', 'Quà Tặng Tháng 4 - 300k', '2026-04-05 08:00:00', '2026-04-25 23:59:59', 1, 'Dành cho hóa đơn từ 3 triệu'),
('KM_SAMPLE_PRICE_7', 'Sắm Đồ Khủng Giảm 2 Triệu', '2026-03-20 08:00:00', '2026-04-30 23:59:59', 1, 'Dành cho hóa đơn từ 20 triệu'),
('KM_SAMPLE_PRICE_8', 'Voucher Học Sinh 100k', '2026-03-10 08:00:00', '2026-04-10 23:59:59', 1, 'Dành cho hóa đơn từ 1 triệu'),
('KM_SAMPLE_PRICE_9', 'Lương Về Giảm 600k', '2026-04-10 08:00:00', '2026-04-20 23:59:59', 1, 'Dành cho hóa đơn từ 6 triệu'),
('KM_SAMPLE_PRICE_10', 'Tri Ân Khách VIP 3 Triệu', '2026-03-30 08:00:00', '2026-05-30 23:59:59', 1, 'Dành cho hóa đơn từ 30 triệu');

-- Chèn vào bảng phụ InvoicePromotionConfigs
INSERT INTO InvoicePromotionConfigs (PromotionID, MinInvoiceValue, DiscountAmount, DiscountPercent, MaxDiscountValue) VALUES
('KM_SAMPLE_PRICE_1', 5000000.0, 500000.0, 0, NULL),
('KM_SAMPLE_PRICE_2', 10000000.0, 1000000.0, 0, NULL),
('KM_SAMPLE_PRICE_3', 2000000.0, 200000.0, 0, NULL),
('KM_SAMPLE_PRICE_4', 8000000.0, 800000.0, 0, NULL),
('KM_SAMPLE_PRICE_5', 15000000.0, 1500000.0, 0, NULL),
('KM_SAMPLE_PRICE_6', 3000000.0, 300000.0, 0, NULL),
('KM_SAMPLE_PRICE_7', 20000000.0, 2000000.0, 0, NULL),
('KM_SAMPLE_PRICE_8', 1000000.0, 100000.0, 0, NULL),
('KM_SAMPLE_PRICE_9', 6000000.0, 600000.0, 0, NULL),
('KM_SAMPLE_PRICE_10', 30000000.0, 3000000.0, 0, NULL);
