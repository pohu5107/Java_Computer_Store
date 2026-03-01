/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import javax.swing.table.DefaultTableModel;
import DAO.BrandsDAO; // Gọi lớp DAO bạn vừa hoàn thành

public class BrandsBUS {
    private BrandsDAO brandsdao = new BrandsDAO();

    public DefaultTableModel getAllBrands() {
        // Tại đây bạn có thể thêm logic (ví dụ: kiểm tra quyền hạn) trước khi lấy dữ liệu
        return brandsdao.getBrandsTable(); 
    }
}
