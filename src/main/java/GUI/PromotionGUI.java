package GUI;

import BUS.PromotionBUS;
import com.toedter.calendar.JDateChooser; // Import thư viện lịch
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class PromotionGUI extends JPanel {
    private PromotionBUS promotionBUS = new PromotionBUS();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private JTable tblPromotion;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnSearch;

    public PromotionGUI() {
        setLayout(new BorderLayout(0, 10));
        setPreferredSize(new Dimension(950, 650));
        setBackground(new Color(240, 242, 245));

        initComponents();
        loadData();
        setupEvents();
    }

    private void initComponents() {
        // --- PHẦN TRÊN: THANH CÔNG CỤ ---
        JPanel pnlNorth = new JPanel(null);
        pnlNorth.setPreferredSize(new Dimension(950, 60));
        pnlNorth.setOpaque(false);

        JLabel lblS = new JLabel("Tìm kiếm:");
        lblS.setBounds(30, 15, 70, 25);
        pnlNorth.add(lblS);

        txtSearch = new JTextField();
        txtSearch.setBounds(100, 15, 200, 25);
        pnlNorth.add(txtSearch);

        btnSearch = new JButton("Tìm Kiếm");
        btnSearch.setBounds(310, 15, 90, 25);
        styleButton(btnSearch, new Color(240, 240, 240), Color.BLACK);
        pnlNorth.add(btnSearch);

        btnAdd = new JButton("Thêm Mới");
        btnAdd.setBounds(435, 10, 110, 35);
//        styleButton(btnAdd, new Color(40, 167, 69), Color.WHITE);

        btnUpdate = new JButton("Cập Nhật");
        btnUpdate.setBounds(555, 10, 110, 35);
//        styleButton(btnUpdate, new Color(0, 123, 255), Color.WHITE);

        btnDelete = new JButton("Xóa KM");
        btnDelete.setBounds(675, 10, 110, 35);
//        styleButton(btnDelete, new Color(220, 53, 69), Color.WHITE);

        btnRefresh = new JButton("Làm Mới");
        btnRefresh.setBounds(795, 10, 110, 35);
//        styleButton(btnRefresh, new Color(108, 117, 125), Color.WHITE);

        pnlNorth.add(btnAdd); 
        pnlNorth.add(btnUpdate); 
        pnlNorth.add(btnDelete); 
        pnlNorth.add(btnRefresh);
        
        add(pnlNorth, BorderLayout.NORTH);

        // --- PHẦN CENTER: BẢNG DỮ LIỆU ---
        String[] columns = {"Mã KM", "Tên KM", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblPromotion = new JTable(model);
        tblPromotion.setRowHeight(25);
        tblPromotion.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPromotion.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tblPromotion);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách khuyến mãi đang diễn ra"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        model.setRowCount(0);
        ArrayList<Object[]> list = promotionBUS.getActive();
        for (Object[] row : list) {
            model.addRow(new Object[]{
                row[0], row[1],
                sdf.format((Date)row[7]), // StartDate nằm ở index 7 trong getActive()
                sdf.format((Date)row[8]), // EndDate nằm ở index 8
                row[9]                    // Status nằm ở index 9
            });
        }
    }

    private void setupEvents() {
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadData();
        });

        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                loadData();
            } else {
                model.setRowCount(0);
                ArrayList<Object[]> list = promotionBUS.search(keyword);
                for (Object[] row : list) {
                    if ("Đang diễn ra".equals(row[9])) {
                        model.addRow(new Object[]{
                            row[0], row[1],
                            sdf.format((Date)row[7]),
                            sdf.format((Date)row[8]),
                            row[9]
                        });
                    }
                }
            }
        });

        btnAdd.addActionListener(e -> showAddDialog());

        btnUpdate.addActionListener(e -> {
            int selectedRow = tblPromotion.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một khuyến mãi để cập nhật!");
                return;
            }
            String id = (String) model.getValueAt(selectedRow, 0);
            showUpdateDialog(id);
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = tblPromotion.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một khuyến mãi để xóa!");
                return;
            }
            String id = (String) model.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khuyến mãi " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String result = promotionBUS.delete(id);
                JOptionPane.showMessageDialog(this, result);
                loadData();
            }
        });

        // Click đúp để sửa
        tblPromotion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = tblPromotion.getSelectedRow();
                    if (selectedRow != -1) {
                        String id = (String) model.getValueAt(selectedRow, 0);
                        showUpdateDialog(id);
                    }
                }
            }
        });
    }

    // ==========================================
    // DIALOG THÊM MỚI KHUYẾN MÃI
    // ==========================================
    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm khuyến mãi", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 420);
        dialog.setLocationRelativeTo(this);

        JPanel pnlContent = new JPanel(null);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblType = new JLabel("Loại khuyến mãi:");
        lblType.setBounds(20, 20, 120, 25);
        pnlContent.add(lblType);

        JComboBox<String> cbType = new JComboBox<>(new String[]{"Khuyến mãi theo sản phẩm", "Khuyến mãi theo giá tiền"});
        cbType.setBounds(150, 20, 200, 25);
        pnlContent.add(cbType);

        JTextField txtID = new JTextField();
        JTextField txtName = new JTextField();
        JDateChooser txtStartDate = new JDateChooser();
        JDateChooser txtEndDate = new JDateChooser();

        addLabelTextField(pnlContent, "Mã KM:", txtID, 20, 60);
        addLabelTextField(pnlContent, "Tên KM:", txtName, 20, 100);
        addLabelDateChooser(pnlContent, "Ngày bắt đầu:", txtStartDate, 20, 140);
        addLabelDateChooser(pnlContent, "Ngày kết thúc:", txtEndDate, 20, 180);

        JTextField txtProductID = new JTextField();
        JTextField txtDiscountPercent = new JTextField();
        JTextField txtMinAmount = new JTextField();
        JTextField txtMaxDiscount = new JTextField();

        cbType.addActionListener(e -> {
            String type = (String) cbType.getSelectedItem();
            // Xóa các component cũ
            pnlContent.remove(txtProductID); pnlContent.remove(txtDiscountPercent);
            pnlContent.remove(txtMinAmount); pnlContent.remove(txtMaxDiscount);
            Component[] comps = pnlContent.getComponents();
            for (Component comp : comps) {
                if (comp instanceof JLabel && (
                    ((JLabel) comp).getText().equals("Mã sản phẩm:") || ((JLabel) comp).getText().equals("% giảm giá:") ||
                    ((JLabel) comp).getText().equals("Mức áp dụng:") || ((JLabel) comp).getText().equals("Giới hạn giảm:"))) {
                    pnlContent.remove(comp);
                }
            }

            if ("Khuyến mãi theo sản phẩm".equals(type)) {
                addLabelTextField(pnlContent, "Mã sản phẩm:", txtProductID, 20, 220);
                addLabelTextField(pnlContent, "% giảm giá:", txtDiscountPercent, 20, 260);
            } else {
                addLabelTextField(pnlContent, "Mức áp dụng:", txtMinAmount, 20, 220);
                addLabelTextField(pnlContent, "Giới hạn giảm:", txtMaxDiscount, 20, 260);
            }
            pnlContent.revalidate();
            pnlContent.repaint();
        });

        cbType.setSelectedIndex(0); // Trigger initial

        JButton btnSave = new JButton("Lưu");
        btnSave.setBounds(200, 320, 100, 35);
