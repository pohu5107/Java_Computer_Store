package GUI;

import BUS.SupplierBUS;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SupplierGUI extends JPanel {

    private JTextField txtMaHieu, txtTenHieu, txtSoDT, txtSearch;
    private JButton btnTimKiem, btnThemmoi, btnCapNhat, btnXoa, btnLamMoi;
    private JTable table;
    private DefaultTableModel model;
    private SupplierBUS supplierBUS = new SupplierBUS();

    public SupplierGUI() {
        // Sử dụng BorderLayout cho JPanel chính
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // --- 1. Panel Nhập liệu ---
        JPanel pnlInput = new JPanel(new GridLayout(2, 2, 20, 10));
        pnlInput.setBorder(BorderFactory.createTitledBorder(null, "Thông tin thương hiệu", 
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14)));
        pnlInput.setBackground(new Color(245, 245, 245));
        
        pnlInput.add(new JLabel("Mã Nhà Cung Cấp:"));
        txtMaHieu = new JTextField();
        pnlInput.add(txtMaHieu);

        pnlInput.add(new JLabel("Tên Nhà Cung Cấp:"));
        txtTenHieu = new JTextField();
        pnlInput.add(txtTenHieu);

        pnlInput.add(new JLabel("Số ĐT:"));
        txtSoDT = new JTextField();
        pnlInput.add(txtSoDT);
        
        pnlInput.add(new JLabel("")); // Giữ layout cân đối

        // --- 2. Panel Chức năng ---
        JPanel pnlControl = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlControl.setBackground(Color.WHITE);
        
        pnlControl.add(new JLabel("Tìm kiếm:"));
        txtSearch = new JTextField(15);
        pnlControl.add(txtSearch);

        btnTimKiem = new JButton("Tìm Kiếm");
        
        btnThemmoi = createStyledButton("Thêm Mới", new Color(40, 167, 69));
        btnCapNhat = createStyledButton("Cập Nhật", new Color(0, 123, 255));
        btnXoa = createStyledButton("Xóa", new Color(220, 53, 69));
        btnLamMoi = createStyledButton("Làm Mới", new Color(108, 117, 125));

        pnlControl.add(btnTimKiem);
        pnlControl.add(btnThemmoi);
        pnlControl.add(btnCapNhat);
        pnlControl.add(btnXoa);
        pnlControl.add(btnLamMoi);

        // Gom nhóm Panel phía trên
        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.add(pnlInput, BorderLayout.NORTH);
        pnlNorth.add(pnlControl, BorderLayout.CENTER);

        // --- 3. Bảng dữ liệu ---
        String[] columns = {"Mã Thương Hiệu", "Tên Thương Hiệu", "Số Điện Thoại"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);

        // Thêm các thành phần vào JPanel chính (this)
        add(pnlNorth, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // --- Khởi tạo dữ liệu và sự kiện ---
        loadData();
        addEvents();
    }

    // Hàm tạo nút bấm có màu sắc như yêu cầu trước đó
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(100, 35));
        return btn;
    }

    private void loadData() {
        model.setRowCount(0);
        ArrayList<Object[]> list = supplierBUS.getAllSupplierDAO();
        for (Object[] row : list) {
            model.addRow(row);
        }
    }
 
    private void addEvents() {
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtMaHieu.setText(model.getValueAt(row, 0).toString());
                txtTenHieu.setText(model.getValueAt(row, 1).toString());
                txtSoDT.setText(model.getValueAt(row, 2).toString());
                txtMaHieu.setEditable(false);
            }
        });

        btnThemmoi.addActionListener(e -> {
            String result = supplierBUS.addSupplier(txtMaHieu.getText(), txtTenHieu.getText(), txtSoDT.getText());
            JOptionPane.showMessageDialog(this, result);
            loadData();
        });

        btnCapNhat.addActionListener(e -> {
            String result = supplierBUS.updateSupplier(txtMaHieu.getText(), txtTenHieu.getText(), txtSoDT.getText());
            JOptionPane.showMessageDialog(this, result);
            loadData();
        });

        btnXoa.addActionListener(e -> {
            String id = txtMaHieu.getText();
            if(id.isEmpty()) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String result = supplierBUS.deleteSupplier(id);
                JOptionPane.showMessageDialog(this, result);
                loadData();
                clearFields();
            }
        });

        btnLamMoi.addActionListener(e -> clearFields());

        btnTimKiem.addActionListener(e -> {
            ArrayList<Object[]> result = supplierBUS.searchSupplier(txtSearch.getText());
            model.setRowCount(0);
            for (Object[] row : result) {
                model.addRow(row);
            }
        });
    }

    private void clearFields() {
        txtMaHieu.setText("");
        txtTenHieu.setText("");
        txtSoDT.setText("");
        txtSearch.setText("");
        txtMaHieu.setEditable(true);
        loadData();
    }
}