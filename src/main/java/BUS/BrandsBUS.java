/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import javax.swing.table.DefaultTableModel;
import DAO.BrandsDAO; // Gọi lớp DAO bạn vừa hoàn thành
import java.util.ArrayList;

public class BrandsBUS {
    private BrandsDAO brandsdao = new BrandsDAO();
// Trong file CategoriesBUS.java
public DefaultTableModel getAllCategories() {
    ArrayList<Object[]> data = dao.getAll(); // Gọi hàm mới từ DAO
    String[] columnNames = {"Mã Danh Mục", "Tên Danh Mục", "Mô Tả"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
    
    for (Object[] row : data) {
        model.addRow(row);
    }
    return model;
}
}