//        styleButton(btnSave, new Color(40, 167, 69), Color.WHITE);
        pnlContent.add(btnSave);

        btnSave.addActionListener(e -> {
            try {
                String id = txtID.getText().trim();
                String name = txtName.getText().trim();
                String type = cbType.getSelectedItem().equals("Khuyến mãi theo sản phẩm") ? "Product" : "Price";
                
                if (txtStartDate.getDate() == null || txtEndDate.getDate() == null) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ngày tháng!");
                    return;
                }
                
                Date startDate = new Date(txtStartDate.getDate().getTime());
                Date endDate = new Date(txtEndDate.getDate().getTime());

                String productID = null;
                Double discountPercent = null;
                Double minAmount = null;
                Double maxDiscount = null;

                if ("Product".equals(type)) {
                    productID = txtProductID.getText().trim();
                    discountPercent = Double.parseDouble(txtDiscountPercent.getText().trim());
                } else {
                    minAmount = Double.parseDouble(txtMinAmount.getText().trim());
                    maxDiscount = Double.parseDouble(txtMaxDiscount.getText().trim());
                }

                String result = promotionBUS.add(id, name, type, productID, discountPercent, minAmount, maxDiscount, startDate, endDate);
                JOptionPane.showMessageDialog(dialog, result);
                if ("Thêm thành công".equals(result)) {
                    dialog.dispose();
                    loadData();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ! Vui lòng kiểm tra lại các ô số.");
            }
        });

        dialog.add(pnlContent, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    // ==========================================
    // DIALOG CẬP NHẬT KHUYẾN MÃI
    // ==========================================
    private void showUpdateDialog(String id) {
        Object[] promo = promotionBUS.getByID(id);
        if (promo == null) return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập nhật khuyến mãi", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 420);
        dialog.setLocationRelativeTo(this);

        JPanel pnlContent = new JPanel(null);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblType = new JLabel("Loại khuyến mãi:");
        lblType.setBounds(20, 20, 120, 25);
        pnlContent.add(lblType);

        JComboBox<String> cbType = new JComboBox<>(new String[]{"Khuyến mãi theo sản phẩm", "Khuyến mãi theo giá tiền"});
        cbType.setBounds(150, 20, 200, 25);
        if ("Product".equals(promo[2])) cbType.setSelectedIndex(0); else cbType.setSelectedIndex(1);
        pnlContent.add(cbType);

        JTextField txtID = new JTextField((String)promo[0]);
        txtID.setEditable(false); txtID.setBackground(new Color(240, 240, 240));
        JTextField txtName = new JTextField((String)promo[1]);
        
        JDateChooser txtStartDate = new JDateChooser();
        txtStartDate.setDate((Date)promo[7]);
        JDateChooser txtEndDate = new JDateChooser();
        txtEndDate.setDate((Date)promo[8]);

        addLabelTextField(pnlContent, "Mã KM:", txtID, 20, 60);
        addLabelTextField(pnlContent, "Tên KM:", txtName, 20, 100);
        addLabelDateChooser(pnlContent, "Ngày bắt đầu:", txtStartDate, 20, 140);
        addLabelDateChooser(pnlContent, "Ngày kết thúc:", txtEndDate, 20, 180);

        JTextField txtProductID = new JTextField(promo[3] != null ? (String)promo[3] : "");
        JTextField txtDiscountPercent = new JTextField(promo[4] != null && (Double)promo[4] > 0 ? promo[4].toString() : "");
        JTextField txtMinAmount = new JTextField(promo[5] != null && (Double)promo[5] > 0 ? promo[5].toString() : "");
        JTextField txtMaxDiscount = new JTextField(promo[6] != null && (Double)promo[6] > 0 ? promo[6].toString() : "");

        cbType.addActionListener(e -> {
            String type = (String) cbType.getSelectedItem();
            pnlContent.remove(txtProductID); pnlContent.remove(txtDiscountPercent);
            pnlContent.remove(txtMinAmount); pnlContent.remove(txtMaxDiscount);
            Component[] comps = pnlContent.getComponents();
            for (Component comp : comps) {
                if (comp instanceof JLabel && (
                    ((JLabel) comp).getText().equals("Mã sản phẩm:") || ((JLabel) comp).getText().equals("% giảm giá:") ||
                    ((JLabel) comp).getText().equals("Mức áp dụng:") || ((JLabel) comp).getText().equals("Giới hạn giảm:"))) {
                    pnlContent.remove(comp);
                }
            }

            if ("Khuyến mãi theo sản phẩm".equals(type)) {
                addLabelTextField(pnlContent, "Mã sản phẩm:", txtProductID, 20, 220);
                addLabelTextField(pnlContent, "% giảm giá:", txtDiscountPercent, 20, 260);
            } else {
                addLabelTextField(pnlContent, "Mức áp dụng:", txtMinAmount, 20, 220);
                addLabelTextField(pnlContent, "Giới hạn giảm:", txtMaxDiscount, 20, 260);
            }
            pnlContent.revalidate();
            pnlContent.repaint();
        });

        cbType.setSelectedIndex(cbType.getSelectedIndex()); // Trigger

        JButton btnSave = new JButton("Lưu");
        btnSave.setBounds(200, 320, 100, 35);
        styleButton(btnSave, new Color(0, 123, 255), Color.WHITE);
        pnlContent.add(btnSave);

        btnSave.addActionListener(e -> {
            try {
                String name = txtName.getText().trim();
                String type = cbType.getSelectedItem().equals("Khuyến mãi theo sản phẩm") ? "Product" : "Price";
                
                if (txtStartDate.getDate() == null || txtEndDate.getDate() == null) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ngày tháng!");
                    return;
                }
                
                Date startDate = new Date(txtStartDate.getDate().getTime());
                Date endDate = new Date(txtEndDate.getDate().getTime());

                String productID = null;
                Double discountPercent = null;
                Double minAmount = null;
                Double maxDiscount = null;

                if ("Product".equals(type)) {
                    productID = txtProductID.getText().trim();
                    discountPercent = Double.parseDouble(txtDiscountPercent.getText().trim());
                } else {
                    minAmount = Double.parseDouble(txtMinAmount.getText().trim());
                    maxDiscount = Double.parseDouble(txtMaxDiscount.getText().trim());
                }

                String result = promotionBUS.update(id, name, type, productID, discountPercent, minAmount, maxDiscount, startDate, endDate);
                JOptionPane.showMessageDialog(dialog, result);
                if ("Cập nhật thành công".equals(result)) {
                    dialog.dispose();
                    loadData();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ! Vui lòng kiểm tra lại các ô số.");
            }
        });

        dialog.add(pnlContent, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    // ==========================================
    // HÀM TIỆN ÍCH (UTILITIES)
    // ==========================================
    private void addLabelTextField(JPanel panel, String label, JTextField field, int x, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(x, y, 150, 25);
        field.setBounds(x + 130, y, 180, 25); // Chỉnh lại khoảng cách cho đẹp
        panel.add(lbl);
        panel.add(field);
    }
    
    private void addLabelDateChooser(JPanel panel, String label, JDateChooser dc, int x, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(x, y, 150, 25);
        dc.setBounds(x + 130, y, 180, 25);
        dc.setDateFormatString("yyyy-MM-dd");
        panel.add(lbl);
        panel.add(dc);
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}