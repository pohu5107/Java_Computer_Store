-- Script tạo bảng Promotions cho hệ thống quản lý cửa hàng máy tính

CREATE TABLE IF NOT EXISTS Promotions (
    PromotionID VARCHAR(10) PRIMARY KEY,
    PromotionName NVARCHAR(100) NOT NULL,
    Type VARCHAR(10) NOT NULL CHECK (Type IN ('Product', 'Price')),
    ProductID VARCHAR(10),
    DiscountPercent DOUBLE,
    MinAmount DOUBLE,
    MaxDiscount DOUBLE,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    Status NVARCHAR(20) DEFAULT N'Đang diễn ra',
    CHECK (StartDate <= EndDate),
    CHECK ((Type = 'Product' AND ProductID IS NOT NULL AND DiscountPercent IS NOT NULL AND DiscountPercent > 0 AND DiscountPercent <= 100 AND MinAmount IS NULL AND MaxDiscount IS NULL) OR
           (Type = 'Price' AND ProductID IS NULL AND DiscountPercent IS NULL AND MinAmount IS NOT NULL AND MinAmount > 0 AND MaxDiscount IS NOT NULL AND MaxDiscount > 0))
);

-- Chèn dữ liệu mẫu
INSERT INTO Promotions (PromotionID, PromotionName, Type, ProductID, DiscountPercent, StartDate, EndDate, Status) VALUES
('PROM001', N'Giảm giá Laptop Gaming', 'Product', 'LAP001', 20.0, '2024-01-01', '2024-12-31', N'Đang diễn ra'),
('PROM002', N'Khuyến mãi mua sắm lớn', 'Price', NULL, NULL, 5000000.0, 500000.0, '2024-02-01', '2024-11-30', N'Đang diễn ra');