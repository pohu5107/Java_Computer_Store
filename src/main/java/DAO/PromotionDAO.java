/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.ConnectDB;
import java.sql.*;
import java.util.ArrayList;

public class PromotionDAO {

    public PromotionDAO() {
    }

    // 1. Lấy tất cả danh sách khuyến mãi (JOIN cả 3 bảng)
    public ArrayList<Object[]> getAll() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT pc.*, pp.ProductID, pp.DiscountPercent AS ProdDiscount, " +
                     "ipc.MinInvoiceValue, ipc.DiscountAmount, ipc.DiscountPercent AS InvDiscount, ipc.MaxDiscountValue " +
                     "FROM PromotionCampaigns pc " +
                     "LEFT JOIN ProductPromotions pp ON pc.PromotionID = pp.PromotionID " +
                     "LEFT JOIN InvoicePromotionConfigs ipc ON pc.PromotionID = ipc.PromotionID";
        
        try (Connection conn = ConnectDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("PromotionID"),        // 0
                    rs.getString("PromotionName"),      // 1
                    rs.getString("Description"),       // 2
                    rs.getTimestamp("StartDate"),       // 3
                    rs.getTimestamp("EndDate"),         // 4
                    rs.getInt("Status"),                // 5
                    rs.getString("ProductID"),          // 6 (nullable)
                    rs.getDouble("ProdDiscount"),       // 7 (nullable)
                    rs.getDouble("MinInvoiceValue"),    // 8 (nullable)
                    rs.getDouble("DiscountAmount"),     // 9 (nullable)
                    rs.getDouble("InvDiscount"),        // 10 (nullable)
                    rs.getDouble("MaxDiscountValue")    // 11 (nullable)
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm mới khuyến mãi (Transaction)
    public boolean insert(String id, String name, String startDate, String endDate, String description, String type, 
                          String productID, Double prodDiscount, Double minInvoice, Double discountAmount, 
                          Double invDiscount, Double maxDiscount) {
        
        String sqlCampaign = "INSERT INTO PromotionCampaigns (PromotionID, PromotionName, StartDate, EndDate, Status, Description) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlProduct = "INSERT INTO ProductPromotions (PromotionID, ProductID, DiscountPercent) VALUES (?, ?, ?)";
        String sqlInvoice = "INSERT INTO InvoicePromotionConfigs (PromotionID, MinInvoiceValue, DiscountAmount, DiscountPercent, MaxDiscountValue) VALUES (?, ?, ?, ?, ?)";

        Connection conn = ConnectDB.getConnection();
        try {
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Thêm vào bảng chính PromotionCampaigns
            try (PreparedStatement pst = conn.prepareStatement(sqlCampaign)) {
                pst.setString(1, id);
                pst.setString(2, name);
                pst.setString(3, startDate);
                pst.setString(4, endDate);
                pst.setInt(5, 1); // Mặc định 1 (Đang chạy)
                pst.setString(6, description);
                pst.executeUpdate();
            }

            // 2. Thêm vào bảng con tương ứng
            if ("Product".equalsIgnoreCase(type)) {
                try (PreparedStatement pstProd = conn.prepareStatement(sqlProduct)) {
                    pstProd.setString(1, id);
                    pstProd.setString(2, productID);
                    pstProd.setDouble(3, prodDiscount);
                    pstProd.executeUpdate();
                }
            } else if ("Price".equalsIgnoreCase(type)) {
                try (PreparedStatement pstInv = conn.prepareStatement(sqlInvoice)) {
                    pstInv.setString(1, id);
                    pstInv.setDouble(2, minInvoice != null ? minInvoice : 0);
                    pstInv.setDouble(3, discountAmount != null ? discountAmount : 0);
                    pstInv.setDouble(4, invDiscount != null ? invDiscount : 0);
                    if (maxDiscount != null) pstInv.setDouble(5, maxDiscount); 
                    else pstInv.setNull(5, Types.DECIMAL);
                    pstInv.executeUpdate();
                }
            }

            conn.commit(); // Thành công
            return true;
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 3. Cập nhật khuyến mãi (Transaction)
    public boolean update(String id, String name, String startDate, String endDate, String description, int status, String type, 
                          String productID, Double prodDiscount, Double minInvoice, Double discountAmount, 
                          Double invDiscount, Double maxDiscount) {
        
        String sqlCampaign = "UPDATE PromotionCampaigns SET PromotionName = ?, StartDate = ?, EndDate = ?, Status = ?, Description = ? WHERE PromotionID = ?";
        String sqlDelProduct = "DELETE FROM ProductPromotions WHERE PromotionID = ?";
        String sqlProduct = "INSERT INTO ProductPromotions (PromotionID, ProductID, DiscountPercent) VALUES (?, ?, ?)";
        String sqlDelInvoice = "DELETE FROM InvoicePromotionConfigs WHERE PromotionID = ?";
        String sqlInvoice = "INSERT INTO InvoicePromotionConfigs (PromotionID, MinInvoiceValue, DiscountAmount, DiscountPercent, MaxDiscountValue) VALUES (?, ?, ?, ?, ?)";

        Connection conn = ConnectDB.getConnection();
        try {
            conn.setAutoCommit(false);

            // 1. Cập nhật PromotionCampaigns
            try (PreparedStatement pst = conn.prepareStatement(sqlCampaign)) {
                pst.setString(1, name);
                pst.setString(2, startDate);
                pst.setString(3, endDate);
                pst.setInt(4, status);
                pst.setString(5, description);
                pst.setString(6, id);
                pst.executeUpdate();
            }

            // 2. Cập nhật chi tiết bảng con (Xóa cũ - Thêm mới để xử lý thay đổi Loại)
            try (PreparedStatement pstDelP = conn.prepareStatement(sqlDelProduct)) {
                pstDelP.setString(1, id); pstDelP.executeUpdate();
            }
            try (PreparedStatement pstDelI = conn.prepareStatement(sqlDelInvoice)) {
                pstDelI.setString(1, id); pstDelI.executeUpdate();
            }

            if ("Product".equalsIgnoreCase(type)) {
                try (PreparedStatement pstProd = conn.prepareStatement(sqlProduct)) {
                    pstProd.setString(1, id);
                    pstProd.setString(2, productID);
                    pstProd.setDouble(3, prodDiscount);
                    pstProd.executeUpdate();
                }
            } else if ("Price".equalsIgnoreCase(type)) {
                try (PreparedStatement pstInv = conn.prepareStatement(sqlInvoice)) {
                    pstInv.setString(1, id);
                    pstInv.setDouble(2, minInvoice != null ? minInvoice : 0);
                    pstInv.setDouble(3, discountAmount != null ? discountAmount : 0);
                    pstInv.setDouble(4, invDiscount != null ? invDiscount : 0);
                    if (maxDiscount != null) pstInv.setDouble(5, maxDiscount);
                    else pstInv.setNull(5, Types.DECIMAL);
                    pstInv.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 4. Xóa khuyến mãi (Tự động Cascade dựa trên schema của bạn)
    public boolean delete(String id) {
        String sql = "DELETE FROM PromotionCampaigns WHERE PromotionID = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Lấy theo ID
    public Object[] getByID(String id) {
        String sql = "SELECT pc.*, pp.ProductID, pp.DiscountPercent AS ProdDiscount, " +
                     "ipc.MinInvoiceValue, ipc.DiscountAmount, ipc.DiscountPercent AS InvDiscount, ipc.MaxDiscountValue " +
                     "FROM PromotionCampaigns pc " +
                     "LEFT JOIN ProductPromotions pp ON pc.PromotionID = pp.PromotionID " +
                     "LEFT JOIN InvoicePromotionConfigs ipc ON pc.PromotionID = ipc.PromotionID " +
                     "WHERE pc.PromotionID = ?";
        
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                        rs.getString("PromotionID"),        // 0
                        rs.getString("PromotionName"),      // 1
                        rs.getString("Description"),       // 2
                        rs.getTimestamp("StartDate"),       // 3
                        rs.getTimestamp("EndDate"),         // 4
                        rs.getInt("Status"),                // 5
                        rs.getString("ProductID"),          // 6 (nullable)
                        rs.getDouble("ProdDiscount"),       // 7 (nullable)
                        rs.getDouble("MinInvoiceValue"),    // 8 (nullable)
                        rs.getDouble("DiscountAmount"),     // 9 (nullable)
                        rs.getDouble("InvDiscount"),        // 10 (nullable)
                        rs.getDouble("MaxDiscountValue")    // 11 (nullable)
                    };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 6. Cập nhật trạng thái tự động (Tuỳ chọn)
    public void updateStatusByDate() {
        String sql = "UPDATE PromotionCampaigns SET Status = CASE " +
                     "WHEN EndDate < CURRENT_TIMESTAMP THEN 0 " +
                     "ELSE Status END";
        try (Connection conn = ConnectDB.getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

