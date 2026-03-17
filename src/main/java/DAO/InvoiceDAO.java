/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import java.sql.*;
import java.util.ArrayList;
import ConnectDB.ConnectDB;

public class InvoiceDAO {
    public ArrayList<Object[]> getAll() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM Invoice";
        Connection conn = ConnectDB.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Object[] row = {
                    rs.getString("InvoiceID"),
                    rs.getString("CustomerID"),
                    rs.getString("StaffID"),
                    rs.getDate("CreateDate"),
                    rs.getDouble("TotalAmount")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(String id, String customerID, String staffID, double total, ArrayList<Object[]> details) {
        Connection conn = ConnectDB.getConnection();
        try {
            conn.setAutoCommit(false);

            String sqlInv = "INSERT INTO Invoices (InvoiceID, CustomerID, StaffID, CreatedDate, SubTotalDate, TotalAmount) VALUES (?, ?, ?, NOW(), ?, ?)";
            PreparedStatement pstInv = conn.prepareStatement(sqlInv);
            pstInv.setString(1, id);
            pstInv.setString(2, customerID);
            pstInv.setString(3, staffID);  
            pstInv.setDouble(4, total); //Subtotal
            pstInv.setDouble(5, total); //TotalAmount
            pstInv.executeUpdate();

            String sqlDet = "INSERT INTO InvoiceDetails (InvoiceID, ProductID, Quantity, UnitPrice, SubTotal) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstDet = conn.prepareStatement(sqlDet);
            
            for (Object[] row : details) {
                pstDet.setString(1, id); // InvoiceID
                pstDet.setString(2, row[0].toString()); // ProductID
                pstDet.setInt(3, Integer.parseInt(row[2].toString())); // Quantity
                pstDet.setDouble(4, Double.parseDouble(row[3].toString())); // 

                pstDet.addBatch(); // Gom lại để thực thi một lần cho nhanh
            }
            pstDet.executeBatch();

            conn.commit();
            return true;
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) {}
        }
    }

    public boolean delete(String id) {
        String sqlDel = "DELETE FROM Invoices WHERE InvoiceID = ?";
        String sqlInv = "DELETE FROM Invoicedetails WHERE InvoiceID = ?";
        Connection conn = ConnectDB.getConnection();
        try {
            PreparedStatement pstDel = conn.prepareStatement(sqlDel);
            pstDel.setString(1, id);
            pstDel.executeUpdate();

            PreparedStatement pstInv = conn.prepareStatement(sqlInv);
            pstInv.setString(1, id);
            return pstInv.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Object[]> getByCustomerID(String customerID) {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM Invoice WHERE CustomerID = ?";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
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
    
    public ArrayList<Object[]> getDetailsByInvoiceID(String invoiceID) {
        ArrayList<Object[]> list = new ArrayList<>();
        // JOIN với bảng Products để lấy Tên sản phẩm cho người dùng dễ đọc
        String sql = "FROM InvoiceDetails d " +
                     "SELECT d.ProductID, p.ProductName, d.Quantity, d.UnitPrice, (d.Quantity * d.UnitPrice) as SubTotal " +
                     "JOIN Products p ON d.ProductID = p.ProductID " +
                     "WHERE d.InvoiceID = ?";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, invoiceID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getString("ProductID"),
                    rs.getString("ProductName"),
                    rs.getInt("Quantity"),
                    rs.getDouble("UnitPriceAtSale"),
                    rs.getDouble("SubTotal")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getLastID(){
        String sql = "SELECT InvoiceID FROM Invoices ORDER BY InvoiceID DESC LIMIT 1";
        Connection conn = ConnectDB.getConnection();
        try (Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            if(rs.next()){
                return rs.getString("InvoiceID");
            }
        } catch (SQLException e) { e.printStackTrace();}
        return null;

    }
    
}

