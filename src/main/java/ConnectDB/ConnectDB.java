package ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
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
            // Kiểm tra: Nếu chưa có kết nối HOẶC kết nối đã bị đóng thì mới tạo mới
            if (conn == null || conn.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, password);
                System.out.println(">>> Đã mở kết nối mới tới MySQL!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("LỖI: Không tìm thấy Driver!");
        } catch (SQLException e) {
            System.err.println("LỖI SQL: " + e.getMessage());
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
        ConnectDB mysql = new ConnectDB();
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

