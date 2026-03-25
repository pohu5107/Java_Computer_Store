package BUS;

import DAO.PromotionDAO;
import java.util.ArrayList;
import java.sql.Date;

public class PromotionBUS {
    private final PromotionDAO promotionDAO = new PromotionDAO();

    public ArrayList<Object[]> getAll() {
        return promotionDAO.getAll();
    }

    public ArrayList<Object[]> getActive() {
        return promotionDAO.getActive();
    }

    public boolean isDuplicate(String id) {
        ArrayList<Object[]> list = promotionDAO.getAll();
        for (Object[] row : list) {
            if (row[0].toString().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public String add(String id, String name, double discountPercent, Date startDate, Date endDate) {
        if (id.trim().isEmpty() || name.trim().isEmpty()) {
            return "Mã và Tên khuyến mãi không được để trống";
        }

        if (isDuplicate(id)) {
            return "Lỗi: Mã khuyến mãi đã tồn tại";
        }

        if (startDate == null || endDate == null) {
            return "Ngày bắt đầu và ngày kết thúc không được để trống";
        }

        if (startDate.after(endDate)) {
            return "Ngày bắt đầu phải trước ngày kết thúc";
        }

        if (discountPercent <= 0 || discountPercent > 100) {
            return "Phần trăm giảm giá phải từ 1 đến 100";
        }

        if (promotionDAO.insert(id, name, discountPercent, startDate, endDate)) {
            return "Thêm khuyến mãi thành công";
        }
        return "Thêm khuyến mãi thất bại";
    }

    public String update(String id, String name, double discountPercent, Date startDate, Date endDate, int status) {
        if (name.trim().isEmpty()) {
            return "Tên khuyến mãi không được để trống";
        }

        if (startDate == null || endDate == null) {
            return "Ngày bắt đầu và ngày kết thúc không được để trống";
        }

        if (startDate.after(endDate)) {
            return "Ngày bắt đầu phải trước ngày kết thúc";
        }

        if (discountPercent <= 0 || discountPercent > 100) {
            return "Phần trăm giảm giá phải từ 1 đến 100";
        }

        if (promotionDAO.update(id, name, discountPercent, startDate, endDate, status)) {
            return "Cập nhật khuyến mãi thành công";
        }
        return "Cập nhật khuyến mãi thất bại";
    }

    public String delete(String id) {
        if (id.trim().isEmpty()) {
            return "Mã không hợp lệ";
        }

        if (promotionDAO.delete(id)) {
            return "Xóa khuyến mãi thành công";
        }
        return "Xóa khuyến mãi thất bại";
    }

    public ArrayList<Object[]> search(String keyword) {
        ArrayList<Object[]> allPromotions = promotionDAO.getAll();
        ArrayList<Object[]> result = new ArrayList<>();

        for (Object[] promotion : allPromotions) {
            if (promotion[0].toString().toLowerCase().contains(keyword.toLowerCase()) ||
                promotion[1].toString().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(promotion);
            }
        }
        return result;
    }

    public Object[] getByID(String id) {
        return (id == null || id.trim().isEmpty()) ? null : promotionDAO.getByID(id);
    }
}
