/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.SupplierDAO;
import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author ACER
 */
public class SupplierBUS {
    private SupplierDAO supplierDAO = new SupplierDAO();
    
    public ArrayList<Object[]> getAllSupplierDAO() {
        return supplierDAO.getAllSupplierDAO();
    }
    
    public boolean isDuplicate(String id) {
        ArrayList<Object[]> list = supplierDAO.getAllSupplierDAO();
        for (Object[] row : list) {
            if (row[0].toString().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }
    
    public String addSupplier(String id, String supplierName, String address, String phone) {
        if (id.trim().isEmpty() || supplierName.trim().isEmpty()) return "ID và Tên không được để trống";

        if (isDuplicate(id)) return "Lỗi: Hệ thống đã có mã";

        return "Thêm thất bại";
    }
    
    public String updateSupplier(String id, String supplierName, String address, String phone) {
        if (supplierName.trim().isEmpty()) {
            return "Tên không được để trống";
        }
        
        if (supplierDAO.updateSupplier(id, supplierName, address, phone)) {
            return "Cập nhật thành công";
        }
        return "Cập nhật thất bại";
    }
        
    public String deleteSupplier(String id){
        if(id.trim().isEmpty()){
            return "ID không hợp lệ";
        }
        if(supplierDAO.deleteSupplier(id)){
            return "Xóa thành công";
        }
        return "Xóa thất bại";
    }
    

    
    // search theo id hoac ten
    public ArrayList<Object[]> searchSupplier(String keyword) {
        ArrayList<Object[]> allSuppliers= supplierDAO.getAllSupplierDAO();
        ArrayList<Object[]> result = new ArrayList<>();

        for (Object[] supplier : allSuppliers) {
            
            if (supplier[0].toString().toLowerCase().contains(keyword.toLowerCase()) || 
                supplier[1].toString().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(supplier);
            }
        }
        return result;
    }
}
