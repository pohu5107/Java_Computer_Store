/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.CustomerDAO;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class CustomerBUS {

    private CustomerDAO customerDAO = new CustomerDAO();

    public DefaultTableModel getAll() {

        String[] columnNames = {"CustomerID", "CustomerName", "Address", "Phone"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        ArrayList<Object[]> list = customerDAO.getAll();

        for (Object[] row : list) {
            model.addRow(row);
        }

        return model;
    }
    
    public boolean isDuplicate(String id) {
        ArrayList<Object[]> list = customerDAO.getAll();
        for (Object[] row : list) {
            if (row[0].toString().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }
    
    public String add(String id, String firstName, String lastName, String address, String phone) {
        if (id.trim().isEmpty() || firstName.trim().isEmpty() || lastName.trim().isEmpty()) return "ID và Tên không được để trống";

        if (isDuplicate(id)) return "Lỗi: Hệ thống đã có mã";

        if (customerDAO.insert(id, firstName, lastName, address, phone)) return "Thêm thành công";
        return "Thêm thất bại";
    }
    
    public String delete(String id){
        if(id.trim().isEmpty()){
            return "ID không hợp lệ";
        }
        if(customerDAO.delete(id)){
            return "Xóa thành công";
        }
        return "Xóa thất bại";
    }
    
    public String update(String id, String firstName, String lastName, String address, String phone) {
        if (firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
            return "Họ và Tên khống được để trống";
        }
        
        if (customerDAO.update(id, firstName, lastName, address, phone)) {
            return "Cập nhật thành công";
        }
        return "Cập nhật thất bại";
    }
    
    public ArrayList<Object[]> search(String keyword) {
        ArrayList<Object[]> allCustomer = customerDAO.getAll();
        ArrayList<Object[]> result = new ArrayList<>();

        for (Object[] customer : allCustomer) {
            
            if (customer[1].toString().toLowerCase().contains(keyword.toLowerCase()) || 
                customer[2].toString().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(customer);
            }
        }
        return result;
    }
}
