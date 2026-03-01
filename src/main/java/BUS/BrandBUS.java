/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;
import DAO.BrandDAO;
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

        if (isDuplicate(id)) return "Loi: he thong da co ma";

        if (brandDAO.insert(id, name, address, phone)) return "Them thanh cong";
        return "Them that bai";
    }
    
    public String deleteBrandBUS(String id){
        if(id.trim().isEmpty()){
            return "ID khong co hop le";
        }
        if(brandDAO.delete(id)){
            return "xoa thanh cong";
        }
        return "Xoa that bai";
    }
    
    public String update(String id, String name, String address, String phone) {
        if (name.trim().isEmpty()) {
            return "Ten hang khong duoc de trong";
        }
        
        if (brandDAO.update(id, name, address, phone)) {
            return "cap nhat thanh cong";
        }
        return "cap nhat that bai";
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
