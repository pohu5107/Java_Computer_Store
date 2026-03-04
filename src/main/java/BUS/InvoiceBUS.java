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
        ArrayList<Object[]> list = invoiceDAO.getAll();
        for (Object[] row : list) {
            if (row[0].toString().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }
    
    public String add(String id, String customerID, String staffID, double total, ArrayList<Object[]> details){
        if(id.trim().isEmpty() || customerID.trim().isEmpty() || staffID.trim().isEmpty()) return "Thieu ID cua hoa don, khach hang hoac nhan vien";
        
        if(details == null || details.isEmpty()) return "Hoa don phai co it nhat 1 san pham";
        
        if(isDuplicate(id)) return "ID hoa don da ton tai";
        
        if(invoiceDAO.insert(id, customerID, staffID, total, details)) return "Them hoa don thanh cong";
        
        return "Them hoa don that bai";
    }
    
    public String delete(String id){
        if(id.trim().isEmpty()) return "ID khong hop le";
        
        if(invoiceDAO.delete(id)) return "Xoa hoa don thanh cong";
        
        return "Xoa hoa don that bai";
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
