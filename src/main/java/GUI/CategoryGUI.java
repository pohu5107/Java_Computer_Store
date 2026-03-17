/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BUS.CategoryBUS;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CategoryGUI extends JFrame {

    private CategoryBUS categoryBUS = new CategoryBUS();

    // Các thành phần giao diện
    private JTextField txtSearch, txtID, txtName, txtDescription;
    private JTable tblCategory;
    private DefaultTableModel modelCategory;
    private JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnRefresh;

    public CategoryGUI() {
        initComponents();
        loadDataToTable(); // Load dữ liệu ngay khi mở form
    }

    private void initComponents() {
        setTitle("Quản Lý Danh Mục Sản Phẩm");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));


        JPanel pnlTop = new JPanel(new BorderLayout(5, 5));
        pnlTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Panel Search
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(30);
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setText("Nhập tên hoặc mã danh mục (VD: LPT)...");
        
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Nhập tên hoặc mã danh mục (VD: LPT)...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setForeground(Color.GRAY);
                    txtSearch.setText("Nhập tên hoặc mã danh mục (VD: LPT)...");
                }
            }
        });

        btnSearch = new JButton("Tìm Kiếm");
        btnRefresh = new JButton("Làm Mới");
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);
        pnlSearch.add(btnRefresh);
        
        pnlTop.add(pnlSearch, BorderLayout.NORTH);

        // 2. Box 1: Điền thông tin
        JPanel box1 = new JPanel(new BorderLayout());
        box1.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Thông tin Danh mục", TitledBorder.LEFT, TitledBorder.TOP));

        JPanel pnlInput = new JPanel(new GridLayout(3, 2, 15, 15));
        pnlInput.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        pnlInput.add(new JLabel("Mã Danh Mục:"));
        txtID = new JTextField();
        pnlInput.add(txtID);

        pnlInput.add(new JLabel("Tên Danh Mục:"));
        txtName = new JTextField();
        pnlInput.add(txtName);

        pnlInput.add(new JLabel("Mô tả:"));
        txtDescription = new JTextField();
        pnlInput.add(txtDescription);

        box1.add(pnlInput, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        
        btnAdd.setBackground(new Color(34, 139, 34)); 
        btnUpdate.setBackground(new Color(0, 102, 204)); 
        btnDelete.setBackground(new Color(204, 0, 0)); 

        pnlButtons.add(btnAdd);
        pnlButtons.add(btnUpdate);
        pnlButtons.add(btnDelete);
        
        box1.add(pnlButtons, BorderLayout.SOUTH);

        pnlTop.add(box1, BorderLayout.CENTER);
        add(pnlTop, BorderLayout.NORTH);

        // Box 2: bảng dữ liệu
        JPanel box2 = new JPanel(new BorderLayout());
        box2.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Danh sách Danh mục", TitledBorder.LEFT, TitledBorder.TOP));

        String[] cols = {"Mã Danh Mục", "Tên Danh Mục", "Mô Tả"};
        modelCategory = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblCategory = new JTable(modelCategory);
        tblCategory.setRowHeight(25);
        box2.add(new JScrollPane(tblCategory), BorderLayout.CENTER);
        
        add(box2, BorderLayout.CENTER);


        
        // Click bảng -> Đổ dữ liệu lên textfield
        tblCategory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblCategory.getSelectedRow();
                if (row != -1) {
                    txtID.setText(modelCategory.getValueAt(row, 0).toString());
                    txtName.setText(modelCategory.getValueAt(row, 1).toString());
                    
                    Object desc = modelCategory.getValueAt(row, 2);
                    txtDescription.setText(desc != null ? desc.toString() : "");
                    
                    txtID.setEditable(false); // Không cho sửa Mã
                }
            }
        });

        // Nút thêm
        btnAdd.addActionListener(e -> {
            String id = txtID.getText().trim();
            String name = txtName.getText().trim();
            String desc = txtDescription.getText().trim();

            String msg = categoryBUS.add(id, name, desc);
            JOptionPane.showMessageDialog(this, msg);
            if (msg.contains("thanh con")) { // Dựa theo thông báo trong BUS của bạn
                loadDataToTable();
                clearForm();
            }
        });

        // Nút sửa
        btnUpdate.addActionListener(e -> {
            String id = txtID.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục cần sửa!");
                return;
            }
            String name = txtName.getText().trim();
            String desc = txtDescription.getText().trim();

            String msg = categoryBUS.update(id, name, desc);
            JOptionPane.showMessageDialog(this, msg);
            if (msg.contains("thanh cong")) {
                loadDataToTable();
                clearForm();
            }
        });

        // Nút xóa
        btnDelete.addActionListener(e -> {
            String id = txtID.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục cần xóa!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa danh mục " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String msg = categoryBUS.delete(id);
                JOptionPane.showMessageDialog(this, msg);
                if (msg.contains("thanh cong")) {
                    loadDataToTable();
                    clearForm();
                }
            }
        });

        // Nút tìm kiếm
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.equals("Nhập tên hoặc mã danh mục (VD: LPT)...") || keyword.isEmpty()) {
                loadDataToTable();
            } else {
                fillTable(categoryBUS.search(keyword));
            }
        });

        // Nút làm mới
        btnRefresh.addActionListener(e -> {
            clearForm();
            loadDataToTable();
        });
    }

    private void fillTable(ArrayList<Object[]> list) {
        modelCategory.setRowCount(0);
        for (Object[] row : list) {
            modelCategory.addRow(row);
        }
    }

    private void loadDataToTable() {
        fillTable(categoryBUS.getAll());
    }

    private void clearForm() {
        txtID.setText("");
        txtID.setEditable(true);
        txtName.setText("");
        txtDescription.setText("");
        
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setText("Nhập tên hoặc mã danh mục (VD: LPT)...");
        tblCategory.clearSelection();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        new CategoryGUI().setVisible(true);
    }
}
