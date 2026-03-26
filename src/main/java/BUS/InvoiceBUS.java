/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;
import DAO.InvoiceDAO;
import java.util.ArrayList;


public class InvoiceBUS {
    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    
    public ArrayList<Object[]> getAll() {
        return invoiceDAO.getAll();
    }
    
    public boolean isDuplicate(String id) {
        String searchId = id.trim();
        ArrayList<Object[]> list = invoiceDAO.getAll();
        for (Object[] row : list) {
            if (row[0].toString().trim().equalsIgnoreCase(searchId)) {
                return true;
            }
        }
        return false;
    }

    public String add(String id, String customerID, String staffID, double total, ArrayList<Object[]> details){
        id = id.trim();
        if(id.isEmpty() || customerID.trim().isEmpty() || staffID.trim().isEmpty()) 
            return "Thiếu ID của hóa đơn, khách hàng hoặc nhân viên";
        if(details == null || details.isEmpty()) 
            return "Hóa đơn phải có ít nhất 1 sản phẩm";
        if(isDuplicate(id)) return "ID " + id + " đã tồn tại trong hệ thống!";
        if(invoiceDAO.insert(id, customerID, staffID, total, details)) 
            return "Thêm thành công";

        return "Thêm thất bại";
    }
    
    public String delete(String id){
        if(id.trim().isEmpty()) return "ID không hợp lệ";
        
        if(invoiceDAO.delete(id)) return "Xóa thành công";
        
        return "Xóa thất bại";
    }
    
    public ArrayList<Object[]> searchByID(String id) {
        ArrayList<Object[]> all = invoiceDAO.getAll();
        ArrayList<Object[]> result = new ArrayList();
        for (Object[] row : all){
            if(row[0].toString().toLowerCase().contains(id.toLowerCase())) result.add(row);
        }
        return result;
    }
    
    public ArrayList<Object[]> searchByCustomerID(String id) {
        return invoiceDAO.getByCustomerID(id);
    }
    
    public ArrayList<Object[]> searchDetailByID(String id) {
        return invoiceDAO.getDetailsByInvoiceID(id);
    }
    
}
