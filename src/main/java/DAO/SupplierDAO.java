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
        String sql = "SELECT * FROM Suppliers";
        Connection conn = connectDB.getConnection();
        try (Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getString("SupplierID"),
                        rs.getString("SupplierName"),
                        rs.getString("Phone")
                };
                list.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }


    public boolean createSupplier(String id, String supplierName, String phone) {
        String sql = "INSERT INTO Suppliers (SupplierID, SupplierName, Phone) VALUES (?, ?, ?)";
        Connection conn = connectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, id);
            pst.setString(2, supplierName);
            pst.setString(3, phone);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }


    public boolean updateSupplier(String id, String supplierName, String phone) {
        String sql = "UPDATE Suppliers SET SupplierName = ?, phone = ? WHERE SupplierID = ?";
        Connection conn = connectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, supplierName);
            pst.setString(2, phone);
            pst.setString(3, id); // Điều kiện WHERE để biết sửa đúng cái nào

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean deleteSupplier(String id) {
        String sql = "DELETE FROM Suppliers WHERE SupplierID = ?";
        Connection conn = connectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
