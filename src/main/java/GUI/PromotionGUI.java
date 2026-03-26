package GUI;

import BUS.PromotionBUS;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
<<<<<<< Updated upstream
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;

public class PromotionGUI extends JPanel {
    private final PromotionBUS promotionBUS = new PromotionBUS();

    private JTable tblPromotion;
    private DefaultTableModel model;
    private JTextField txtID, txtName, txtDiscountPercent, txtSearch;
=======
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
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        JPanel pnlNorth = new JPanel(null);
        pnlNorth.setPreferredSize(new Dimension(950, 220));
        pnlNorth.setOpaque(false);

        JPanel pnlInput = new JPanel(null);
        pnlInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Thông tin khuyến mãi"));
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBounds(20, 10, 895, 130);

        addLabelTextField(pnlInput, "Mã KM:", txtID = new JTextField(), 20, 35);
        addLabelTextField(pnlInput, "Tên KM:", txtName = new JTextField(), 20, 75);
        addLabelTextField(pnlInput, "% Giảm:", txtDiscountPercent = new JTextField(), 450, 35);
        
        pnlNorth.add(pnlInput);

        // --- KHU VỰC TÌM KIẾM ---
        JLabel lblS = new JLabel("Tìm kiếm:");
        lblS.setBounds(30, 160, 70, 30);
        pnlNorth.add(lblS);

        txtSearch = new JTextField();
        txtSearch.setBounds(100, 160, 200, 30);
        pnlNorth.add(txtSearch);

        btnSearch = new JButton("Tìm Kiếm");
        btnSearch.setBounds(310, 160, 100, 30);
        styleButton(btnSearch, new Color(240, 240, 240), Color.BLACK);
        pnlNorth.add(btnSearch);

        // --- CÁC NÚT CHỨC NĂNG ---
        btnAdd = new JButton("Thêm Mới");
        btnAdd.setBounds(435, 155, 110, 40);
        styleButton(btnAdd, new Color(40, 167, 69), Color.WHITE);

        btnUpdate = new JButton("Cập Nhật");
        btnUpdate.setBounds(555, 155, 110, 40);
        styleButton(btnUpdate, new Color(0, 123, 255), Color.WHITE);

        btnDelete = new JButton("Xóa");
        btnDelete.setBounds(675, 155, 110, 40);
        styleButton(btnDelete, new Color(220, 53, 69), Color.WHITE);

        btnRefresh = new JButton("Làm Mới");
        btnRefresh.setBounds(795, 155, 110, 40);
        styleButton(btnRefresh, new Color(108, 117, 125), Color.WHITE);

        pnlNorth.add(btnAdd);
        pnlNorth.add(btnUpdate);
        pnlNorth.add(btnDelete);
        pnlNorth.add(btnRefresh);
        add(pnlNorth, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        pnlCenter.setOpaque(false);

        String[] columns = {"Mã KM", "Tên Khuyến Mãi", "% Giảm", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Trạng Thái"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tblPromotion = new JTable(model);
        tblPromotion.setRowHeight(30);
        pnlCenter.add(new JScrollPane(tblPromotion), BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);
    }

    private void setupEvents() {
        tblPromotion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblPromotion.getSelectedRow();
                if (row != -1) {
                    txtID.setText(safeToString(model.getValueAt(row, 0)));
                    txtName.setText(safeToString(model.getValueAt(row, 1)));
                    txtDiscountPercent.setText(safeToString(model.getValueAt(row, 2)));
                    txtID.setEditable(false);
                    txtID.setBackground(new Color(245, 245, 245));

                    // Hiển thị chi tiết trong popup
                    if (e.getClickCount() == 2) {
                        showDetailDialog(txtID.getText());
=======
        // --- PHẦN TRÊN: THANH CÔNG CỤ ---
        JPanel pnlNorth = new JPanel(null);
        pnlNorth.setPreferredSize(new Dimension(950, 50));
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
        styleButton(btnAdd, new Color(40, 167, 69), Color.WHITE);

        btnUpdate = new JButton("Cập Nhật");
        btnUpdate.setBounds(555, 10, 110, 35);
        styleButton(btnUpdate, new Color(0, 123, 255), Color.WHITE);

        btnDelete = new JButton("Xóa KM");
        btnDelete.setBounds(675, 10, 110, 35);
        styleButton(btnDelete, new Color(220, 53, 69), Color.WHITE);

        btnRefresh = new JButton("Làm Mới");
        btnRefresh.setBounds(795, 10, 110, 35);
        styleButton(btnRefresh, new Color(108, 117, 125), Color.WHITE);

        pnlNorth.add(btnAdd); pnlNorth.add(btnUpdate); pnlNorth.add(btnDelete); pnlNorth.add(btnRefresh);
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
                sdf.format((Date)row[2]),
                sdf.format((Date)row[3]),
                row[4]
            });
        }
    }

