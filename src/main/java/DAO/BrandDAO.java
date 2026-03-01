/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import java.sql.*;
import java.util.ArrayList;
import ConnectDB.ConnectDB;
/**
 *
 * @author Phu
 */
public class BrandDAO {
    private ConnectDB connectDB = new ConnectDB();

    // 1. Lấy toàn bộ danh sách Hãng sản xuất
    public ArrayList<Object[]> getAll() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM Brands";
        
        try (Connection conn = connectDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("BrandID"),
                    rs.getString("BrandName"),
                    rs.getString("Address"),
                    rs.getString("Phone")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm một Hãng mới
    public boolean insert(String id, String name, String address, String phone) {
        String sql = "INSERT INTO Brands (BrandID, BrandName, Address, Phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, id);
            pst.setString(2, name);
            pst.setString(3, address);
            pst.setString(4, phone);
            
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Xóa một Hãng theo ID
    public boolean delete(String id) {
        String sql = "DELETE FROM Brands WHERE BrandID = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
