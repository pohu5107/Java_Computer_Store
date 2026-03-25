package GUI;

import BUS.PromotionBUS;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;

public class PromotionGUI extends JPanel {
    private final PromotionBUS promotionBUS = new PromotionBUS();

    private JTable tblPromotion;
    private DefaultTableModel model;
    private JTextField txtID, txtName, txtDiscountPercent, txtSearch;
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
                    }
                }
            }
        });

        btnAdd.addActionListener(e -> showAddDialog());

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
                loadData();
            }
        });

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
