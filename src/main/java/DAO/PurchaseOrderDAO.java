/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import ConnectDB.ConnectDB;
import java.util.ArrayList;
import java.sql.*;

public class PurchaseOrderDAO {
    public ArrayList<Object[]> getAll(){
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT p.OrderID, p.StaffID, s.LastName AS StaffName, "+
                "p.SuppierID, sup.SupplierName, p.OrderDate, p.TotalAmount "+
                "FROM purchaseorsers p "+
                "JOIN staff s ON p.StaffID = s.StaffID "+
                "JOIN Suppliers up ON p.SupplierID = sup.SupplierID "+
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
            e.printStackTrace();      
        }
        return list;
    }  
    
    public ArrayList<Object[]> getDetailsByOrderID(String orderID){
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT d.ProductID,p.ProductName, d.Quantity, d.UnitPrice, "+
                "(d.Quantity * d.UnitPrice) AS SubTotal "+
                "FROM purchaseorderdetails d "+
                "JOIN products p ON d.ProductID = p.PruductID "+
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
    
    public String getLastID(){
        String sql = "SELECT OrderID FROM purchaseoders ORDER BY OrderID DESC LIMTI 1";
        Connection conn = ConnectDB.getConnection();
        try(PreparedStatement pst = conn.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()){
                if(rs.next()) return rs.getString("Order");
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    // them phieu nhap moi (giaodich: phiếu + chi tiết + cộng kho)
    public boolean insert(String orderID, `````````````````````)
    
    

//
//    // 4. Thêm Phiếu Nhập Mới (Transaction: Phiếu + Chi tiết + Cộng Kho)
//    public boolean insert(String orderId, String staffId, String supplierId, double totalAmount, ArrayList<Object[]> details) {
//        String sqlOrder = "INSERT INTO PurchaseOrders (OrderID, StaffID, SupplierID, TotalAmount) VALUES (?, ?, ?, ?)";
//        String sqlDetail = "INSERT INTO PurchaseOrderDetails (OrderID, ProductID, Quantity, UnitPrice) VALUES (?, ?, ?, ?)";
//        String sqlUpdateProduct = "UPDATE Products SET Quantity = Quantity + ? WHERE ProductID = ?";
//
//        Connection conn = null;
//        try {
//            conn = connectDB.getConnection();
//            conn.setAutoCommit(false); // Bật chế độ Transaction
//
//            // Bước 1: Lưu hóa đơn tổng
//            try (PreparedStatement pstOrder = conn.prepareStatement(sqlOrder)) {
//                pstOrder.setString(1, orderId);
//                pstOrder.setString(2, staffId);
//                pstOrder.setString(3, supplierId);
//                pstOrder.setDouble(4, totalAmount);
//                pstOrder.executeUpdate();
//            }
//
//            // Bước 2: Lưu chi tiết & Bước 3: Cộng số lượng vào kho
//            try (PreparedStatement pstDetail = conn.prepareStatement(sqlDetail);
//                 PreparedStatement pstUpdate = conn.prepareStatement(sqlUpdateProduct)) {
//                
//                for (Object[] row : details) {
//                    String productId = row[0].toString();
//                    int qty = Integer.parseInt(row[1].toString());
//                    double price = Double.parseDouble(row[2].toString());
//
//                    // Thêm Chi tiết
//                    pstDetail.setString(1, orderId);
//                    pstDetail.setString(2, productId);
//                    pstDetail.setInt(3, qty);
//                    pstDetail.setDouble(4, price);
//                    pstDetail.executeUpdate();
//
//                    // Cộng Kho
//                    pstUpdate.setInt(1, qty);
//                    pstUpdate.setString(2, productId);
//                    pstUpdate.executeUpdate();
//                }
//            }
//
//            conn.commit(); // Xác nhận nếu mọi thứ đều suôn sẻ
//            return true;
//
//        } catch (SQLException e) {
//            try {
//                if (conn != null) conn.rollback(); // Hủy bỏ toàn bộ nếu có lỗi
//            } catch (SQLException ex) { ex.printStackTrace(); }
//            e.printStackTrace();
//            return false;
//        } finally {
//            try {
//                if (conn != null) {
//                    conn.setAutoCommit(true);
//                    conn.close();
//                }
//            } catch (SQLException e) { e.printStackTrace(); }
//        }
//    }
//}
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
    
    
    
    
    
}
