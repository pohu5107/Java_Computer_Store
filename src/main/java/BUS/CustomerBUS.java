/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.CustomerDAO;
import java.sql.*;
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
    
    public boolean isDuplicate(String id){
        ArrayList<Object[]> list = customerDAO.getAll();
        for(Object[] row : list){
            if(row[0].toString().equalsIgnoreCase(id)){
                return true;
            }
        }
        return false;
    }
    
    
    
    public String add(String id, String firstName, String lastName, String address, String phone){
        if(id.trim().isEmpty() || firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
            return "ID va Ten khong duoc de trong";
        }
        
        if (isDuplicate(id)) return "loi: he thong da co ma!";
        
        if(customerDAO.insert(id, firstName, lastName, address, phone)){
            return "Them khach hang thanh cong";
        }
        return "them khach hang that bai";
    }
    
    public String delete(String id){
        if(id.trim().isEmpty()){
            return "ID khong hop le";
        }
        if(customerDAO.delete(id)){
            return "Xoa thanh cong";
        }
        return "Xoa that bai";
    }
    
    public String update(String id, String firstName, String lastName, String address, String phone){
        if(firstName.trim().isEmpty() || lastName.trim().isEmpty()){
            return "Ho va ten khong duoc de trong!";
        }
       if(customerDAO.update(id, firstName, lastName, address, phone)){
           return "cap nhat thanh cong";
       }
       return "Cap nhat that bai";
    }
     
    public ArrayList<Object[]> search(String keyword){
        ArrayList<Object[]> allCustomer = customerDAO.getAll();
        ArrayList<Object[]> result = new ArrayList<>();
        
        for(Object[] customer : allCustomer){
            if(customer[1].toString().toLowerCase().contains(keyword.toLowerCase()) ||
                customer[2].toString().toLowerCase().contains(keyword.toLowerCase())){
                result.add(customer);
            }
        }
        return result;
    }
    
    
    
    
    
    
    
}
