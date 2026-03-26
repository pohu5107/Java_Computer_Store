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
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245)); // Nền xám nhạt như hình mẫu
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- 1. TOP PANEL: TÌM KIẾM ---
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlTop.setOpaque(false);
        
        txtSearch = new JTextField(30);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setText("Nhập tên hoặc mã nhà cung cấp...");
        txtSearch.setForeground(Color.GRAY);
        
        btnTimKiem = new JButton("Tìm Kiếm");
        styleButton(btnTimKiem, new Color(0, 123, 255));
        btnTimKiem.setPreferredSize(new Dimension(120, 35)); // Fix kích thước hiển thị đủ chữ
        pnlTop.add(btnTimKiem);
        
        btnLamMoi = new JButton("Làm Mới");
        styleButton(btnLamMoi, new Color(108, 117, 125));
        btnLamMoi.setPreferredSize(new Dimension(120, 35)); // Fix kích thước hiển thị đủ chữ
        pnlTop.add(btnLamMoi);

        pnlTop.add(txtSearch);
        pnlTop.add(btnTimKiem);
        pnlTop.add(btnLamMoi);

        // --- 2. CENTER PANEL: THÔNG TIN NHẬP LIỆU ---
        JPanel pnlInputWrap = new JPanel(new BorderLayout());
        pnlInputWrap.setOpaque(false);
        
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                "THÔNG TIN NHÀ CUNG CẤP", TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 14), new Color(0, 102, 204)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã NCC
        gbc.gridx = 0; gbc.gridy = 0; pnlInput.add(new JLabel("Mã Nhân Viên:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; txtMaHieu = new JTextField(); pnlInput.add(txtMaHieu, gbc);

        // Số ĐT
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0; pnlInput.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0; txtSoDT = new JTextField(); pnlInput.add(txtSoDT, gbc);

        // Tên NCC
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; pnlInput.add(new JLabel("Tên:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; txtTenHieu = new JTextField(); pnlInput.add(txtTenHieu, gbc);

        // --- 3. ACTION PANEL: CÁC NÚT THÊM, SỬA, XÓA ---
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlActions.setOpaque(false);
        
        btnThemmoi = createStyledButton("Thêm Mới", new Color(40, 167, 69));
        btnCapNhat = createStyledButton("Cập Nhật", new Color(255, 153, 0));
        btnXoa = createStyledButton("Xóa", new Color(220, 53, 69));

        pnlActions.add(btnThemmoi);
        pnlActions.add(btnCapNhat);
        pnlActions.add(btnXoa);

        pnlInputWrap.add(pnlInput, BorderLayout.CENTER);
        pnlInputWrap.add(pnlActions, BorderLayout.SOUTH);

        // --- 4. BOTTOM PANEL: BẢNG DỮ LIỆU ---
        String[] columns = {"Mã NV", "Tên", "SĐT"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionBackground(new Color(232, 242, 254));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // GOM TẤT CẢ VÀO PANEL CHÍNH
        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.setOpaque(false);
        pnlNorth.add(pnlTop, BorderLayout.NORTH);
        pnlNorth.add(pnlInputWrap, BorderLayout.CENTER);

        add(pnlNorth, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Sự kiện
        loadData();
        addEvents();
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
        private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); // Tắt viền mặc định của Windows
        btn.setOpaque(true);         // Quan trọng: Để màu nền hiển thị rõ
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtMaHieu.setText(model.getValueAt(row, 0).toString());
                txtTenHieu.setText(model.getValueAt(row, 1).toString());
                txtSoDT.setText(model.getValueAt(row, 2).toString());
                txtMaHieu.setEditable(false);
            }
        });

        btnTimKiem.addActionListener(e -> {
            ArrayList<Object[]> result = supplierBUS.searchSupplier(txtSearch.getText());
            model.setRowCount(0);
            for (Object[] row : result) model.addRow(row);
        });

        btnThemmoi.addActionListener(e -> {
            String res = supplierBUS.addSupplier(txtMaHieu.getText(), txtTenHieu.getText(), txtSoDT.getText());
            JOptionPane.showMessageDialog(this, res);
            loadData();
        });

        btnCapNhat.addActionListener(e -> {
            String res = supplierBUS.updateSupplier(txtMaHieu.getText(), txtTenHieu.getText(), txtSoDT.getText());
            JOptionPane.showMessageDialog(this, res);
            loadData();
        });

        btnXoa.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?", "Thông báo", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String res = supplierBUS.deleteSupplier(txtMaHieu.getText());
                JOptionPane.showMessageDialog(this, res);
                loadData();
            }
        });

        btnLamMoi.addActionListener(e -> {
            txtMaHieu.setText(""); txtTenHieu.setText(""); txtSoDT.setText(""); txtSearch.setText("");
            txtMaHieu.setEditable(true);
            loadData();
        });
    }
}