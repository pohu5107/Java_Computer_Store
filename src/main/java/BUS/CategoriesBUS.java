/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import javax.swing.table.DefaultTableModel;
import DAO.CategoriesDAO;

public class CategoriesBUS {
    private CategoriesDAO categoriesdao = new CategoriesDAO();

    public DefaultTableModel getAllCategories() {
        // Bạn có thể thêm logic kiểm tra hoặc sắp xếp ở đây nếu cần
        return categoriesdao.getCategoriesTable();
    }
}