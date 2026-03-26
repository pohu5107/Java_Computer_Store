/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.ConnectDB;
<<<<<<< Updated upstream
import java.sql.*;
import java.util.ArrayList;
=======
import java.util.ArrayList;
import java.sql.*;
import java.time.LocalDate;
>>>>>>> Stashed changes

public class PromotionDAO {

    public ArrayList<Object[]> getAll() {
        ArrayList<Object[]> list = new ArrayList<>();
<<<<<<< Updated upstream
        String sql = "SELECT PromotionID, PromotionName, DiscountPercent, StartDate, EndDate, Status FROM Promotions";
        Connection conn = ConnectDB.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
=======
        String sql = "SELECT PromotionID, PromotionName, StartDate, EndDate, Status FROM Promotions";
        Connection conn = ConnectDB.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
>>>>>>> Stashed changes
            while (rs.next()) {
                Object[] row = {
                    rs.getString("PromotionID"),
                    rs.getString("PromotionName"),
<<<<<<< Updated upstream
                    rs.getDouble("DiscountPercent"),
                    rs.getDate("StartDate"),
                    rs.getDate("EndDate"),
                    rs.getInt("Status")
=======
                    rs.getDate("StartDate"),
                    rs.getDate("EndDate"),
                    rs.getString("Status")
>>>>>>> Stashed changes
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Object[]> getActive() {
        ArrayList<Object[]> list = new ArrayList<>();
<<<<<<< Updated upstream
        String sql = "SELECT PromotionID, PromotionName, DiscountPercent, StartDate, EndDate, Status FROM Promotions WHERE Status = 1";
        Connection conn = ConnectDB.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
=======
        String sql = "SELECT PromotionID, PromotionName, StartDate, EndDate, Status FROM Promotions WHERE Status = N'Đang diễn ra'";
        Connection conn = ConnectDB.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
>>>>>>> Stashed changes
            while (rs.next()) {
                Object[] row = {
                    rs.getString("PromotionID"),
                    rs.getString("PromotionName"),
<<<<<<< Updated upstream
                    rs.getDouble("DiscountPercent"),
                    rs.getDate("StartDate"),
                    rs.getDate("EndDate"),
                    rs.getInt("Status")
=======
                    rs.getDate("StartDate"),
                    rs.getDate("EndDate"),
                    rs.getString("Status")
>>>>>>> Stashed changes
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

<<<<<<< Updated upstream
    public boolean insert(String id, String name, double discountPercent, Date startDate, Date endDate) {
        String sql = "INSERT INTO Promotions (PromotionID, PromotionName, DiscountPercent, StartDate, EndDate, Status) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, id);
            pst.setString(2, name);
            pst.setDouble(3, discountPercent);
            pst.setDate(4, startDate);
            pst.setDate(5, endDate);
            pst.setInt(6, 1); // Default status = 1 (Active)
            
=======
    public boolean insert(String id, String name, String type, String productID, Double discountPercent, Double minAmount, Double maxDiscount, Date startDate, Date endDate) {
        Connection conn = ConnectDB.getConnection();
        String sql = "INSERT INTO Promotions (PromotionID, PromotionName, Type, ProductID, DiscountPercent, MinAmount, MaxDiscount, StartDate, EndDate, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, id);
            pst.setString(2, name);
            pst.setString(3, type);
            if (productID != null) pst.setString(4, productID); else pst.setNull(4, Types.VARCHAR);
            if (discountPercent != null) pst.setDouble(5, discountPercent); else pst.setNull(5, Types.DOUBLE);
            if (minAmount != null) pst.setDouble(6, minAmount); else pst.setNull(6, Types.DOUBLE);
            if (maxDiscount != null) pst.setDouble(7, maxDiscount); else pst.setNull(7, Types.DOUBLE);
            pst.setDate(8, startDate);
            pst.setDate(9, endDate);
            String status = LocalDate.now().isAfter(endDate.toLocalDate()) ? "Kết thúc" : "Đang diễn ra";
            pst.setString(10, status);
>>>>>>> Stashed changes
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

<<<<<<< Updated upstream
    public boolean update(String id, String name, double discountPercent, Date startDate, Date endDate, int status) {
        String sql = "UPDATE Promotions SET PromotionName = ?, DiscountPercent = ?, StartDate = ?, EndDate = ?, Status = ? WHERE PromotionID = ?";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, name);
            pst.setDouble(2, discountPercent);
            pst.setDate(3, startDate);
            pst.setDate(4, endDate);
            pst.setInt(5, status);
            pst.setString(6, id);
            
=======
    public boolean update(String id, String name, String type, String productID, Double discountPercent, Double minAmount, Double maxDiscount, Date startDate, Date endDate) {
        Connection conn = ConnectDB.getConnection();
        String sql = "UPDATE Promotions SET PromotionName = ?, Type = ?, ProductID = ?, DiscountPercent = ?, MinAmount = ?, MaxDiscount = ?, StartDate = ?, EndDate = ?, Status = ? WHERE PromotionID = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setString(2, type);
            if (productID != null) pst.setString(3, productID); else pst.setNull(3, Types.VARCHAR);
            if (discountPercent != null) pst.setDouble(4, discountPercent); else pst.setNull(4, Types.DOUBLE);
            if (minAmount != null) pst.setDouble(5, minAmount); else pst.setNull(5, Types.DOUBLE);
            if (maxDiscount != null) pst.setDouble(6, maxDiscount); else pst.setNull(6, Types.DOUBLE);
            pst.setDate(7, startDate);
            pst.setDate(8, endDate);
            String status = LocalDate.now().isAfter(endDate.toLocalDate()) ? "Kết thúc" : "Đang diễn ra";
            pst.setString(9, status);
            pst.setString(10, id);
>>>>>>> Stashed changes
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
<<<<<<< Updated upstream
        String sql = "DELETE FROM Promotions WHERE PromotionID = ?";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            
=======
        Connection conn = ConnectDB.getConnection();
        String sql = "DELETE FROM Promotions WHERE PromotionID = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
>>>>>>> Stashed changes
            pst.setString(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Object[] getByID(String id) {
<<<<<<< Updated upstream
        String sql = "SELECT PromotionID, PromotionName, DiscountPercent, StartDate, EndDate, Status FROM Promotions WHERE PromotionID = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Object[] {
                    rs.getString("PromotionID"),
                    rs.getString("PromotionName"),
                    rs.getDouble("DiscountPercent"),
                    rs.getDate("StartDate"),
                    rs.getDate("EndDate"),
                    rs.getInt("Status")
=======
        String sql = "SELECT * FROM Promotions WHERE PromotionID = ?";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
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
>>>>>>> Stashed changes
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
<<<<<<< Updated upstream
=======

    public void updateStatus() {
        String sql = "UPDATE Promotions SET Status = CASE WHEN EndDate < CURDATE() THEN N'Kết thúc' ELSE N'Đang diễn ra' END";
        Connection conn = ConnectDB.getConnection();
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
>>>>>>> Stashed changes
}
