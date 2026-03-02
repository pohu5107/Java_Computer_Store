/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.*;
import java.util.ArrayList;
import ConnectDB.ConnectDB;

public class CustomerDAO {
    private ConnectDB connectDB = new ConnectDB();

    // 1. Lấy toàn bộ khách hàng
    public ArrayList<Object[]> getAll() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer";

        try (Connection conn = connectDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                    rs.getString("CustomerID"),
                    rs.getString("CustomerName"),
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

    // 2. Thêm khách hàng
    public boolean insert(String id, String name, String address, String phone) {
        String sql = "INSERT INTO Customer (CustomerID, CustomerName, Address, Phone) VALUES (?, ?, ?, ?)";

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

    // 3. Xóa khách hàng theo ID
    public boolean delete(String id) {
        String sql = "DELETE FROM Customer WHERE CustomerID = ?";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, id);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. (Thêm nghiệp vụ) Tìm theo tên
    public ArrayList<Object[]> searchByName(String keyword) {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer WHERE CustomerName LIKE ?";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("CustomerID"),
                    rs.getString("CustomerName"),
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
}
