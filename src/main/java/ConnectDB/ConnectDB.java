package ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static final String hostname = "127.0.0.1";
    private static final String port = "3306";
    private static final String database = "java_computer_store";
    private static final String user = "root";
    private static final String password = "123456789"; 

    private static final String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database 
                             + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";

    private static Connection conn = null;

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, password);
                System.out.println(">>> Đã mở kết nối DUY NHẤT tới MySQL!");
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
}