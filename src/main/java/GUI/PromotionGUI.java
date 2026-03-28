package GUI;

import BUS.PromotionBUS;
import com.toedter.calendar.JDateChooser;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class PromotionGUI extends JPanel {
    private PromotionBUS promotionBUS = new PromotionBUS();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private JTabbedPane tabbedPane;
    private JTable tblGeneral, tblProduct, tblPrice;
    private DefaultTableModel modelGeneral, modelProduct, modelPrice;
    private JTextField txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnSearch;

    public PromotionGUI() {
        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(950, 650));
        setBackground(new Color(240, 242, 245));

        initComponents();
        loadData();
        setupEvents();
    }

    private void initComponents() {
        // TOP PANEL: Search and Common Actions
        JPanel pnlNorth = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlNorth.setOpaque(false);

        pnlNorth.add(new JLabel("Tìm kiếm:"));
        txtSearch = new JTextField(20);
        pnlNorth.add(txtSearch);

        btnSearch = new JButton("Tìm Kiếm");
        styleButton(btnSearch, new Color(240, 240, 240), Color.BLACK);
        pnlNorth.add(btnSearch);

        btnAdd = new JButton("Thêm Mới");
        btnUpdate = new JButton("Cập Nhật");
        btnDelete = new JButton("Xóa KM");
        btnRefresh = new JButton("Làm Mới");

        pnlNorth.add(btnAdd);
        pnlNorth.add(btnUpdate);
        pnlNorth.add(btnDelete);
        pnlNorth.add(btnRefresh);

        add(pnlNorth, BorderLayout.NORTH);

        // TABBED PANE
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Tab 1: Khuyến mãi chung
        modelGeneral = new DefaultTableModel(new String[]{"Mã KM", "Tên KM", "Mô tả", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblGeneral = createTable(modelGeneral);
        tabbedPane.addTab("Khuyến mãi chung", new JScrollPane(tblGeneral));

        // Tab 2: Khuyến mãi theo sản phẩm
        modelProduct = new DefaultTableModel(new String[]{"Mã KM", "Tên KM", "Mã sản phẩm", "Mô tả", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblProduct = createTable(modelProduct);
        tabbedPane.addTab("Khuyến mãi theo sản phẩm", new JScrollPane(tblProduct));

        // Tab 3: Khuyến mãi theo giá tiền
        modelPrice = new DefaultTableModel(new String[]{"Mã KM", "Tên KM", "Điều kiện (>=)", "% Giảm", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblPrice = createTable(modelPrice);
        tabbedPane.addTab("Khuyến mãi theo giá tiền", new JScrollPane(tblPrice));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        return table;
    }

    private void loadData() {
        modelGeneral.setRowCount(0);
        modelProduct.setRowCount(0);
        modelPrice.setRowCount(0);

        ArrayList<Object[]> list = promotionBUS.getAll();
        for (Object[] row : list) {
            String type = (String) row[2];
            String startDate = (row[7] != null) ? sdf.format((Date) row[7]) : "";
            String endDate = (row[8] != null) ? sdf.format((Date) row[8]) : "";

            if ("General".equals(type)) {
                modelGeneral.addRow(new Object[]{row[0], row[1], row[10], startDate, endDate, row[9]});
            } else if ("Product".equals(type)) {
                modelProduct.addRow(new Object[]{row[0], row[1], row[3], row[10], startDate, endDate, row[9]});
            } else if ("Price".equals(type)) {
                modelPrice.addRow(new Object[]{row[0], row[1], row[5], row[4], startDate, endDate, row[9]});
            }
        }
    }

    private void setupEvents() {
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadData();
        });

        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            modelGeneral.setRowCount(0);
            modelProduct.setRowCount(0);
            modelPrice.setRowCount(0);

            ArrayList<Object[]> list = promotionBUS.search(keyword);
            for (Object[] row : list) {
                String type = (String) row[2];
                String startDate = (row[7] != null) ? sdf.format((Date) row[7]) : "";
                String endDate = (row[8] != null) ? sdf.format((Date) row[8]) : "";

                if ("General".equals(type)) {
                    modelGeneral.addRow(new Object[]{row[0], row[1], row[10], startDate, endDate, row[9]});
                } else if ("Product".equals(type)) {
                    modelProduct.addRow(new Object[]{row[0], row[1], row[3], row[10], startDate, endDate, row[9]});
                } else if ("Price".equals(type)) {
                    modelPrice.addRow(new Object[]{row[0], row[1], row[5], row[4], startDate, endDate, row[9]});
                }
            }
        });

        btnAdd.addActionListener(e -> showAddDialog());

        btnUpdate.addActionListener(e -> {
            JTable currentTable = getCurrentTable();
            int selectedRow = currentTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một khuyến mãi để cập nhật!");
                return;
            }
            String id = (String) currentTable.getValueAt(selectedRow, 0);
            showUpdateDialog(id);
        });

        btnDelete.addActionListener(e -> {
            JTable currentTable = getCurrentTable();
            int selectedRow = currentTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một khuyến mãi để xóa!");
                return;
            }
            String id = (String) currentTable.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khuyến mãi " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String result = promotionBUS.delete(id);
                JOptionPane.showMessageDialog(this, result);
                loadData();
            }
        });

        MouseAdapter doubleClickEvent = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable table = (JTable) e.getSource();
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String id = (String) table.getValueAt(selectedRow, 0);
                        showUpdateDialog(id);
                    }
                }
            }
        };
        tblGeneral.addMouseListener(doubleClickEvent);
        tblProduct.addMouseListener(doubleClickEvent);
        tblPrice.addMouseListener(doubleClickEvent);
    }

    private JTable getCurrentTable() {
        int index = tabbedPane.getSelectedIndex();
        if (index == 0) return tblGeneral;
        if (index == 1) return tblProduct;
        return tblPrice;
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm khuyến mãi mới", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(550, 500);
        dialog.setLocationRelativeTo(this);

        JPanel pnlContent = new JPanel(null);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblType = new JLabel("Loại khuyến mãi:");
        lblType.setBounds(20, 20, 120, 25);
        pnlContent.add(lblType);

        JComboBox<String> cbType = new JComboBox<>(new String[]{"Khuyến mãi chung", "Khuyến mãi theo sản phẩm", "Khuyến mãi theo giá tiền"});
        cbType.setBounds(150, 20, 250, 25);
        cbType.setSelectedIndex(tabbedPane.getSelectedIndex());
        pnlContent.add(cbType);

        JTextField txtID = new JTextField();
        JTextField txtName = new JTextField();
        JTextArea txtDesc = new JTextArea();
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDesc);
        
        JDateChooser txtStartDate = new JDateChooser();
        JDateChooser txtEndDate = new JDateChooser();

        addLabelComponent(pnlContent, "Mã KM:", txtID, 20, 60);
        addLabelComponent(pnlContent, "Tên KM:", txtName, 20, 100);
        
        JLabel lblDesc = new JLabel("Mô tả:");
        lblDesc.setBounds(20, 140, 120, 25);
        pnlContent.add(lblDesc);
        scrollDesc.setBounds(150, 140, 250, 60);
        pnlContent.add(scrollDesc);

        addLabelDateChooser(pnlContent, "Ngày bắt đầu:", txtStartDate, 20, 210);
        addLabelDateChooser(pnlContent, "Ngày kết thúc:", txtEndDate, 20, 250);

        // Dynamic fields
        JTextField txtProductID = new JTextField();
        JTextField txtDiscountPercent = new JTextField();
        JTextField txtMinAmount = new JTextField();

        JLabel lblDynamic1 = new JLabel();
        JLabel lblDynamic2 = new JLabel();
        lblDynamic1.setBounds(20, 290, 120, 25);
        lblDynamic2.setBounds(20, 330, 120, 25);
        txtProductID.setBounds(150, 290, 250, 25);
        txtDiscountPercent.setBounds(150, 330, 250, 25);
        txtMinAmount.setBounds(150, 290, 250, 25);

        cbType.addActionListener(e -> {
            String selection = (String) cbType.getSelectedItem();
            pnlContent.remove(lblDynamic1); pnlContent.remove(lblDynamic2);
            pnlContent.remove(txtProductID); pnlContent.remove(txtDiscountPercent); pnlContent.remove(txtMinAmount);

            if ("Khuyến mãi chung".equals(selection)) {
                // No extra fields
            } else if ("Khuyến mãi theo sản phẩm".equals(selection)) {
                lblDynamic1.setText("Mã sản phẩm:");
                lblDynamic2.setText("% giảm giá:");
                pnlContent.add(lblDynamic1); pnlContent.add(lblDynamic2);
                pnlContent.add(txtProductID); pnlContent.add(txtDiscountPercent);
            } else {
                lblDynamic1.setText("Điều kiện (>=):");
                lblDynamic2.setText("% giảm giá:");
                pnlContent.add(lblDynamic1); pnlContent.add(lblDynamic2);
                pnlContent.add(txtMinAmount); pnlContent.add(txtDiscountPercent);
            }
            pnlContent.revalidate();
            pnlContent.repaint();
        });

        cbType.setSelectedIndex(cbType.getSelectedIndex()); // Trigger initial

        JButton btnSave = new JButton("Thêm Thành Công");
        btnSave.setBounds(175, 380, 180, 40);
        styleButton(btnSave, new Color(40, 167, 69), Color.WHITE);
        pnlContent.add(btnSave);

        btnSave.addActionListener(e -> {
            try {
                String id = txtID.getText().trim();
                String name = txtName.getText().trim();
                String desc = txtDesc.getText().trim();
                String typeStr = (String) cbType.getSelectedItem();
                String type = "General";
                if (typeStr.contains("sản phẩm")) type = "Product";
                else if (typeStr.contains("giá tiền")) type = "Price";

                if (txtStartDate.getDate() == null || txtEndDate.getDate() == null) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ngày tháng!");
                    return;
                }

                Date start = new Date(txtStartDate.getDate().getTime());
                Date end = new Date(txtEndDate.getDate().getTime());

                String prodID = null;
                Double disc = null;
                Double min = null;

                if ("Product".equals(type)) {
                    prodID = txtProductID.getText().trim();
                    disc = Double.parseDouble(txtDiscountPercent.getText().trim());
                } else if ("Price".equals(type)) {
                    min = Double.parseDouble(txtMinAmount.getText().trim());
                    disc = Double.parseDouble(txtDiscountPercent.getText().trim());
                }

                String res = promotionBUS.add(id, name, type, prodID, disc, min, 0.0, start, end, desc);
                JOptionPane.showMessageDialog(dialog, res);
                if (res.equals("Thêm thành công")) {
                    dialog.dispose();
                    loadData();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Dữ liệu số không hợp lệ!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        dialog.add(pnlContent);
        dialog.setVisible(true);
    }

    private void showUpdateDialog(String id) {
        Object[] promo = promotionBUS.getByID(id);
        if (promo == null) return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập nhật khuyến mãi", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(550, 500);
        dialog.setLocationRelativeTo(this);

        JPanel pnlContent = new JPanel(null);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblType = new JLabel("Loại khuyến mãi:");
        lblType.setBounds(20, 20, 120, 25);
        pnlContent.add(lblType);

        String typeEnum = (String) promo[2];
        String typeLabel = "Khuyến mãi chung";
        if ("Product".equals(typeEnum)) typeLabel = "Khuyến mãi theo sản phẩm";
        else if ("Price".equals(typeEnum)) typeLabel = "Khuyến mãi theo giá tiền";

        JTextField txtTypeDisplay = new JTextField(typeLabel);
        txtTypeDisplay.setBounds(150, 20, 250, 25);
        txtTypeDisplay.setEditable(false);
        pnlContent.add(txtTypeDisplay);

        JTextField txtID = new JTextField((String) promo[0]);
        txtID.setEditable(false);
        JTextField txtName = new JTextField((String) promo[1]);
        JTextArea txtDesc = new JTextArea((String) promo[10]);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDesc);
        
        JDateChooser txtStartDate = new JDateChooser();
        txtStartDate.setDate((Date) promo[7]);
        JDateChooser txtEndDate = new JDateChooser();
        txtEndDate.setDate((Date) promo[8]);

        addLabelComponent(pnlContent, "Mã KM:", txtID, 20, 60);
        addLabelComponent(pnlContent, "Tên KM:", txtName, 20, 100);
        
        JLabel lblDesc = new JLabel("Mô tả:");
        lblDesc.setBounds(20, 140, 120, 25);
        pnlContent.add(lblDesc);
        scrollDesc.setBounds(150, 140, 250, 60);
        pnlContent.add(scrollDesc);

        addLabelDateChooser(pnlContent, "Ngày bắt đầu:", txtStartDate, 20, 210);
        addLabelDateChooser(pnlContent, "Ngày kết thúc:", txtEndDate, 20, 250);

        JTextField txtDynamic1 = new JTextField();
        JTextField txtDynamic2 = new JTextField();
        JLabel lblDynamic1 = new JLabel();
        JLabel lblDynamic2 = new JLabel();
        lblDynamic1.setBounds(20, 290, 120, 25);
        lblDynamic2.setBounds(20, 330, 120, 25);
        txtDynamic1.setBounds(150, 290, 250, 25);
        txtDynamic2.setBounds(150, 330, 250, 25);

        if ("Product".equals(typeEnum)) {
            lblDynamic1.setText("Mã sản phẩm:");
            txtDynamic1.setText((String) promo[3]);
            lblDynamic2.setText("% giảm giá:");
            txtDynamic2.setText(promo[4].toString());
            pnlContent.add(lblDynamic1); pnlContent.add(lblDynamic2);
            pnlContent.add(txtDynamic1); pnlContent.add(txtDynamic2);
        } else if ("Price".equals(typeEnum)) {
            lblDynamic1.setText("Điều kiện (>=):");
            txtDynamic1.setText(promo[5].toString());
            lblDynamic2.setText("% giảm giá:");
            txtDynamic2.setText(promo[4].toString());
            pnlContent.add(lblDynamic1); pnlContent.add(lblDynamic2);
            pnlContent.add(txtDynamic1); pnlContent.add(txtDynamic2);
        }

        JButton btnSave = new JButton("Lưu Cập Nhật");
        btnSave.setBounds(175, 380, 180, 40);
        styleButton(btnSave, new Color(0, 123, 255), Color.WHITE);
        pnlContent.add(btnSave);

        btnSave.addActionListener(e -> {
            try {
                String name = txtName.getText().trim();
                String desc = txtDesc.getText().trim();
                
                if (txtStartDate.getDate() == null || txtEndDate.getDate() == null) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ngày tháng!");
                    return;
                }

                Date start = new Date(txtStartDate.getDate().getTime());
                Date end = new Date(txtEndDate.getDate().getTime());

                String prodID = null;
                Double disc = null;
                Double min = null;

                if ("Product".equals(typeEnum)) {
                    prodID = txtDynamic1.getText().trim();
                    disc = Double.parseDouble(txtDynamic2.getText().trim());
                } else if ("Price".equals(typeEnum)) {
                    min = Double.parseDouble(txtDynamic1.getText().trim());
                    disc = Double.parseDouble(txtDynamic2.getText().trim());
                }

                String res = promotionBUS.update(id, name, typeEnum, prodID, disc, min, 0.0, start, end, desc);
                JOptionPane.showMessageDialog(dialog, res);
                if (res.equals("Cập nhật thành công")) {
                    dialog.dispose();
                    loadData();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ!");
            }
        });

        dialog.add(pnlContent);
        dialog.setVisible(true);
    }

    private void addLabelComponent(JPanel p, String text, JComponent c, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 120, 25);
        c.setBounds(x + 130, y, 250, 25);
        p.add(l);
        p.add(c);
    }

    private void addLabelDateChooser(JPanel p, String text, JDateChooser dc, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 120, 25);
        dc.setBounds(x + 130, y, 250, 25);
        dc.setDateFormatString("dd/MM/yyyy");
        p.add(l);
        p.add(dc);
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}