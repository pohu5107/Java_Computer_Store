/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConnectDB;
import java.sql.Connection;
/**
 *
 * @author USER
 */
public class testDb {
    public static void main(String[] args) {
        ConnectDB db = new ConnectDB();
        
        System.out.println("Đang thử kết nối...");
        Connection conn = db.getConnection();
        
        if (conn != null) {
            System.out.println("=> TEST THÀNH CÔNG!");
        } else {
            System.out.println("=> TEST THẤT BẠI!");
        }
    }
}
