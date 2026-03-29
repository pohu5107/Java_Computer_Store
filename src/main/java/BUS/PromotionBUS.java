/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.PromotionDAO;
import java.util.ArrayList;
import java.sql.Date;

public class PromotionBUS {
    private final PromotionDAO promotionDAO = new PromotionDAO();

    public ArrayList<Object[]> getAll() {
        promotionDAO.updateStatus(); 
        return promotionDAO.getAll();
    }

    public ArrayList<Object[]> getActive() {
        promotionDAO.updateStatus();
        return promotionDAO.getActive();
    }

    public boolean isDuplicate(String id) {
        ArrayList<Object[]> list = promotionDAO.getAll();
        for (Object[] row : list) {
            if (row[0].toString().equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    public String add(String id, String name, String type, String productID, Double discountPercent, Double minAmount, Double maxDiscount, Date startDate, Date endDate) {
        if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            return "ID và Tên không được để trống";
        }
        if (isDuplicate(id)) {
            return "Lỗi: ID đã tồn tại";
        }
        if (startDate == null || endDate == null) {
            return "Ngày bắt đầu và ngày kết thúc không được để trống";
        }
        if (startDate.after(endDate)) {
            return "Ngày bắt đầu phải trước ngày kết thúc";
        }

        if ("Product".equals(type)) {
            if (productID == null || productID.trim().isEmpty()) return "Mã sản phẩm không được để trống";
            if (discountPercent == null || discountPercent <= 0 || discountPercent > 100) return "% giảm giá phải từ 1 đến 100";
        } else if ("Price".equals(type)) {
            if (minAmount == null || minAmount <= 0) return "Mức áp dụng phải lớn hơn 0";
            if (maxDiscount == null || maxDiscount <= 0) return "Giới hạn giảm phải lớn hơn 0";
        } else {
            return "Loại khuyến mãi không hợp lệ";
        }

        if (promotionDAO.insert(id, name, type, productID, discountPercent, minAmount, maxDiscount, startDate, endDate)) {
            return "Thêm thành công";
        }
        return "Thêm thất bại";
    }

    public String update(String id, String name, String type, String productID, Double discountPercent, Double minAmount, Double maxDiscount, Date startDate, Date endDate) {
        if (name == null || name.trim().isEmpty()) {
            return "Tên không được để trống";
        }
        if (startDate == null || endDate == null) {
            return "Ngày bắt đầu và ngày kết thúc không được để trống";
        }
        if (startDate.after(endDate)) {
            return "Ngày bắt đầu phải trước ngày kết thúc";
        }

        if ("Product".equals(type)) {
            if (productID == null || productID.trim().isEmpty()) return "Mã sản phẩm không được để trống";
            if (discountPercent == null || discountPercent <= 0 || discountPercent > 100) return "% giảm giá phải từ 1 đến 100";
        } else if ("Price".equals(type)) {
            if (minAmount == null || minAmount <= 0) return "Mức áp dụng phải lớn hơn 0";
            if (maxDiscount == null || maxDiscount <= 0) return "Giới hạn giảm phải lớn hơn 0";
        } else {
            return "Loại khuyến mãi không hợp lệ";
        }

        // Gọi DAO
        if (promotionDAO.update(id, name, type, productID, discountPercent, minAmount, maxDiscount, startDate, endDate)) {
            return "Cập nhật thành công";
        }
        return "Cập nhật thất bại";
    }

    // Xóa khuyến mãi
    public String delete(String id) {
        if (id == null || id.trim().isEmpty()) return "ID không hợp lệ";
        if (promotionDAO.delete(id)) return "Xóa thành công";
        return "Xóa thất bại";
    }

    // Tìm kiếm
    public ArrayList<Object[]> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        
        ArrayList<Object[]> allPromotion = promotionDAO.getAll();
        ArrayList<Object[]> result = new ArrayList<>();
        String lowerKey = keyword.toLowerCase();
        
        for (Object[] p : allPromotion) {
            if (p[0].toString().toLowerCase().contains(lowerKey) || 
                p[1].toString().toLowerCase().contains(lowerKey)) {
                result.add(p);
            }
        }
        return result;
    }

    // Lấy chi tiết theo ID
    public Object[] getByID(String id) {
        return (id == null || id.trim().isEmpty()) ? null : promotionDAO.getByID(id);
    }
}
