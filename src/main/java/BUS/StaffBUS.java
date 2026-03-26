
package BUS;

import DAO.StaffDAO;
import java.sql.Date;
import java.util.ArrayList;

public class StaffBUS {
    private StaffDAO staffDAO = new StaffDAO();

    public ArrayList<Object[]> getAll() {
        return staffDAO.getAll();
    }

    public boolean isDuplicate(String id) {
        ArrayList<Object[]> list = staffDAO.getAll();
        for (Object[] row : list) {
            if (row[0].toString().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public String add(String id, String firstName, String lastName, String gender, Date birthDate, String phone) {
        if (id.trim().isEmpty() || firstName.trim().isEmpty()) {
            return "Mã, Tên không được để trống!";
        }

        if (isDuplicate(id)) {
            return "Lỗi: Mã nhân viên đã tồn tại trong hệ thống";
        }

        if (staffDAO.insert(id, firstName, lastName, gender, birthDate, phone)) {
            return "Thêm nhân viên thành công!";
        }
        return "Thêm nhân viên thất bại!";
    }

    public String update(String id, String firstName, String lastName, String gender, Date birthDate, String phone) {
        if (firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
            return "Tên nhân viên không được để trống!";
        }
        if (staffDAO.update(id, firstName, lastName, gender, birthDate, phone)) {
            return "Cập nhật thông tin thành công!";
        }
        return "Cập nhật thất bại!";
    }

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