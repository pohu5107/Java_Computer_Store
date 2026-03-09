package BUS;

import DAO.ProductDAO;
import java.util.ArrayList;

public class ProductBUS {
    private ProductDAO productDAO = new ProductDAO();

    public ArrayList<Object[]> getAll() {
        return productDAO.getAll();
    }

    public boolean isDuplicate(String id) {
        ArrayList<Object[]> list = productDAO.getAll();
        for (Object[] row : list) {
            if (row[0].toString().equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    // Thêm mới mặc định số lượng là 0
    public String add(String id, String name, double price, String unit, String catID, String brandID, String cpu, String ram, String vga, String mainboard) {
        if (id.trim().isEmpty() || name.trim().isEmpty()) return "ID và Tên không được để trống";
        if (isDuplicate(id)) return "Lỗi: ID đã tồn tại";

        // Mặc định truyền số lượng 0 khi tạo mới
        if (productDAO.insert(id, name, 0, price, unit, catID, brandID, cpu, ram, vga, mainboard)) {
            return "Thêm thành công";
        }
        return "Thêm thất bại";
    }

    // Cập nhật thông tin (Đã bỏ tham số qty)
    public String update(String id, String name, double price, String unit, String catID, String brandID, String cpu, String ram, String vga, String mainboard) {
        if (name.trim().isEmpty()) return "Tên không được để trống";
        
        if (productDAO.update(id, name, price, unit, catID, brandID, cpu, ram, vga, mainboard)) {
            return "Cập nhật thành công";
        }
        return "Cập nhật thất bại";
    }

    public String delete(String id) {
        if (id.trim().isEmpty()) return "ID không hợp lệ";
        if (productDAO.delete(id)) return "Xóa thành công";
        return "Xóa thất bại";
    }

    // Hàm gọi từ SaleGUI hoặc ImportGUI
    public boolean updateStock(String id, int amount) {
        return productDAO.updateQuantity(id, amount);
    }

    public ArrayList<Object[]> search(String keyword) {
        ArrayList<Object[]> allProduct = productDAO.getAll();
        ArrayList<Object[]> result = new ArrayList<>();
        for (Object[] p : allProduct) {
            if (p[0].toString().contains(keyword) || p[1].toString().toLowerCase().contains(keyword.toLowerCase()))
                result.add(p);
        }
        return result;
    }

    public ArrayList<Object[]> searchByPrice(String input) {
        ArrayList<Object[]> allProduct = productDAO.getAll();
        ArrayList<Object[]> result = new ArrayList<>();
        String operator = input.trim().replaceAll("[0-9]", "");
        String money = input.trim().replaceAll("[^0-9]", "");
        if (money.isEmpty()) return allProduct;
        
        double searchPrice = Double.parseDouble(money);
        for (Object[] product : allProduct) {
            double productPrice = (double) product[3];
            boolean check = false;
            switch (operator) {
                case ">": if (productPrice > searchPrice) check = true; break;
                case ">=": if (productPrice >= searchPrice) check = true; break;
                case "<": if (productPrice < searchPrice) check = true; break;
                case "<=": if (productPrice <= searchPrice) check = true; break;
                case "=": case "": if (productPrice == searchPrice) check = true; break;
            }
            if (check) result.add(product);
        }
        return result;
    }

    public Object[] getByID(String id) {
        return (id == null || id.trim().isEmpty()) ? null : productDAO.getByID(id);
    }
}