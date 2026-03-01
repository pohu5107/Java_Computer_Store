/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import ConnectDB.MySQLConnectDB; // Import lớp kết nối của bạn

public class BrandsDAO {
    
    public DefaultTableModel getBrandsTable() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Mã Thương Hiệu");
        model.addColumn("Tên Thương Hiệu");
        model.addColumn("Địa Chỉ");
        model.addColumn("Số Điện Thoại");
        try {
            // Sử dụng lớp kết nối bạn đã viết
            MySQLConnectDB db = new MySQLConnectDB(); 
            Connection conn = db.getConnection();
            
            if (conn != null) {
                String sql = "SELECT * FROM Brands"; 
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);
                
                while(rs.next()) {
                    model.addRow(new Object[] {
                        rs.getString("BrandID"),
                        rs.getString("BrandName"),
                        rs.getString("Address"),
                        rs.getString("Phone")
                    });
                }
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("Lỗi truy vấn: " + e.getMessage());
        }
        return model;
    }
}