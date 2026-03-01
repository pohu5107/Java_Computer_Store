/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;
import BUS.BrandBUS;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BrandGUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private BrandBUS brandBUS = new BrandBUS();
    
    public BrandGUI() {
        setTitle("Test Kết Nối Dữ Liệu - Brand");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Tạo bảng và Header
        String[] columns = {"Mã Hãng", "Tên Hãng", "Địa chỉ", "SĐT"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 2. Tạo nút bấm để Test
        JButton btnLoad = new JButton("Nạp dữ liệu từ MySQL");
        btnLoad.addActionListener(e -> loadData());
        add(btnLoad, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        // Gọi BUS lấy dữ liệu
        ArrayList<Object[]> list = brandBUS.getAll();
        
        // Xóa dữ liệu cũ trên Table
        model.setRowCount(0);

        // Đổ dữ liệu mới vào
        for (Object[] row : list) {
            model.addRow(row);
        }
        
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Dữ liệu trống hoặc lỗi kết nối!");
        }
    }
    
    public static void main(String[] args) {
        // Chạy thử giao diện
        SwingUtilities.invokeLater(() -> {
            new BrandGUI().setVisible(true);
        });
    }

}
