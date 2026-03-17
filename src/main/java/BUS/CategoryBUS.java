///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
package BUS;

import DAO.CategoryDAO;
import java.util.ArrayList;
import javax.swing.table.DefaultTableCellRenderer;

public class CategoryBUS {
    private ArrayList<Object[]> listCategory;
    private CategoryDAO categoryDAO = new CategoryDAO();

    public boolean isDuplicate(String id){
        ArrayList<Object[]> list = categoryDAO.getAll();
        for (Object[] row : list){
            if(row[0].toString().equalsIgnoreCase(id)){
                return true;
            }
        }
        return false;
    }
    

    
    public ArrayList<Object[]> getAll(){
        return categoryDAO.getAll();
    }
    
    public String add(String id, String name, String description){
        if (id.trim().isEmpty() || name.trim().isEmpty()){
            return "ID and name cannot be empty!";
        }
        if (isDuplicate(id)){
            return "Loi: he thong da co danh muc nay!";
        }
        if (categoryDAO.insert(id, name, description)){
            return "them danh muc thanh con!";
        } return "them danh muc that bai!";
    }
    
    public String delete(String id){
        if (id.trim().isEmpty()){
            return "ID khong hop le";
        } 
        if (categoryDAO.delete(id)){
            return "xoa danh muc thanh cong";
        } return "Xoa that bai!";  
    }
    
    public String update (String id, String name, String description){
        if(name.trim().isEmpty()){
            return "Ten danh muc khong duoc de trong!";
        }
        if(categoryDAO.update(id, name, description)){
            return "Cap nhat thanh cong";
        }
        return "cap nhat that bai!";
    }
    
    public ArrayList<Object[]> search(String keyword){
        ArrayList<Object[]> allCategory = categoryDAO.getAll();
        ArrayList<Object[]> result = new ArrayList<>();
        String lowerKey = keyword.toLowerCase();
        for (Object[] categoryo : allCategory) {
            if(categoryo[0].toString().toLowerCase().contains(lowerKey) ||
               categoryo[1].toString().toLowerCase().contains(lowerKey)){
                result.add(categoryo);
            }
        }
        return result;
    }

}


