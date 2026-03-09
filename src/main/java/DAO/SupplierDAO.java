/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.*;
import java.util.ArrayList;

import ConnectDB.ConnectDB;

public class SupplierDAO {
    private ConnectDB connectDB = new ConnectDB();

    // Lấy toàn bộ dữ liệu
    public ArrayList<Object[]> getAllSupplierDAO() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM Supplier";

        try (Connection conn = connectDB.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getString("SupplierID"),
                        rs.getString("SupplierName"),
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

    public boolean createSupplier(String id, String supplierName, String address, String phone) {
        String sql = "INSERT INTO Supplier (SupplierID, SupplierName, Address, Phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectDB.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, id);
            pst.setString(2, supplierName);
            pst.setString(3, address);
            pst.setString(4, phone);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean updateSupplier(String id, String supplierName, String address, String phone) {
        String sql = "UPDATE Supplier SET SupplierName = ?, address = ?, phone = ? WHERE SupplierID = ?";
        try (Connection conn = connectDB.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, supplierName);
            pst.setString(2, address);
            pst.setString(3, phone);
            pst.setString(6, id); // Điều kiện WHERE để biết sửa đúng cái nào

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSupplier(String id) {
        String sql = "DELETE FROM Supplier WHERE SupplierID = ?";
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
