package ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnectDB {
    // Cấu hình thông tin kết nối (Thay đổi nếu bạn dùng port khác hoặc có mật khẩu)
    private final String hostname = "localhost";
    private final String port = "3306";
    private final String database = "java_computer_store";
    private final String user = "root";
    private final String password = "root"; // XAMPP mặc định để trống

    private final String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database 
                             + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";

    private Connection conn = null;

    public Connection getConnection() {
        try {
            // 1. Nạp Driver (Bắt buộc với dự án Maven/JDBC)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 2. Thiết lập kết nối
            conn = DriverManager.getConnection(url, user, password);
            
        } catch (ClassNotFoundException e) {
            System.err.println("LỖI: Không tìm thấy Driver MySQL. Kiểm tra lại Dependencies trong pom.xml!");
        } catch (SQLException e) {
            System.err.println("LỖI SQL: " + e.getMessage());
            System.err.println("GỢI Ý: Kiểm tra XAMPP đã bật MySQL chưa, hoặc tên Database '" + database + "' có đúng không.");
        }
        return conn;
    }

    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Đã đóng kết nối Database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- HÀM CHẠY THỬ NHANH ---
    public static void main(String[] args) {
        MySQLConnectDB mysql = new MySQLConnectDB();
        Connection testConn = mysql.getConnection();
        
        if (testConn != null) {
            System.out.println("==========================================");
            System.out.println(" Connected to: java_computer_store");
            System.out.println("==========================================");
            
            // Thử đóng kết nối sau khi test 
            mysql.closeConnection();
        } else {
            System.out.println(">>> KẾT NỐI THẤT BẠI!");
        }
    }
}

