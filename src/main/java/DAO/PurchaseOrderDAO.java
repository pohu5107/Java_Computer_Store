/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import ConnectDB.ConnectDB;
import java.util.ArrayList;
import java.sql.*;

public class PurchaseOrderDAO {
    
    // Đã sửa LEFT JOIN và lỗi chính tả SuppierID
    public ArrayList<Object[]> getAll(){
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT p.OrderID, p.StaffID, s.LastName AS StaffName, "+
                "p.SupplierID, sup.SupplierName, p.OrderDate, p.TotalAmount "+
                "FROM PurchaseOrders p "+
                "LEFT JOIN Staff s ON p.StaffID = s.StaffID "+
                "LEFT JOIN Suppliers sup ON p.SupplierID = sup.SupplierID "+
                "ORDER BY p.OrderDate DESC";
                
        Connection conn = ConnectDB.getConnection();
        try(PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()){
            while(rs.next()){
                Object[] row = {
                    rs.getString("OrderID"),
                    rs.getString("StaffID") + " - "+ rs.getString("StaffName"),
                    rs.getString("SupplierID") + " - "+ rs.getString("SupplierName"),
                    rs.getTimestamp("OrderDate"),
                    rs.getDouble("TotalAmount")
                };
                list.add(row);
            }
        } catch (SQLException e){
            System.out.println("Lỗi SQL tại getAll: " + e.getMessage());
            e.printStackTrace();      
        }
        return list;
    }  
    
    // Đã sửa LEFT JOIN và PruductID
    public ArrayList<Object[]> getDetailsByOrderID(String orderID){
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT d.ProductID, p.ProductName, d.Quantity, d.UnitPrice, "+
                "(d.Quantity * d.UnitPrice) AS SubTotal "+
                "FROM purchaseorderdetails d "+
                "LEFT JOIN products p ON d.ProductID = p.ProductID "+
                "WHERE d.OrderID = ?";
                
        Connection conn = ConnectDB.getConnection();
        try(PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1, orderID);
            try(ResultSet rs = pst.executeQuery()){
                while(rs.next()){
                    Object[] row ={
                        rs.getString("ProductID"),
                        rs.getString("ProductName"),
                        rs.getString("Quantity"),
                        rs.getDouble("UnitPrice"),
                        rs.getDouble("SubTotal")
                    }; 
                    list.add(row);
                }
            }   
        } catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    
    // Đã sửa purchaseorders, LIMIT 1 và OrderID
    public String getLastID(){
        String sql = "SELECT OrderID FROM purchaseorders ORDER BY OrderID DESC LIMIT 1";
        Connection conn = ConnectDB.getConnection();
        try(PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()){
                if(rs.next()) return rs.getString("OrderID");
        } catch (SQLException e){
            System.out.println("Lỗi SQL tại getLastID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
      
    
    // 4. Lưu Phiếu + Lưu Chi Tiết + Tồn Kho 
    public boolean insert(String orderId, String staffId, String supplierId, double totalAmount, ArrayList<Object[]> details) {
        String sqlOrder = "INSERT INTO PurchaseOrders (OrderID, StaffID, SupplierID, TotalAmount) VALUES (?, ?, ?, ?)";
        String sqlDetail = "INSERT INTO PurchaseOrderDetails (OrderID, ProductID, Quantity, UnitPrice) VALUES (?, ?, ?, ?)";
        String sqlUpdateProduct = "UPDATE Products SET Quantity = Quantity + ? WHERE ProductID = ?";

        Connection conn = ConnectDB.getConnection();
        if(conn == null) { return false; }
        try {
            conn.setAutoCommit(false); 
            // 1. Lưu thông tin chung của Phiếu Nhập
            try (PreparedStatement pstOrder = conn.prepareStatement(sqlOrder)) {
                pstOrder.setString(1, orderId);
                pstOrder.setString(2, staffId);
                pstOrder.setString(3, supplierId);
                pstOrder.setDouble(4, totalAmount);
                pstOrder.executeUpdate();
            }
            // 2+3  Lưu từng dòng chi tiết và cộng vào Kho
            try (PreparedStatement pstDetail = conn.prepareStatement(sqlDetail);
                 PreparedStatement pstUpdate = conn.prepareStatement(sqlUpdateProduct)) {
                for (Object[] row : details) {
                    String productId = row[0].toString();
                    int qty = Integer.parseInt(row[1].toString());
                    double price = Double.parseDouble(row[2].toString());
                    // Thêm vào bảng Detail
                    pstDetail.setString(1, orderId);
                    pstDetail.setString(2, productId);
                    pstDetail.setInt(3, qty);
                    pstDetail.setDouble(4, price);
                    pstDetail.executeUpdate();
                    // Cộng số lượng vào bảng Products
                    pstUpdate.setInt(1, qty);
                    pstUpdate.setString(2, productId);
                    pstUpdate.executeUpdate();
                }
            }
            // Xác nhận lưu toàn bộ
            conn.commit(); 
            return true;
        } catch (SQLException e) {
            e.printStackTrace(); 
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try {   conn.setAutoCommit(true);
                    conn.close();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
    
}
