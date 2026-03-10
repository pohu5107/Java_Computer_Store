/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.StaffDAO;
import java.sql.Date;
import java.util.ArrayList;

public class StaffBUS {
    private StaffDAO staffDAO = new StaffDAO();

    // 1. Lấy toàn bộ danh sách nhân viên
    public ArrayList<Object[]> getAll() {
        return staffDAO.getAll();
    }

    // 2. Kiểm tra trùng lặp Mã nhân viên
    public boolean isDuplicate(String id) {
        ArrayList<Object[]> list = staffDAO.getAll();
        for (Object[] row : list) {
            if (row[0].toString().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    // 3. Thêm Nhân viên mới
    // LƯU Ý: Các tham số truyền vào (id, name, phone, role...) cần khớp với cấu trúc bảng Staff trong Database của bạn
    public String add(String id, String firstName, String lastName, String gender, Date birthDate, String phone) {
        if (id.trim().isEmpty() || firstName.trim().isEmpty()) {
            return "Mã, Tên không được để trống!";
        }

        if (isDuplicate(id)) {
            return "Lỗi: Mã nhân viên đã tồn tại trong hệ thống!";
        }

        // Gọi hàm insert từ StaffDAO (bạn cần đảm bảo StaffDAO có hàm này với tham số tương ứng)
        if (staffDAO.insert(id, firstName, lastName, gender, birthDate, phone)) {
            return "Thêm nhân viên thành công!";
        }
        return "Thêm nhân viên thất bại!";
    }

    // 4. Cập nhật thông tin Nhân viên
    public String update(String id, String firstName, String lastName, String gender, Date birthDate, String phone) {
        if (firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
            return "Tên nhân viên không được để trống!";
        }
        if (staffDAO.update(id, firstName, lastName, gender, birthDate, phone)) {
            return "Cập nhật thông tin thành công!";
        }
        return "Cập nhật thất bại!";
    }

    // 5. Xóa Nhân viên
    public String delete(String id) {
        if (id.trim().isEmpty()) {
            return "Mã nhân viên không hợp lệ!";
        }
        if (staffDAO.delete(id)) {
            return "Xóa nhân viên thành công!";
        }
        return "Xóa nhân viên thất bại!";
    }

    // 6. Tìm kiếm Nhân viên (Theo Mã hoặc Tên)
    public ArrayList<Object[]> search(String keyword) {
        ArrayList<Object[]> allStaff = staffDAO.getAll();
        ArrayList<Object[]> result = new ArrayList<>();
        String lowerKey = keyword.toLowerCase();
        for (Object[] staff : allStaff) {
            if (staff[0].toString().toLowerCase().contains(lowerKey) || 
                staff[1].toString().toLowerCase().contains(lowerKey)) {
                result.add(staff);
            }
        }
        return result;
    }
    

}