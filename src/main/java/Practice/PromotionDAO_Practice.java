package Practice;

import ConnectDB.ConnectDB;
import java.sql.*;
import java.util.ArrayList;

/**
 * 🎓 FILE LUYỆN TẬP DAO: PromotionDAO_Practice.java
 * -------------------------------------------------------------------------
 * HƯỚNG DẪN:
 * 1. Tập trung nhớ kỹ bộ khung: Connection -> PreparedStatement -> Execute.
 * 2. Để ý cách dùng try-with-resources (ngoặc tròn sau try) để tự động đóng kết nối.
 * -------------------------------------------------------------------------
 */
public class PromotionDAO_Practice {
    // 1. Khai báo câu lệnh SELECT_ALL dùng chung cho cả lớp
    

    // 2. Hàm getAll: Mở Connection -> Tạo Statement -> Chạy Query -> Duyệt ResultSet
    public ArrayList<Object[]> getAll() {
        // Gõ code tại đây:
        return null;
    }


    // 3. Hàm mapRsToRow: Chuyển 1 dòng ResultSet sang mảng Object[] (12 cột)
    private Object[] mapRsToRow(ResultSet rs) throws SQLException {
        // Gõ code tại đây:
        return null;
    }


    // 4. Hàm insert: Dùng Transaction (Giao dịch)
    // Bước 1: setAutoCommit(false)
    // Bước 2: Chèn bảng chính
    // Bước 3: Chèn bảng phụ (Product hoặc Price)
    // Bước 4: commit()
    public boolean insert(String id, String name, String start, String end, String desc, String type, 
                          String pid, Double pDsc, Double mInv, Double dAmt) {
        // Gõ code tại đây:
        return false;
    }


    // 5. Hàm update: Tương tự insert nhưng có bước DELETE bảng phụ cũ trước khi chèn mới
    public boolean update(String id, String name, String start, String end, String desc, int status, String type, 
                          String pid, Double pDsc, Double mInv, Double dAmt) {
        // Gõ code tại đây:
        return false;
    }


    // 6. Hàm delete: Chỉ cần xóa ở bảng chính (vì có ON DELETE CASCADE)
    public boolean delete(String id) {
        // Gõ code tại đây:
        return false;
    }


    // 7. Hàm getByID: Truy vấn kết hợp WHERE PromotionID = ?
    public Object[] getByID(String id) {
        // Gõ code tại đây:
        return null;
    }


    // 8. Hàm updateStatusByDate: Chạy lệnh UPDATE SQL trực tiếp
    public void updateStatusByDate() {
        // Gõ code tại đây:
    }
}