    private void setupEvents() {
        btnRefresh.addActionListener(e -> loadData());

        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                loadData();
            } else {
                model.setRowCount(0);
                ArrayList<Object[]> list = promotionBUS.search(keyword);
                for (Object[] row : list) {
                    if ("Đang diễn ra".equals(row[4])) {
                        model.addRow(new Object[]{
                            row[0], row[1],
                            sdf.format((Date)row[2]),
                            sdf.format((Date)row[3]),
                            row[4]
                        });
>>>>>>> Stashed changes
                    }
                }
            }
        });

        btnAdd.addActionListener(e -> showAddDialog());

<<<<<<< Updated upstream
        btnUpdate.addActionListener(e -> showEditDialog());

        btnDelete.addActionListener(e -> {
            String id = txtID.getText();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần xóa");
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khuyến mãi này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION) == 0) {
                JOptionPane.showMessageDialog(this, promotionBUS.delete(id));
                refreshForm();
=======
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
>>>>>>> Stashed changes
                loadData();
            }
        });

<<<<<<< Updated upstream
        btnSearch.addActionListener(e -> fillTable(promotionBUS.search(txtSearch.getText().trim())));
        btnRefresh.addActionListener(e -> {
            refreshForm();
            loadData();
        });
    }

    private void showDetailDialog(String promotionID) {
        Object[] promotion = promotionBUS.getByID(promotionID);
        if (promotion == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khuyến mãi");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi Tiết Khuyến Mãi", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel pnlDetail = new JPanel(null);
        pnlDetail.setBackground(Color.WHITE);
        pnlDetail.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        int y = 20;
        addDetailLabel(pnlDetail, "Mã KM:", promotion[0].toString(), 20, y);
        y += 40;
        addDetailLabel(pnlDetail, "Tên KM:", promotion[1].toString(), 20, y);
        y += 40;
        addDetailLabel(pnlDetail, "% Giảm:", promotion[2].toString(), 20, y);
        y += 40;
        addDetailLabel(pnlDetail, "Ngày Bắt Đầu:", promotion[3].toString(), 20, y);
        y += 40;
        addDetailLabel(pnlDetail, "Ngày Kết Thúc:", promotion[4].toString(), 20, y);
        y += 40;
        String status = promotion[5].equals(1) ? "Hoạt động" : "Không hoạt động";
        addDetailLabel(pnlDetail, "Trạng Thái:", status, 20, y);

        dialog.add(pnlDetail, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel();
        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dialog.dispose());
        pnlButton.add(btnClose);
        dialog.add(pnlButton, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Khuyến Mãi Mới", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel pnlForm = new JPanel(null);
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtAddID = new JTextField();
        JTextField txtAddName = new JTextField();
        JTextField txtAddDiscount = new JTextField();
        com.toedter.calendar.JDateChooser addStartDate = new com.toedter.calendar.JDateChooser();
        com.toedter.calendar.JDateChooser addEndDate = new com.toedter.calendar.JDateChooser();

        addFormLabel(pnlForm, "Mã KM:", txtAddID, 20, 20);
        addFormLabel(pnlForm, "Tên KM:", txtAddName, 20, 60);
        addFormLabel(pnlForm, "% Giảm (1-100):", txtAddDiscount, 20, 100);
        addFormLabelDate(pnlForm, "Ngày Bắt Đầu:", addStartDate, 20, 140);
        addFormLabelDate(pnlForm, "Ngày Kết Thúc:", addEndDate, 20, 180);

        pnlForm.add(new JLabel("Mã KM:")).setBounds(20, 20, 100, 25);
        pnlForm.add(txtAddID).setBounds(120, 20, 300, 25);

        pnlForm.add(new JLabel("Tên KM:")).setBounds(20, 60, 100, 25);
        pnlForm.add(txtAddName).setBounds(120, 60, 300, 25);

        pnlForm.add(new JLabel("% Giảm (1-100):")).setBounds(20, 100, 100, 25);
        pnlForm.add(txtAddDiscount).setBounds(120, 100, 300, 25);

        pnlForm.add(new JLabel("Ngày Bắt Đầu:")).setBounds(20, 140, 100, 25);
        pnlForm.add(addStartDate).setBounds(120, 140, 300, 25);

        pnlForm.add(new JLabel("Ngày Kết Thúc:")).setBounds(20, 180, 100, 25);
        pnlForm.add(addEndDate).setBounds(120, 180, 300, 25);

        dialog.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel();
        JButton btnSave = new JButton("Thêm");
        JButton btnCancel = new JButton("Hủy");

        btnSave.addActionListener(e -> {
            try {
                String id = txtAddID.getText().trim();
                String name = txtAddName.getText().trim();
                String discountStr = txtAddDiscount.getText().trim();
                java.util.Date startDate = addStartDate.getDate();
                java.util.Date endDate = addEndDate.getDate();

                if (id.isEmpty() || name.isEmpty() || discountStr.isEmpty() || startDate == null || endDate == null) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin");
                    return;
                }

                double discount = Double.parseDouble(discountStr);
                Date sqlStartDate = new Date(startDate.getTime());
                Date sqlEndDate = new Date(endDate.getTime());

                String message = promotionBUS.add(id, name, discount, sqlStartDate, sqlEndDate);
                JOptionPane.showMessageDialog(dialog, message);

                if (message.contains("thành công")) {
                    dialog.dispose();
                    loadData();
                    refreshForm();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Phần trăm giảm giá phải là một số");
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());
        pnlButton.add(btnSave);
        pnlButton.add(btnCancel);
        dialog.add(pnlButton, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showEditDialog() {
        String id = txtID.getText();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần cập nhật");
            return;
        }

        Object[] promotion = promotionBUS.getByID(id);
        if (promotion == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khuyến mãi");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập Nhật Khuyến Mãi", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel pnlForm = new JPanel(null);
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtEditID = new JTextField(promotion[0].toString());
        JTextField txtEditName = new JTextField(promotion[1].toString());
        JTextField txtEditDiscount = new JTextField(promotion[2].toString());
        com.toedter.calendar.JDateChooser editStartDate = new com.toedter.calendar.JDateChooser();
        com.toedter.calendar.JDateChooser editEndDate = new com.toedter.calendar.JDateChooser();

        editStartDate.setDate(new java.util.Date(((Date) promotion[3]).getTime()));
        editEndDate.setDate(new java.util.Date(((Date) promotion[4]).getTime()));

        txtEditID.setEditable(false);
        txtEditID.setBackground(new Color(245, 245, 245));

        pnlForm.add(new JLabel("Mã KM:")).setBounds(20, 20, 100, 25);
        pnlForm.add(txtEditID).setBounds(120, 20, 300, 25);

        pnlForm.add(new JLabel("Tên KM:")).setBounds(20, 60, 100, 25);
        pnlForm.add(txtEditName).setBounds(120, 60, 300, 25);

        pnlForm.add(new JLabel("% Giảm (1-100):")).setBounds(20, 100, 100, 25);
        pnlForm.add(txtEditDiscount).setBounds(120, 100, 300, 25);

        pnlForm.add(new JLabel("Ngày Bắt Đầu:")).setBounds(20, 140, 100, 25);
        pnlForm.add(editStartDate).setBounds(120, 140, 300, 25);

        pnlForm.add(new JLabel("Ngày Kết Thúc:")).setBounds(20, 180, 100, 25);
        pnlForm.add(editEndDate).setBounds(120, 180, 300, 25);

        dialog.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel();
        JButton btnSave = new JButton("Cập Nhật");
        JButton btnCancel = new JButton("Hủy");

        btnSave.addActionListener(e -> {
            try {
                String name = txtEditName.getText().trim();
                String discountStr = txtEditDiscount.getText().trim();
                java.util.Date startDate = editStartDate.getDate();
                java.util.Date endDate = editEndDate.getDate();

                if (name.isEmpty() || discountStr.isEmpty() || startDate == null || endDate == null) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin");
                    return;
                }

                double discount = Double.parseDouble(discountStr);
                Date sqlStartDate = new Date(startDate.getTime());
                Date sqlEndDate = new Date(endDate.getTime());

                String message = promotionBUS.update(id, name, discount, sqlStartDate, sqlEndDate, 1);
                JOptionPane.showMessageDialog(dialog, message);

                if (message.contains("thành công")) {
                    dialog.dispose();
                    loadData();
                    refreshForm();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Phần trăm giảm giá phải là một số");
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());
        pnlButton.add(btnSave);
        pnlButton.add(btnCancel);
        dialog.add(pnlButton, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void styleButton(JButton btn, Color bgColor, Color fgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void addLabelTextField(JPanel p, String label, JTextField t, int x, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(x, y, 110, 25);
        p.add(lbl);
        t.setBounds(x + 110, y, 250, 25);
        p.add(t);
    }

    private void addFormLabel(JPanel p, String label, JTextField t, int x, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(x, y, 100, 25);
        p.add(lbl);
        t.setBounds(x + 100, y, 300, 25);
        p.add(t);
    }

    private void addFormLabelDate(JPanel p, String label, com.toedter.calendar.JDateChooser dc, int x, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(x, y, 100, 25);
        p.add(lbl);
        dc.setBounds(x + 100, y, 300, 25);
        p.add(dc);
    }

    private void addDetailLabel(JPanel p, String label, String value, int x, int y) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setBounds(x, y, 100, 25);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        p.add(lblLabel);

        JLabel lblValue = new JLabel(value);
        lblValue.setBounds(x + 100, y, 250, 25);
        p.add(lblValue);
    }

    private void loadData() {
        fillTable(promotionBUS.getActive());
    }

    private void fillTable(ArrayList<Object[]> list) {
        model.setRowCount(0);
        if (list != null) {
            for (Object[] row : list) {
                Object[] displayRow = new Object[6];
                displayRow[0] = row[0];
                displayRow[1] = row[1];
                displayRow[2] = row[2] + "%";
                displayRow[3] = row[3];
                displayRow[4] = row[4];
                displayRow[5] = row[5].equals(1) ? "Hoạt động" : "Không hoạt động";
                model.addRow(displayRow);
            }
        }
    }

    private void refreshForm() {
        txtID.setText("");
        txtName.setText("");
        txtDiscountPercent.setText("");
        txtSearch.setText("");
        txtID.setEditable(true);
        txtID.setBackground(Color.WHITE);
        tblPromotion.clearSelection();
    }

    private String safeToString(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }
}
=======
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

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm khuyến mãi", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel pnlContent = new JPanel(null);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Dropdown loại
        JLabel lblType = new JLabel("Loại khuyến mãi:");
        lblType.setBounds(20, 20, 120, 25);
        pnlContent.add(lblType);

        JComboBox<String> cbType = new JComboBox<>(new String[]{"Khuyến mãi theo sản phẩm", "Khuyến mãi theo giá tiền"});
        cbType.setBounds(150, 20, 200, 25);
        pnlContent.add(cbType);

        // Fields chung
        JTextField txtID = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtStartDate = new JTextField();
        JTextField txtEndDate = new JTextField();

        addLabelTextField(pnlContent, "Mã KM:", txtID, 20, 60);
        addLabelTextField(pnlContent, "Tên KM:", txtName, 20, 100);
        addLabelTextField(pnlContent, "Ngày bắt đầu (yyyy-MM-dd):", txtStartDate, 20, 140);
        addLabelTextField(pnlContent, "Ngày kết thúc (yyyy-MM-dd):", txtEndDate, 20, 180);

        // Fields động
        JTextField txtProductID = new JTextField();
        JTextField txtDiscountPercent = new JTextField();
        JTextField txtMinAmount = new JTextField();
        JTextField txtMaxDiscount = new JTextField();

        cbType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = (String) cbType.getSelectedItem();
                if ("Khuyến mãi theo sản phẩm".equals(type)) {
                    addLabelTextField(pnlContent, "Mã sản phẩm:", txtProductID, 20, 220);
                    addLabelTextField(pnlContent, "% giảm giá:", txtDiscountPercent, 20, 260);
                    pnlContent.remove(txtMinAmount);
                    pnlContent.remove(txtMaxDiscount);
                } else {
                    addLabelTextField(pnlContent, "Mức áp dụng:", txtMinAmount, 20, 220);
                    addLabelTextField(pnlContent, "Giới hạn giảm:", txtMaxDiscount, 20, 260);
                    pnlContent.remove(txtProductID);
                    pnlContent.remove(txtDiscountPercent);
                }
                pnlContent.revalidate();
                pnlContent.repaint();
            }
        });

        // Trigger initial
        cbType.setSelectedIndex(0);

        JButton btnSave = new JButton("Lưu");
        btnSave.setBounds(200, 320, 80, 30);
        styleButton(btnSave, new Color(40, 167, 69), Color.WHITE);
        pnlContent.add(btnSave);

        btnSave.addActionListener(e -> {
            try {
                String id = txtID.getText().trim();
                String name = txtName.getText().trim();
                String type = cbType.getSelectedItem().equals("Khuyến mãi theo sản phẩm") ? "Product" : "Price";
                Date startDate = Date.valueOf(txtStartDate.getText().trim());
                Date endDate = Date.valueOf(txtEndDate.getText().trim());

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
                JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ: " + ex.getMessage());
            }
        });

        dialog.add(pnlContent, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(String id) {
        Object[] promo = promotionBUS.getByID(id);
        if (promo == null) return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập nhật khuyến mãi", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        JPanel pnlContent = new JPanel(null);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Dropdown loại
        JLabel lblType = new JLabel("Loại khuyến mãi:");
        lblType.setBounds(20, 20, 120, 25);
        pnlContent.add(lblType);

        JComboBox<String> cbType = new JComboBox<>(new String[]{"Khuyến mãi theo sản phẩm", "Khuyến mãi theo giá tiền"});
        cbType.setBounds(150, 20, 200, 25);
        if ("Product".equals(promo[2])) cbType.setSelectedIndex(0); else cbType.setSelectedIndex(1);
        pnlContent.add(cbType);

        // Fields chung
        JTextField txtID = new JTextField((String)promo[0]);
        txtID.setEditable(false);
        JTextField txtName = new JTextField((String)promo[1]);
        JTextField txtStartDate = new JTextField(promo[7].toString());
        JTextField txtEndDate = new JTextField(promo[8].toString());

        addLabelTextField(pnlContent, "Mã KM:", txtID, 20, 60);
        addLabelTextField(pnlContent, "Tên KM:", txtName, 20, 100);
        addLabelTextField(pnlContent, "Ngày bắt đầu (yyyy-MM-dd):", txtStartDate, 20, 140);
        addLabelTextField(pnlContent, "Ngày kết thúc (yyyy-MM-dd):", txtEndDate, 20, 180);

        // Fields động
        JTextField txtProductID = new JTextField(promo[3] != null ? (String)promo[3] : "");
        JTextField txtDiscountPercent = new JTextField(promo[4] != null ? promo[4].toString() : "");
        JTextField txtMinAmount = new JTextField(promo[5] != null ? promo[5].toString() : "");
        JTextField txtMaxDiscount = new JTextField(promo[6] != null ? promo[6].toString() : "");

        cbType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = (String) cbType.getSelectedItem();
                if ("Khuyến mãi theo sản phẩm".equals(type)) {
                    addLabelTextField(pnlContent, "Mã sản phẩm:", txtProductID, 20, 220);
                    addLabelTextField(pnlContent, "% giảm giá:", txtDiscountPercent, 20, 260);
                    pnlContent.remove(txtMinAmount);
                    pnlContent.remove(txtMaxDiscount);
                } else {
                    addLabelTextField(pnlContent, "Mức áp dụng:", txtMinAmount, 20, 220);
                    addLabelTextField(pnlContent, "Giới hạn giảm:", txtMaxDiscount, 20, 260);
                    pnlContent.remove(txtProductID);
                    pnlContent.remove(txtDiscountPercent);
                }
                pnlContent.revalidate();
                pnlContent.repaint();
            }
        });

        // Trigger initial
        cbType.setSelectedIndex(cbType.getSelectedIndex());

        JButton btnSave = new JButton("Lưu");
        btnSave.setBounds(200, 320, 80, 30);
        styleButton(btnSave, new Color(0, 123, 255), Color.WHITE);
        pnlContent.add(btnSave);

        btnSave.addActionListener(e -> {
            try {
                String name = txtName.getText().trim();
                String type = cbType.getSelectedItem().equals("Khuyến mãi theo sản phẩm") ? "Product" : "Price";
                Date startDate = Date.valueOf(txtStartDate.getText().trim());
                Date endDate = Date.valueOf(txtEndDate.getText().trim());

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
                JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ: " + ex.getMessage());
            }
        });

        dialog.add(pnlContent, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void addLabelTextField(JPanel panel, String label, JTextField field, int x, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(x, y, 150, 25);
        field.setBounds(x + 160, y, 150, 25);
        panel.add(lbl);
        panel.add(field);
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }
}
>>>>>>> Stashed changes
