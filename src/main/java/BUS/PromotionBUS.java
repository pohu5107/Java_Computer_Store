package BUS;

import DAO.PromotionDAO;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

/**
 * Lớp BUS: Quản lý logic Khuyến mãi và RAM Cache.
 */
public class PromotionBUS {
    private final PromotionDAO dao = new PromotionDAO();
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ArrayList<Object[]> list; // Danh sách RAM Cache

    public PromotionBUS() { refreshData(); }

    // Làm mới dữ liệu RAM từ DB
    public void refreshData() { dao.updateStatusByDate(); list = dao.getAll(); }

    // Lấy toàn bộ danh sách từ RAM
    public ArrayList<Object[]> getAll() {
        if (list == null) refreshData();
        return list;
    }

    // Kiểm tra trùng mã ID trên RAM
    public boolean isDuplicate(String id) {
        for (Object[] row : getAll()) if (row[0].toString().equalsIgnoreCase(id)) return true;
        return false;
    }

    // Thêm mới: Kiểm tra logic -> Ghi DB -> Refresh RAM
    public String add(String id, String nm, java.util.Date s, java.util.Date e, String de, String t, 
                      String pid, Double pDs, Double mIv, Double dAm) {
        if (id == null || id.trim().isEmpty() || nm == null || nm.trim().isEmpty()) return "Mã và Tên không trống";
        if (isDuplicate(id)) return "Mã đã tồn tại";
        if (s == null || e == null || s.after(e)) return "Ngày không hợp lệ";
        if ("Product".equals(t) && (pid == null || pid.isEmpty() || pDs <= 0)) return "Dữ liệu SP lỗi";
        
        if (dao.insert(id, nm, df.format(s), df.format(e), de, t, pid, pDs, mIv, dAm)) {
            refreshData(); return "Thêm thành công";
        }
        return "Thêm thất bại";
    }

    // Cập nhật: Kiểm tra logic -> Ghi DB -> Refresh RAM
    public String update(String id, String nm, java.util.Date s, java.util.Date e, String de, int st, String t, 
                         String pid, Double pDs, Double mIv, Double dAm) {
        if (nm == null || nm.isEmpty() || s == null || e == null || s.after(e)) return "Dữ liệu không hợp lệ";
        if (dao.update(id, nm, df.format(s), df.format(e), de, st, t, pid, pDs, mIv, dAm)) {
            refreshData(); return "Cập nhật thành công";
        }
        return "Cập nhật thất bại";
    }

    // Xóa: Xóa DB -> Refresh RAM
    public String delete(String id) {
        if (id == null || id.isEmpty()) return "Mã lỗi";
        if (dao.delete(id)) { refreshData(); return "Xóa thành công"; }
        return "Xóa thất bại";
    }

    // Tìm kiếm nhanh trên RAM
    public ArrayList<Object[]> search(String key) {
        if (key == null || key.trim().isEmpty()) return getAll();
        ArrayList<Object[]> res = new ArrayList<>();
        String k = key.toLowerCase();
        for (Object[] p : getAll()) if (p[0].toString().toLowerCase().contains(k) || p[1].toString().toLowerCase().contains(k)) res.add(p);
        return res;
    }

    // Lấy chi tiết theo ID từ RAM
    public Object[] getByID(String id) {
        for (Object[] r : getAll()) if (r[0].toString().equalsIgnoreCase(id)) return r;
        return dao.getByID(id);
    }
}





