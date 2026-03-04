/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import ConnectDB.ConnectDB;
import java.sql.*;
import java.util.ArrayList;

public class StatisticDAO {

    // 1. Tổng Chi (Lấy từ bảng PurchaseOrders)
    public double getTotalExpenditure(String fromDate, String toDate) {
        double total = 0;
        // Chú ý: Cột ngày là OrderDate
        String sql = "SELECT SUM(TotalAmount) FROM PurchaseOrders WHERE OrderDate BETWEEN ? AND ?";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, fromDate);
            pst.setString(2, toDate);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) total = rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return total;
    }

    // 2. Tổng Thu (Lấy từ bảng Invoices)
    public double getTotalRevenue(String fromDate, String toDate) {
        double total = 0;
        // Cột ngày là CreatedDate
        String sql = "SELECT SUM(TotalAmount) FROM Invoices WHERE CreatedDate BETWEEN ? AND ?";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, fromDate);
            pst.setString(2, toDate);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) total = rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return total;
    }

    // Lợi nhuận = (Số lượng bán * Giá bán) - (Số lượng bán * Giá nhập trung bình)
    public ArrayList<Object[]> getProfitReport(String fromDate, String toDate) {
        ArrayList<Object[]> list = new ArrayList<>();
        // Câu SQL này lấy doanh thu từng món và trừ đi vốn nhập tương ứng
        String sql = "SELECT p.ProductID, p.ProductName, " +
                     "SUM(id.Quantity) as QtySold, " +
                     "SUM(id.SubTotal) as Revenue, " +
                     "SUM(id.Quantity * (SELECT AVG(UnitPrice) FROM PurchaseOrderDetails WHERE ProductID = p.ProductID)) as EstCost " +
                     "FROM InvoiceDetails id " +
                     "JOIN Products p ON id.ProductID = p.ProductID " +
                     "JOIN Invoices i ON id.InvoiceID = i.InvoiceID " +
                     "WHERE i.CreatedDate BETWEEN ? AND ? " +
                     "GROUP BY p.ProductID, p.ProductName";
        
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, fromDate);
            pst.setString(2, toDate);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                double rev = rs.getDouble("Revenue");
                double cost = rs.getDouble("EstCost");
                double profit = rev - cost;
                list.add(new Object[]{
                    rs.getString("ProductID"),
                    rs.getString("ProductName"),
                    rs.getInt("QtySold"),
                    rev, // Doanh thu
                    profit // Lợi nhuận
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}