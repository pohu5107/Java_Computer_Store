/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.*;
import java.util.ArrayList;

import ConnectDB.ConnectDB;

public class StaffDAO {
    private ConnectDB connectDB = new ConnectDB();

    // Lấy toàn bộ dữ liệu
    public ArrayList<Object[]> getAllStaffDAO() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM Staff";
        Connection conn = connectDB.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getString("StaffID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Gender"),
                        rs.getDate("Date"),
                        rs.getString("Phone")
                };
                list.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    public boolean createStaff(String id, String firstName, String lastName, String gender, Date date, String phone) {
        String sql = "INSERT INTO Staff (StaffID, FirstName, LastName, Gender, Date, Phone) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = connectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, id);
            pst.setString(2, firstName);
            pst.setString(3, lastName);
            pst.setString(4, gender);
            pst.setDate(5, date);
            pst.setString(6, phone);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean updateStaff(String id, String firstName, String lastName, String gender, Date date, String phone) {

        String sql = "UPDATE Staff SET FirstName = ?, LastName = ?, gender = ?, date = ?, phone = ? WHERE StaffID = ?";
        Connection conn = connectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, gender);
            pst.setDate(4, date);
            pst.setString(5, phone);
            pst.setString(6, id); // Điều kiện WHERE để biết sửa đúng cái nào

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStaff(String id) {
        String sql = "DELETE FROM Staff WHERE StaffID = ?";
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
