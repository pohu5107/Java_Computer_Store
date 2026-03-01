/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;
import DAO.BrandDAO;
import java.util.ArrayList;

public class BrandBUS {
    private BrandDAO brandDAO = new BrandDAO();
    
    public ArrayList<Object[]> getALLBrandBUS() {
        return brandDAO.getAllBrandDAO();
    }
    
    public String addBrandBUS(String id, String name, String address, String phone){
        if(id.trim().isEmpty() || name.trim().isEmpty()){
            return "ID va Ten khong duoc de trong";
        }
        if(brandDAO.insertBrandDAO(id, name, address, phone)){
            return "Them thanh cong";
        }
        return "Them that bai(Trung ma)";
    }
}
