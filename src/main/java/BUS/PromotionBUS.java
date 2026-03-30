/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.PromotionDAO;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

/**
 * Lớp BUS: Quản lý logic Khuyến mãi và RAM Cache.
 */
public class PromotionBUS {
<<<<<<< HEAD
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
=======
    private final PromotionDAO promotionDAO = new PromotionDAO();
    private final SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ArrayList<Object[]> listPromotions;

    public PromotionBUS() {
        refreshData();
    }

    public void refreshData() {
        promotionDAO.updateStatusByDate();
        this.listPromotions = promotionDAO.getAll();
    }

    public ArrayList<Object[]> getAll() {
        if (listPromotions == null) refreshData();
        return listPromotions;
>>>>>>> phat
    }

    // Kiểm tra trùng mã ID trên RAM
    public boolean isDuplicate(String id) {
<<<<<<< HEAD
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
=======
        for (Object[] row : getAll()) {
            if (row[0].toString().equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    public String add(String id, String name, java.util.Date start, java.util.Date end, String desc, String type, 
                      String productID, Double prodDisc, Double minInv, Double discAmt, Double invDisc, Double maxDisc) {
        
        if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            return "Mã và Tên không được để trống";
        }
        if (isDuplicate(id)) {
            return "Lỗi: Mã khuyến mãi đã tồn tại";
        }
        if (start == null || end == null) {
            return "Ngày bắt đầu và kết thúc không được để trống";
        }
        if (start.after(end)) {
            return "Ngày bắt đầu phải trước ngày kết thúc";
        }

        if ("Product".equals(type)) {
            if (productID == null || productID.trim().isEmpty()) return "Mã sản phẩm không được để trống";
            if (prodDisc == null || prodDisc <= 0 || prodDisc > 100) return "% giảm giá phải từ 1 đến 100";
        } else if ("Price".equals(type)) {
            if (minInv == null || minInv < 0) return "Mức áp dụng không hợp lệ";
        }

        String startStr = sqlDateFormat.format(start);
        String endStr = sqlDateFormat.format(end);

        if (promotionDAO.insert(id, name, startStr, endStr, desc, type, productID, prodDisc, minInv, discAmt, invDisc, maxDisc)) {
            refreshData(); // Cập nhật RAM
            return "Thêm thành công";
>>>>>>> phat
        }
        return "Thêm thất bại";
    }

<<<<<<< HEAD
    // Cập nhật: Kiểm tra logic -> Ghi DB -> Refresh RAM
    public String update(String id, String nm, java.util.Date s, java.util.Date e, String de, int st, String t, 
                         String pid, Double pDs, Double mIv, Double dAm) {
        if (nm == null || nm.isEmpty() || s == null || e == null || s.after(e)) return "Dữ liệu không hợp lệ";
        if (dao.update(id, nm, df.format(s), df.format(e), de, st, t, pid, pDs, mIv, dAm)) {
            refreshData(); return "Cập nhật thành công";
=======
    public String update(String id, String name, java.util.Date start, java.util.Date end, String desc, int status, String type, 
                         String productID, Double prodDisc, Double minInv, Double discAmt, Double invDisc, Double maxDisc) {
        
        if (name == null || name.trim().isEmpty()) {
            return "Tên không được để trống";
        }
        if (start == null || end == null) {
            return "Ngày bắt đầu và kết thúc không được để trống";
        }
        if (start.after(end)) {
            return "Ngày bắt đầu phải trước ngày kết thúc";
        }

        if ("Product".equals(type)) {
            if (productID == null || productID.trim().isEmpty()) return "Mã sản phẩm không được để trống";
            if (prodDisc == null || prodDisc <= 0 || prodDisc > 100) return "% giảm giá phải từ 1 đến 100";
        }

        String startStr = sqlDateFormat.format(start);
        String endStr = sqlDateFormat.format(end);

        if (promotionDAO.update(id, name, startStr, endStr, desc, status, type, productID, prodDisc, minInv, discAmt, invDisc, maxDisc)) {
            refreshData(); // Cập nhật RAM
            return "Cập nhật thành công";
>>>>>>> phat
        }
        return "Cập nhật thất bại";
    }

<<<<<<< HEAD
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
=======
    public String delete(String id) {
        if (id == null || id.trim().isEmpty()) return "Mã không hợp lệ";
        if (promotionDAO.delete(id)) {
            refreshData(); // Cập nhật RAM
            return "Xóa thành công";
        }
        return "Xóa thất bại";
    }

    public ArrayList<Object[]> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        
        ArrayList<Object[]> result = new ArrayList<>();
        String lowerKey = keyword.toLowerCase();
        
        for (Object[] p : getAll()) {
            if (p[0].toString().toLowerCase().contains(lowerKey) || 
                p[1].toString().toLowerCase().contains(lowerKey)) {
                result.add(p);
            }
        }
        return result;
    }

    public Object[] getByID(String id) {
        if (id == null || id.trim().isEmpty()) return null;
        for (Object[] row : getAll()) {
            if (row[0].toString().equalsIgnoreCase(id)) return row;
        }
        // Nếu không có trong RAM (hiếm khi xảy ra nếu logic đồng bộ tốt), lấy từ DB
        return promotionDAO.getByID(id);
>>>>>>> phat
    }
}


<<<<<<< HEAD



=======
>>>>>>> phat
