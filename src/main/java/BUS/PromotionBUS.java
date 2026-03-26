package BUS;

import DAO.PromotionDAO;
import java.util.ArrayList;
import java.sql.Date;

public class PromotionBUS {
<<<<<<< Updated upstream
    private final PromotionDAO promotionDAO = new PromotionDAO();

    public ArrayList<Object[]> getAll() {
=======
    private PromotionDAO promotionDAO = new PromotionDAO();

    public ArrayList<Object[]> getAll() {
        promotionDAO.updateStatus(); // Cập nhật trạng thái trước khi lấy
>>>>>>> Stashed changes
        return promotionDAO.getAll();
    }

    public ArrayList<Object[]> getActive() {
<<<<<<< Updated upstream
=======
        promotionDAO.updateStatus();
>>>>>>> Stashed changes
        return promotionDAO.getActive();
    }

    public boolean isDuplicate(String id) {
        ArrayList<Object[]> list = promotionDAO.getAll();
        for (Object[] row : list) {
<<<<<<< Updated upstream
            if (row[0].toString().equalsIgnoreCase(id)) {
                return true;
            }
=======
            if (row[0].toString().equalsIgnoreCase(id)) return true;
>>>>>>> Stashed changes
        }
        return false;
    }

<<<<<<< Updated upstream
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
=======
    public String add(String id, String name, String type, String productID, Double discountPercent, Double minAmount, Double maxDiscount, Date startDate, Date endDate) {
        if (id.trim().isEmpty() || name.trim().isEmpty()) return "ID và Tên không được để trống";
        if (isDuplicate(id)) return "Lỗi: ID đã tồn tại";
        if (startDate.after(endDate)) return "Ngày bắt đầu phải trước ngày kết thúc";

        if (type.equals("Product")) {
            if (productID == null || productID.trim().isEmpty()) return "Mã sản phẩm không được để trống";
            if (discountPercent == null || discountPercent <= 0 || discountPercent > 100) return "% giảm giá phải từ 1 đến 100";
        } else if (type.equals("Price")) {
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
        if (name.trim().isEmpty()) return "Tên không được để trống";
        if (startDate.after(endDate)) return "Ngày bắt đầu phải trước ngày kết thúc";

        if (type.equals("Product")) {
            if (productID == null || productID.trim().isEmpty()) return "Mã sản phẩm không được để trống";
            if (discountPercent == null || discountPercent <= 0 || discountPercent > 100) return "% giảm giá phải từ 1 đến 100";
        } else if (type.equals("Price")) {
            if (minAmount == null || minAmount <= 0) return "Mức áp dụng phải lớn hơn 0";
            if (maxDiscount == null || maxDiscount <= 0) return "Giới hạn giảm phải lớn hơn 0";
        } else {
            return "Loại khuyến mãi không hợp lệ";
        }

        if (promotionDAO.update(id, name, type, productID, discountPercent, minAmount, maxDiscount, startDate, endDate)) {
            return "Cập nhật thành công";
        }
        return "Cập nhật thất bại";
    }

    public String delete(String id) {
        if (id.trim().isEmpty()) return "ID không hợp lệ";
        if (promotionDAO.delete(id)) return "Xóa thành công";
        return "Xóa thất bại";
    }

    public ArrayList<Object[]> search(String keyword) {
        ArrayList<Object[]> allPromotion = promotionDAO.getAll();
        ArrayList<Object[]> result = new ArrayList<>();
        for (Object[] p : allPromotion) {
            if (p[0].toString().contains(keyword) || p[1].toString().toLowerCase().contains(keyword.toLowerCase()))
                result.add(p);
>>>>>>> Stashed changes
        }
        return result;
    }

    public Object[] getByID(String id) {
        return (id == null || id.trim().isEmpty()) ? null : promotionDAO.getByID(id);
    }
<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
