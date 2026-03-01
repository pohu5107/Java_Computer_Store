/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import ConnectDB.MySQLConnectDB; // Hãy đảm bảo tên này khớp với file kết nối của bạn

public class CategoriesDAO {
    
    public DefaultTableModel getCategoriesTable() {
        // Bảng Categories có 3 cột
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Mã Danh Mục");
        model.addColumn("Tên Danh Mục");
        model.addColumn("Mô Tả");
        
        try {
            MySQLConnectDB db = new MySQLConnectDB(); 
            Connection conn = db.getConnection();
            
            if (conn != null) {
                String sql = "SELECT * FROM Categories"; 
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);
                
                while(rs.next()) {
                    // Lấy dữ liệu từ 3 trường tương ứng
                    model.addRow(new Object[] {
                        rs.getString("CategoryID"),
                        rs.getString("CategoryName"),
                        rs.getString("Description")
                    });
                }
                conn.close();
            }
        } catch (Exception e) {
            System.out.println("Lỗi truy vấn Categories: " + e.getMessage());
        }
        return model;
    }
}