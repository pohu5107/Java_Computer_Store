/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
            if (row[0].toString().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }
    
    public String add(String id, String name, int qty, double price, String unit, String catID, String brandID, String cpu, String ram, String vga, String mainboard){
        if(id.trim().isEmpty()||name.trim().isEmpty()) return "ID va Ten khong duoc de trong";
        
        if(isDuplicate(id)) return "Loi: da co ID ton tai";
        
        if(productDAO.insert(id, name, qty, price, unit, catID, brandID, cpu, ram, vga, mainboard)) return "Them thanh cong";
        
        return "Them that bai";
    }
    
    public String delete(String id){
        if(id.trim().isEmpty()) return "ID khong hop le";
        
        if(productDAO.delete(id)) return "Xoa thanh cong";
        
        return "Xoa that bai";
    }
    
    public String update(String id, String name, int qty, double price, String unit, String catID, String brandID, String cpu, String ram, String vga, String mainboard){
        if(name.trim().isEmpty()) return "Ten khong duoc de trong";
        
        if(productDAO.update(id, name, qty, price, unit, catID, brandID, cpu, ram, vga, mainboard)) return "Cap nhat thanh cong";
        
        return "Cap nhat that bai";
    }
    public ArrayList<Object[]> search(String keyword){
        ArrrayList<Object[]> list = productDAO.getAll();
        
    }
}
