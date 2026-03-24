/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;
import DAO.BrandDAO;
import javax.swing.table.DefaultTableModel;
import DAO.BrandDAO; 
import java.util.ArrayList;

import java.util.ArrayList;

public class BrandBUS {
    private BrandDAO brandDAO = new BrandDAO();
    
    public ArrayList<Object[]> getAll() {
        return brandDAO.getAll();
    }
    
    public boolean isDuplicate(String id) {
        ArrayList<Object[]> list = brandDAO.getAll();
        for (Object[] row : list) {
            if (row[0].toString().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }
    
    public String add(String id, String name, String address, String phone) {
        if (id.trim().isEmpty() || name.trim().isEmpty()) return "ID va Ten khong duoc de trong";

        if (isDuplicate(id)) return "Loi: Hệ thống đã có mã này";

        if (brandDAO.insert(id, name, address, phone)) return "Thêm thành công";
        return "Them that bai";
    }
    
    public String delete(String id){
        if(id.trim().isEmpty()){
            return "ID không hợp lệ";
        }
        if(brandDAO.delete(id)){
            return "Xóa thành công";
        }
        return "Xóa thất bại";
    }
    
    public String update(String id, String name, String address, String phone) {
        if (name.trim().isEmpty()) {
            return "Tên hãng không được để trống";
        }
        
        if (brandDAO.update(id, name, address, phone)) {
            return "cập nhật thành công";
        }
        return "Cập nhật thất bại";
    }
    
    // search theo id hoac ten
    public ArrayList<Object[]> search(String keyword) {
        ArrayList<Object[]> allBrands = brandDAO.getAll();
        ArrayList<Object[]> result = new ArrayList<>();

        String lowerKey = keyword.toLowerCase();

        for (Object[] brand : allBrands) {
            
            if (brand[0].toString().toLowerCase().contains(lowerKey) || 
                brand[1].toString().toLowerCase().contains(lowerKey)) {
                result.add(brand);
            }
        }
        return result;
    }
}