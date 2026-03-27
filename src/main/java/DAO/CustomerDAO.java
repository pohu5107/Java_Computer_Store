/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.*;
import java.util.ArrayList;
import ConnectDB.ConnectDB;

public class CustomerDAO {

    public ArrayList<Object[]> getAll() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer";
        Connection conn = ConnectDB.getConnection();
        try (Statement st = conn.createStatement();
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

    public boolean insert(String id, String firstName, String lastName, String address, String phone) {
        String sql = "INSERT INTO Customer (CustomerID, FirstName, LastName, Address, Phone) VALUES (?, ?, ?, ?, ?)";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, id);
            pst.setString(2, firstName);
            pst.setString(3, lastName);
            pst.setString(4, address);
            pst.setString(5, phone);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM Customer WHERE CustomerID = ?";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, id);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(String id, String firstName, String lastName, String address, String phone) {
        String sql = "UPDATE Customers SET FirstName = ?, LastName = ?, Address = ?, Phone = ? WHERE CustomerID = ?";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, address);
            pst.setString(4, phone);
            pst.setString(5, id);
            
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
