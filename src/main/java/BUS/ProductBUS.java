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
        if(id.trim().isEmpty()||name.trim().isEmpty()) return "ID và Tên không được để trống";
        
        if(isDuplicate(id)) return "Lỗi: ID đã tồn tại";
        
        if(productDAO.insert(id, name, qty, price, unit, catID, brandID, cpu, ram, vga, mainboard)) return "Thêm thành công";
        
        return "Thêm thất bại";
    }
    
    public String delete(String id){
        if(id.trim().isEmpty()) return "ID không hợp lệ";
        
        if(productDAO.delete(id)) return "Xóa thành công";
        
        return "Xóa thất bại";
    }
    
    public String update(String id, String name, int qty, double price, String unit, String catID, String brandID, String cpu, String ram, String vga, String mainboard){
        if(name.trim().isEmpty()) return "Tên không được để trống";
        
        if(productDAO.update(id, name, qty, price, unit, catID, brandID, cpu, ram, vga, mainboard)) return "Cập nhật thành công";
        
        return "Cập nhật thất bại";
    }
    
    public ArrayList<Object[]> search(String keyword){
        ArrayList<Object[]> allProduct = productDAO.getAll();
        ArrayList<Object[]> result = new ArrayList();
        
       for(Object[] product: allProduct){
           if(product[0].toString().trim().contains(keyword) ||
              product[1].toString().trim().contains(keyword))
               result.add(product);
       }
       return result;
    }
    
    public ArrayList<Object[]> searchByPrice(String input){
        ArrayList<Object[]> allProduct = productDAO.getAll();
        ArrayList<Object[]> result = new ArrayList();
        
        String operator = input.trim().replaceAll("[0-9]", "");
        String money = input.trim().replaceAll("[^0-9]", "");
        
        if(money.isEmpty()) return allProduct;
        
        double searchPrice = Double.parseDouble(money);
        
       for(Object[] product: allProduct){
           Double ProductPrice = (double) product[3];
           boolean check = false;
           switch(operator){
               case ">":
                   if(ProductPrice > searchPrice) check = true;
                   break;
               case ">=":
                   if(ProductPrice >= searchPrice) check = true;
                   break;
               case "<":
                   if(ProductPrice < searchPrice) check = true;
                   break;
               case "<=":
                   if(ProductPrice <= searchPrice) check = true;
                   break;
               case "=":
                   if(ProductPrice == searchPrice) check = true;
                   break;
           }
           if(check) result.add(product);
       }
       return result;
    } 
    public Object[] getByID(String id) {
        if(id == null || id.trim().isEmpty()) return null;
        return productDAO.getByID(id);
    }
}
