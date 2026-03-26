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
            return "Mã và tên không được để trống";
        }
        if (isDuplicate(id)){
            return "Loi: Hệ thống đã có danh mục này";
        }
        if (categoryDAO.insert(id, name, description)){
            return "Thêm danh mục thành công";
        } return "Thêm danh mục thất bại";
    }
    
    public String delete(String id){
        if (id.trim().isEmpty()){
            return "ID khong hop le";
        } 
        if (categoryDAO.delete(id)){
            return "Xóa danh mục thành công";
        } return "Xóa danh mục thất bại";  
    }
    
    public String update (String id, String name, String description){
        if(name.trim().isEmpty()){
            return "Tên danh mục không được để trống";
        }
        if(categoryDAO.update(id, name, description)){
            return "Cập nhật thành công";
        }
        return "Cập nhật thất bại";
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