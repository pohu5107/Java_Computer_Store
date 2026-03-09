package GUI;

import BUS.BrandBUS;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BrandGUI extends JFrame {
    private BrandBUS brandBUS = new BrandBUS();
    private JTable tblBrand;
    private DefaultTableModel model;
    private JTextField txtID, txtName, txtAddress, txtPhone, txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnSearch;

    public BrandGUI() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Quản Lý Thương Hiệu - Hệ Thống Máy Tính");
        setSize(950, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        
        // Màu nền nhẹ cho toàn bộ cửa sổ để làm nổi bật các panel trắng
        getContentPane().setBackground(new Color(240, 242, 245));

        // --- KHU VỰC NHẬP LIỆU ---
        JPanel pnlInput = new JPanel(null);
        pnlInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Thông tin thương hiệu"));
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBounds(20, 20, 895, 130);
        add(pnlInput);

        addLabelTextField(pnlInput, "Mã Thương Hiệu:", txtID = new JTextField(), 20, 35);
        addLabelTextField(pnlInput, "Tên Thương Hiệu:", txtName = new JTextField(), 20, 75);
        addLabelTextField(pnlInput, "Địa Chỉ:", txtAddress = new JTextField(), 450, 35);
        addLabelTextField(pnlInput, "Số ĐT:", txtPhone = new JTextField(), 450, 75);

        // --- TÌM KIẾM ---
        JLabel lblS = new JLabel("Tìm kiếm:");
        lblS.setBounds(30, 170, 70, 25);
        add(lblS);

        txtSearch = new JTextField();
        txtSearch.setBounds(100, 170, 200, 25);
        add(txtSearch);

        btnSearch = new JButton("Tìm Kiếm");
        btnSearch.setBounds(310, 170, 100, 25);
        styleButton(btnSearch, new Color(240, 240, 240), Color.BLACK); // Nút tìm kiếm màu sáng chữ đen

        // --- CÁC NÚT CHỨC NĂNG (MÀU RỰC RỠ NHƯ MẪU) ---
        btnAdd = new JButton("Thêm Mới");
        btnAdd.setBounds(435, 165, 110, 35);
        styleButton(btnAdd, new Color(40, 167, 69), Color.WHITE); // Xanh lá rực

        btnUpdate = new JButton("Cập Nhật");
        btnUpdate.setBounds(555, 165, 110, 35);
        styleButton(btnUpdate, new Color(0, 123, 255), Color.WHITE); // Xanh dương rực

        btnDelete = new JButton("Xóa");
        btnDelete.setBounds(675, 165, 110, 35);
        styleButton(btnDelete, new Color(220, 53, 69), Color.WHITE); // Đỏ rực

        btnRefresh = new JButton("Làm Mới");
        btnRefresh.setBounds(795, 165, 110, 35);
        styleButton(btnRefresh, new Color(108, 117, 125), Color.WHITE); // Xám đậm

        add(btnAdd); add(btnUpdate); add(btnDelete); add(btnRefresh); add(btnSearch);

        // --- BẢNG DỮ LIỆU ---
        String[] columns = {"Mã Thương Hiệu", "Tên Thương Hiệu", "Địa Chỉ", "Số Điện Thoại"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblBrand = new JTable(model);
        tblBrand.setRowHeight(30);
        tblBrand.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblBrand.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(tblBrand);
        scroll.setBounds(20, 215, 895, 370);
        add(scroll);

        // --- FIX LỖI NULLPOINTER TRONG ẢNH ---
        tblBrand.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblBrand.getSelectedRow();
                if (row != -1) {
                    txtID.setText(safeToString(model.getValueAt(row, 0)));
                    txtName.setText(safeToString(model.getValueAt(row, 1)));
                    txtAddress.setText(safeToString(model.getValueAt(row, 2)));
                    txtPhone.setText(safeToString(model.getValueAt(row, 3)));
                    txtID.setEditable(false);
                    txtID.setBackground(new Color(245, 245, 245));
                }
            }
        });

        // Events cho các nút (giữ nguyên logic của bạn)
        btnAdd.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, brandBUS.add(txtID.getText(), txtName.getText(), txtAddress.getText(), txtPhone.getText()));
            loadData();
        });

        btnUpdate.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, brandBUS.update(txtID.getText(), txtName.getText(), txtAddress.getText(), txtPhone.getText()));
            loadData();
        });

        btnDelete.addActionListener(e -> {
            String id = txtID.getText();
            if (id.isEmpty()) return;
            if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa?", "Xác nhận", 0) == 0) {
                JOptionPane.showMessageDialog(this, brandBUS.delete(id));
                refreshForm();
                loadData();
            }
        });

        btnSearch.addActionListener(e -> fillTable(brandBUS.search(txtSearch.getText().trim())));
        btnRefresh.addActionListener(e -> { refreshForm(); loadData(); });
    }

    // HÀM QUAN TRỌNG NHẤT: Ép nút hiển thị màu phẳng, đậm đà
    private void styleButton(JButton btn, Color bgColor, Color fgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        // Tắt hiệu ứng mặc định của hệ thống Windows
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false); // Quan trọng nhất
        btn.setOpaque(true);             // Ép hiển thị màu nền do mình chọn
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Thêm hiệu ứng Hover cho chuyên nghiệp
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgColor.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });
    }

    private void addLabelTextField(JPanel p, String label, JTextField t, int x, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl.setBounds(x, y, 110, 25);
        p.add(lbl);
        t.setBounds(x + 120, y, 280, 25);
        p.add(t);
    }

    private void loadData() { fillTable(brandBUS.getAll()); }

    private void fillTable(ArrayList<Object[]> list) {
        model.setRowCount(0);
        if (list != null) {
            for (Object[] row : list) model.addRow(row);
        }
    }

    private void refreshForm() {
        txtID.setText(""); txtName.setText(""); txtAddress.setText(""); txtPhone.setText("");
        txtID.setEditable(true); txtID.setBackground(Color.WHITE);
        tblBrand.clearSelection();
    }

    private String safeToString(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e) {}
        SwingUtilities.invokeLater(() -> new BrandGUI().setVisible(true));
    }
}