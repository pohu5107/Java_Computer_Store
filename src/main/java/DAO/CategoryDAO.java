package DAO;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.*;
import java.util.ArrayList;
import ConnectDB.ConnectDB;

public class CategoryDAO {
    private ConnectDB connectDB = new ConnectDB();

    // 1. Lấy toàn bộ danh sách Hãng sản xuất
    public ArrayList<Object[]> getAllBrandDAO() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM Brands";
        
        try (Connection conn = connectDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("CategoryID"),
                    rs.getString("CategoryName"),
                    rs.getString("Description"),
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm một Hãng mới
    public boolean insertCategoryDAO(String id, String name, String description) {
        String sql = "INSERT INTO Brands (CategoryID, CategoryName, Description) VALUES (?, ?, ?)";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, id);
            pst.setString(2, name);
            pst.setString(3, description);
            
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Xóa một Hãng theo ID
    public boolean deleteCategoryDAO(String id) {
        String sql = "DELETE FROM Categories WHERE CategoryID = ?";
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















