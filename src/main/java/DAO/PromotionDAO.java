/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import ConnectDB.ConnectDB;
import java.sql.*;
import java.util.ArrayList;

public class PromotionDAO {

    public ArrayList<Object[]> getAll() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT PromotionID, PromotionName, DiscountPercent, StartDate, EndDate, Status FROM Promotions";
        Connection conn = ConnectDB.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("PromotionID"),
                    rs.getString("PromotionName"),
                    rs.getDouble("DiscountPercent"),
                    rs.getDate("StartDate"),
                    rs.getDate("EndDate"),
                    rs.getInt("Status")
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
        String sql = "SELECT PromotionID, PromotionName, DiscountPercent, StartDate, EndDate, Status FROM Promotions WHERE Status = 1";
        Connection conn = ConnectDB.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("PromotionID"),
                    rs.getString("PromotionName"),
                    rs.getDouble("DiscountPercent"),
                    rs.getDate("StartDate"),
                    rs.getDate("EndDate"),
                    rs.getInt("Status")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

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
            
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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
            
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM Promotions WHERE PromotionID = ?";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Object[] getByID(String id) {
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
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
