
package DAO;

import java.sql.*;
import java.util.ArrayList;

import ConnectDB.ConnectDB;

public class SupplierDAO {

    public ArrayList<Object[]> getAllStaffDAO() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM Supplier";
        Connection conn = ConnectDB.getConnection();  
        try (Statement st = conn.createStatement();
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

    public boolean createStaff(String id, String supplierName, String address, String phone) {
        String sql = "INSERT INTO Supplier (SupplierID, SupplierName, Address, Phone) VALUES (?, ?, ?, ?)";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
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

    public boolean update(String id, String supplierName, String address, String phone) {
        String sql = "UPDATE Supplier SET SupplierName = ?, address = ?, phone = ? WHERE SupplierID = ?";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {

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

    public boolean deleteStaff(String id) {
        String sql = "DELETE FROM Supplier WHERE SupplierID = ?";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}