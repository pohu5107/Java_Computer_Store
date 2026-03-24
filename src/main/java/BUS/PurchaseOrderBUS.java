/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.PurchaseOrderDAO;
import java.util.ArrayList;

public class PurchaseOrderBUS {
    private PurchaseOrderDAO orderDAO = new PurchaseOrderDAO();

    public ArrayList<Object[]> getAll() {
        return orderDAO.getAll();
    }

    public ArrayList<Object[]> getDetailsByOrderID(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return new ArrayList<>(); // Trả về mảng rỗng nếu mã không hợp lệ
        }
        return orderDAO.getDetailsByOrderID(orderId);
    }

    //  tự động sinh mã phiếu nhập mới
    public String generateNextID() {
        String lastID = orderDAO.getLastID();
        if (lastID == null || lastID.isEmpty()) {
            return "PN001"; 
        }
        try {
            String prefix = lastID.replaceAll("[0-9]", ""); // Lấy chữ (VD: PN)
            String numberStr = lastID.replaceAll("[^0-9]", ""); // Lấy số (VD: 005)
            
            int nextNumber = Integer.parseInt(numberStr) + 1;
            
            return prefix + String.format("%0" + numberStr.length() + "d", nextNumber);
        } catch (Exception e) {
            // Đề phòng trường hợp mã cũ trong DB bị sai định dạng chuẩn
            return "PN_NEW"; 
        }
    }

    public String add(String staffId, String supplierId, double totalAmount, ArrayList<Object[]> details) {
        if (staffId == null || staffId.trim().isEmpty() || supplierId == null || supplierId.trim().isEmpty()) {
            return "Lỗi: Vui lòng chọn nhân viên và nhà cung cấp";
        }
        
        if (details == null || details.isEmpty()) {
            return "Lỗi: Phiếu nhập phải có ít nhất 1 mặt hàng";
        }
        
        if (totalAmount <= 0) {
            return "Lỗi: Tổng tiền phiếu nhập không hợp lệ";
        }
        
        String newOrderID = generateNextID();
        if (orderDAO.insert(newOrderID, staffId, supplierId, totalAmount, details)) {
            return "Tạo phiếu nhập thành công. Mã phiếu nhập : " + newOrderID;
        }
        return "Lỗi: Không thể lưu phiếu nhập";
    }
}