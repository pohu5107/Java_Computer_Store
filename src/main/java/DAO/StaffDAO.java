/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.*;
import java.util.ArrayList;
import ConnectDB.ConnectDB;

public class StaffDAO {
    public ArrayList<Object[]> getAll() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM staff";

        try (Connection conn = ConnectDB.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Object[] row = {
                        rs.getString("StaffID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Gender"),
                        rs.getDate("BirthDate"),
                        rs.getString("Phone")
                };
                list.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    public boolean insert(String id, String firstName, String lastName, String gender, Date birthDate, String phone) {
        String sql = "INSERT INTO Staff (StaffID, FirstName, LastName, Gender, BirthDate, Phone) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, id);
            pst.setString(2, firstName);
            pst.setString(3, lastName);
            pst.setString(4, gender);
            pst.setDate(5, birthDate);
            pst.setString(6, phone);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean update(String id, String firstName, String lastName, String gender, Date birthDate, String phone) {
        String sql = "UPDATE Staff SET FirstName = ?, LastName = ?, Gender = ?, BirthDate = ?, Phone = ? WHERE StaffID = ?";
        try (Connection conn = ConnectDB.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, gender);
            pst.setDate(4, birthDate);
            pst.setString(5, phone);
            pst.setString(6, id); // Điều kiện WHERE để biết sửa đúng cái nào

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM Staff WHERE StaffID = ?";
        try (Connection conn = ConnectDB.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }        
    }

       
    
}