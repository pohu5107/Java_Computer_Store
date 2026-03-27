package DAO;

import ConnectDB.ConnectDB;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class PromotionDAO {

    // 1. Lấy tất cả danh sách khuyến mãi
    public ArrayList<Object[]> getAll() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM promotioncampaigns"; 
        Connection conn = ConnectDB.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("PromotionID"),
                    rs.getString("PromotionName"),
                    rs.getString("Type"),
                    rs.getString("ProductID"),
                    rs.getDouble("DiscountPercent"),
                    rs.getDouble("MinAmount"),
                    rs.getDouble("MaxDiscount"),
                    rs.getDate("StartDate"),
                    rs.getDate("EndDate"),
                    rs.getString("Status")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Lấy các khuyến mãi đang hoạt động
    public ArrayList<Object[]> getActive() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM promotioncampaigns WHERE Status = N'Đang diễn ra'";
        
        try (Connection conn = ConnectDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("PromotionID"),
                    rs.getString("PromotionName"),
                    rs.getString("Type"),
                    rs.getString("ProductID"),
                    rs.getDouble("DiscountPercent"),
                    rs.getDouble("MinAmount"),
                    rs.getDouble("MaxDiscount"),
                    rs.getDate("StartDate"),
                    rs.getDate("EndDate"),
                    rs.getString("Status")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Thêm mới khuyến mãi
    public boolean insert(String id, String name, String type, String productID, Double discountPercent, Double minAmount, Double maxDiscount, Date startDate, Date endDate) {
        String sql = "INSERT INTO promotioncampaigns (PromotionID, PromotionName, Type, ProductID, DiscountPercent, MinAmount, MaxDiscount, StartDate, EndDate, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
             
            pst.setString(1, id);
            pst.setString(2, name);
            pst.setString(3, type);
            
            if (productID != null && !productID.isEmpty()) pst.setString(4, productID); 
            else pst.setNull(4, Types.VARCHAR);
            
            if (discountPercent != null) pst.setDouble(5, discountPercent); 
            else pst.setNull(5, Types.DOUBLE);
            
            if (minAmount != null) pst.setDouble(6, minAmount); 
            else pst.setNull(6, Types.DOUBLE);
            
            if (maxDiscount != null) pst.setDouble(7, maxDiscount); 
            else pst.setNull(7, Types.DOUBLE);
            
            pst.setDate(8, startDate);
            pst.setDate(9, endDate);
            
            // Tự động xét trạng thái dựa trên ngày hiện tại và ngày kết thúc
            String status = LocalDate.now().isAfter(endDate.toLocalDate()) ? "Kết thúc" : "Đang diễn ra";
            pst.setString(10, status);
            
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Cập nhật khuyến mãi
    public boolean update(String id, String name, String type, String productID, Double discountPercent, Double minAmount, Double maxDiscount, Date startDate, Date endDate) {
        String sql = "UPDATE promotioncampaigns SET PromotionName = ?, Type = ?, ProductID = ?, DiscountPercent = ?, MinAmount = ?, MaxDiscount = ?, StartDate = ?, EndDate = ?, Status = ? WHERE PromotionID = ?";
        
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
             
            pst.setString(1, name);
            pst.setString(2, type);
            
            if (productID != null && !productID.isEmpty()) pst.setString(3, productID); 
            else pst.setNull(3, Types.VARCHAR);
            
            if (discountPercent != null) pst.setDouble(4, discountPercent); 
            else pst.setNull(4, Types.DOUBLE);
            
            if (minAmount != null) pst.setDouble(5, minAmount); 
            else pst.setNull(5, Types.DOUBLE);
            
            if (maxDiscount != null) pst.setDouble(6, maxDiscount); 
            else pst.setNull(6, Types.DOUBLE);
            
            pst.setDate(7, startDate);
            pst.setDate(8, endDate);
            
            String status = LocalDate.now().isAfter(endDate.toLocalDate()) ? "Kết thúc" : "Đang diễn ra";
            pst.setString(9, status);
            pst.setString(10, id);
            
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Xóa khuyến mãi
    public boolean delete(String id) {
        String sql = "DELETE FROM Promotions WHERE PromotionID = ?";
        
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
             
            pst.setString(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 6. Lấy 1 khuyến mãi theo ID
    public Object[] getByID(String id) {
        String sql = "SELECT * FROM promotioncampaigns WHERE PromotionID = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                        rs.getString("PromotionID"),
                        rs.getString("PromotionName"),
                        rs.getString("Type"),
                        rs.getString("ProductID"),
                        rs.getDouble("DiscountPercent"),
                        rs.getDouble("MinAmount"),
                        rs.getDouble("MaxDiscount"),
                        rs.getDate("StartDate"),
                        rs.getDate("EndDate"),
                        rs.getString("Status")
                    };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật lại toàn bộ trạng thái (Dùng khi khởi động App để update hạn khuyến mãi)
    public void updateStatus() {
        String sql = "UPDATE promotioncampaigns SET Status = CASE WHEN EndDate < CURDATE() THEN N'Kết thúc' ELSE N'Đang diễn ra' END";
        try (Connection conn = ConnectDB.getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}