/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;
import DAO.StaffDAO;
import java.sql.Date;
import java.util.ArrayList;
/**
 *
 * @author ACER
 */
public class StaffBUS {
    private StaffDAO staffDAO = new StaffDAO();
    
    public ArrayList<Object[]> getAllStaffDAO() {
        return staffDAO.getAllStaffDAO();
    }
    
    public boolean isDuplicate(String id) {
        ArrayList<Object[]> list = staffDAO.getAllStaffDAO();
        for (Object[] row : list) {
            if (row[0].toString().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }
    
    public String addStaffBUS(String id, String firstName, String lastName, String gender, Date date, String phone) {
        if (id.trim().isEmpty() || firstName.trim().isEmpty()) return "ID và Tên không được để trống";

        if (isDuplicate(id)) return "Lỗi: Hệ thống đã có mã";

        if (staffDAO.createStaff(id, firstName, lastName, gender, date, phone)) return "Thêm thành công";
        return "Thêm thất bại";
    }
    
    public String updateStaffBUS(String id, String firstName, String lastName, String gender, Date date, String phone) {
        if (firstName.trim().isEmpty()) {
            return "Tên không được để trống";
        }
        
        if (staffDAO.updateStaff(id, firstName, lastName, gender, date, phone)) {
            return "Cập nhật thành công";
        }
        return "Cập nhật thất bại";
    }
        
    public String deleteStaffBUS(String id){
        if(id.trim().isEmpty()){
            return "ID không hợp lệ";
        }
        if(staffDAO.deleteStaff(id)){
            return "Xóa thành công";
        }
        return "Xóa thất bại";
    }
    

    
    // search theo id hoac ten
    public ArrayList<Object[]> search(String keyword) {
        ArrayList<Object[]> allStaffs = staffDAO.getAllStaffDAO();
        ArrayList<Object[]> result = new ArrayList<>();

        for (Object[] staff : allStaffs) {
            
            if (staff[0].toString().toLowerCase().contains(keyword.toLowerCase()) || 
                staff[1].toString().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(staff);
            }
        }
        return result;
    }
}
