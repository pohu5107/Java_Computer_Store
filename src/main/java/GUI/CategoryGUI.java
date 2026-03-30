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

public class CategoryGUI extends JPanel {

    private CategoryBUS categoryBUS = new CategoryBUS();

    private JTextField txtSearch, txtID, txtName, txtDescription;
    private JTable tblCategory;
    private DefaultTableModel modelCategory;
    private JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnRefresh;

    public CategoryGUI() {
        setLayout(new BorderLayout(0, 10)); 
        setPreferredSize(new Dimension(950, 650)); 
        setBackground(new Color(240, 242, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        loadData();      
        setupEvents();    
    }

    private void initComponents() { 
        JPanel pnlTop = new JPanel(new BorderLayout(5, 5));
        pnlTop.setOpaque(false);
        pnlTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        txtSearch = new JTextField(30);
        txtSearch.setPreferredSize(new Dimension(300, 35));
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setText("Nhập tên hoặc mã danh mục (VD: LPT)...");
        
        btnSearch = new JButton("Tìm Kiếm");
        styleButton(btnSearch, new Color(0, 123, 255));
        btnSearch.setPreferredSize(new Dimension(120, 35)); 

        btnRefresh = new JButton("Làm Mới");
        styleButton(btnRefresh, new Color(108, 117, 125));
        btnRefresh.setPreferredSize(new Dimension(120, 35)); 

        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);
        pnlSearch.add(btnRefresh);
        pnlTop.add(pnlSearch, BorderLayout.NORTH);

        JPanel box1 = new JPanel(new BorderLayout());
        box1.setBackground(Color.WHITE);
        box1.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Thông tin Danh mục", TitledBorder.LEFT, TitledBorder.TOP));

        JPanel pnlInput = new JPanel(new GridLayout(3, 2, 20, 15));
        pnlInput.setOpaque(false);
        pnlInput.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        pnlInput.add(new JLabel("Mã Danh Mục:"));
        txtID = new JTextField(); 
        txtID.setPreferredSize(new Dimension(0, 30));
        pnlInput.add(txtID);

        pnlInput.add(new JLabel("Tên Danh Mục:"));
        txtName = new JTextField(); pnlInput.add(txtName);

        pnlInput.add(new JLabel("Mô tả:"));
        txtDescription = new JTextField(); pnlInput.add(txtDescription);

        box1.add(pnlInput, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlButtons.setOpaque(false);
        
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        
        styleButton(btnAdd, new Color(40, 167, 69)); 
        styleButton(btnUpdate, new Color(0, 123, 255)); 
        styleButton(btnDelete, new Color(220, 53, 69)); 

        Dimension btnSize = new Dimension(110, 40);
        btnAdd.setPreferredSize(btnSize);
        btnUpdate.setPreferredSize(btnSize);
        btnDelete.setPreferredSize(btnSize);

        pnlButtons.add(btnAdd);
        pnlButtons.add(btnUpdate);
        pnlButtons.add(btnDelete);
        box1.add(pnlButtons, BorderLayout.SOUTH);

        pnlTop.add(box1, BorderLayout.CENTER);
        add(pnlTop, BorderLayout.NORTH);

        JPanel box2 = new JPanel(new BorderLayout());
        box2.setBackground(Color.WHITE);
        box2.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Danh sách Danh mục", TitledBorder.LEFT, TitledBorder.TOP));

        String[] cols = {"Mã Danh Mục", "Tên Danh Mục", "Mô Tả"};
        modelCategory = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblCategory = new JTable(modelCategory);
        tblCategory.setRowHeight(30);
        tblCategory.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scroll = new JScrollPane(tblCategory);
        scroll.getViewport().setBackground(Color.WHITE);
        box2.add(scroll, BorderLayout.CENTER);
        
        add(box2, BorderLayout.CENTER);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); 
        btn.setOpaque(true);      
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupEvents() {
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

        tblCategory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblCategory.getSelectedRow();
                if (row != -1) {
                    txtID.setText(modelCategory.getValueAt(row, 0).toString());
                    txtName.setText(modelCategory.getValueAt(row, 1).toString());
                    Object desc = modelCategory.getValueAt(row, 2);
                    txtDescription.setText(desc != null ? desc.toString() : "");
                    txtID.setEditable(false);
                }
            }
        });

        btnAdd.addActionListener(e -> {
            String id = txtID.getText().trim();
            String name = txtName.getText().trim();
            String desc = txtDescription.getText().trim();
            if(id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã và Tên không được để trống!");
                return;
            }
            String msg = categoryBUS.add(id, name, desc);
            JOptionPane.showMessageDialog(this, msg);
            if (msg.toLowerCase().contains("thành công")) {
                loadData();
                clearForm();
            }
        });

        btnUpdate.addActionListener(e -> {
            String id = txtID.getText().trim();
            if (id.isEmpty()) { JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục!"); return; }
            String name = txtName.getText().trim();
            String desc = txtDescription.getText().trim();
            String msg = categoryBUS.update(id, name, desc);
            JOptionPane.showMessageDialog(this, msg);
            if (msg.toLowerCase().contains("thành công")) {
                loadData();
                clearForm();
            }
        });

        btnDelete.addActionListener(e -> {
            String id = txtID.getText().trim();
            if (id.isEmpty()) { JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục!"); return; }
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa danh mục " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String msg = categoryBUS.delete(id);
                JOptionPane.showMessageDialog(this, msg);
                if (msg.toLowerCase().contains("thành công")) {
                    loadData();
                    clearForm();
                }
            }
        });

        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.equals("Nhập tên hoặc mã danh mục (VD: LPT)...") || keyword.isEmpty()) {
                loadData();
            } else {
                fillTable(categoryBUS.search(keyword));
            }
        });

        btnRefresh.addActionListener(e -> { clearForm(); loadData(); });
    }

    private void fillTable(ArrayList<Object[]> list) {
        modelCategory.setRowCount(0);
        if (list != null) {
            for (Object[] row : list) modelCategory.addRow(row);
        }
    }

    private void loadData() {
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
}