/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.*;
import java.util.ArrayList;
import ConnectDB.ConnectDB;

public class InvoicebDAO {
    private ConnectDB connectDB = new ConnectDB();

    // 1. Lấy toàn bộ hóa đơn
    public ArrayList<Object[]> getAll() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM Invoice";

        try (Connection conn = connectDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                    rs.getString("InvoiceID"),
                    rs.getString("CustomerID"),
                    rs.getDate("InvoiceDate"),
                    rs.getDouble("TotalAmount")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm hóa đơn
    public boolean insert(String id, String customerID, Date date, double total) {
        String sql = "INSERT INTO Invoice (InvoiceID, CustomerID, InvoiceDate, TotalAmount) VALUES (?, ?, ?, ?)";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, id);
            pst.setString(2, customerID);
            pst.setDate(3, date);
            pst.setDouble(4, total);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Xóa hóa đơn
    public boolean delete(String id) {
        String sql = "DELETE FROM Invoice WHERE InvoiceID = ?";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, id);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. (Thêm nghiệp vụ) Lấy hóa đơn theo CustomerID
    public ArrayList<Object[]> getByCustomerID(String customerID) {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM Invoice WHERE CustomerID = ?";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, customerID);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("InvoiceID"),
                    rs.getString("CustomerID"),
                    rs.getDate("InvoiceDate"),
                    rs.getDouble("TotalAmount")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}