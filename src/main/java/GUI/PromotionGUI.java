package GUI;

import BUS.PromotionBUS;
import com.toedter.calendar.JDateChooser;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class PromotionGUI extends JPanel {
    private PromotionBUS promotionBUS = new PromotionBUS();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

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

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        modelGeneral = new DefaultTableModel(new String[]{"Mã KM", "Tên KM", "Mô tả", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblGeneral = createTable(modelGeneral);
        tabbedPane.addTab("Khuyến mãi chung", new JScrollPane(tblGeneral));

        modelProduct = new DefaultTableModel(new String[]{"Mã KM", "Tên KM", "Mã sản phẩm", "% Giảm", "Mô tả", "Bắt đầu", "Kết thúc", "Trạng thái"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblProduct = createTable(modelProduct);
        tabbedPane.addTab("Khuyến mãi theo sản phẩm", new JScrollPane(tblProduct));

        modelPrice = new DefaultTableModel(new String[]{"Mã KM", "Tên KM", "Điều kiện (>=)", "Giảm trực tiếp", "% Giảm", "Giảm tối đa", "Trạng thái"}, 0) {
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
            String id = (String) row[0];
            String name = (String) row[1];
            String desc = (String) row[2];
            String start = (row[3] != null) ? sdf.format((Timestamp) row[3]) : "";
            String end = (row[4] != null) ? sdf.format((Timestamp) row[4]) : "";
            int statusInt = (int) row[5];
            String statusStr = (statusInt == 1) ? "Đang diễn ra" : "Kết thúc";

            String prodID = (String) row[6];
            Double prodDisc = (Double) row[7];
            Double minInv = (Double) row[8];
            Double discAmt = (Double) row[9];
            Double invDisc = (Double) row[10];
            Double maxDisc = (Double) row[11];

            // Phân loại vào tab
            if (prodID != null && !prodID.isEmpty()) {
                modelProduct.addRow(new Object[]{id, name, prodID, prodDisc + "%", desc, start, end, statusStr});
            } else if ((minInv != null && minInv > 0) || (discAmt != null && discAmt > 0) || (invDisc != null && invDisc > 0)) {
                modelPrice.addRow(new Object[]{id, name, minInv, discAmt, invDisc + "%", maxDisc, statusStr});
            } else {
                modelGeneral.addRow(new Object[]{id, name, desc, start, end, statusStr});
            }
        }
    }

    private void setupEvents() {
        btnRefresh.addActionListener(e -> { txtSearch.setText(""); loadData(); });
        btnSearch.addActionListener(e -> performSearch());
        btnAdd.addActionListener(e -> showAddDialog());
        btnUpdate.addActionListener(e -> {
            JTable table = getCurrentTable();
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) { JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi!"); return; }
            showUpdateDialog((String) table.getValueAt(selectedRow, 0));
        });
        btnDelete.addActionListener(e -> {
            JTable table = getCurrentTable();
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) { JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi!"); return; }
            String id = (String) table.getValueAt(selectedRow, 0);
            if (JOptionPane.showConfirmDialog(this, "Xóa khuyến mãi " + id + "?") == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, promotionBUS.delete(id));
                loadData();
            }
        });
        MouseAdapter doubleClick = new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable t = (JTable) e.getSource();
                    int r = t.getSelectedRow();
                    if (r != -1) showUpdateDialog((String) t.getValueAt(r, 0));
                }
            }
        };
        tblGeneral.addMouseListener(doubleClick);
        tblProduct.addMouseListener(doubleClick);
        tblPrice.addMouseListener(doubleClick);
    }

    private void performSearch() {
        String k = txtSearch.getText().trim();
        modelGeneral.setRowCount(0); modelProduct.setRowCount(0); modelPrice.setRowCount(0);
        ArrayList<Object[]> list = promotionBUS.search(k);
        for (Object[] row : list) {
            String id = (String) row[0]; String name = (String) row[1]; String desc = (String) row[2];
            String start = (row[3] != null) ? sdf.format((Timestamp) row[3]) : "";
            String end = (row[4] != null) ? sdf.format((Timestamp) row[4]) : "";
            String status = ((int)row[5] == 1) ? "Đang diễn ra" : "Kết thúc";
            if (row[6] != null) modelProduct.addRow(new Object[]{id, name, row[6], row[7], desc, start, end, status});
            else if (row[8] != null && (Double)row[8] > 0) modelPrice.addRow(new Object[]{id, name, row[8], row[9], row[10], row[11], status});
            else modelGeneral.addRow(new Object[]{id, name, desc, start, end, status});
        }
    }

    private JTable getCurrentTable() {
        int i = tabbedPane.getSelectedIndex();
        return (i == 0) ? tblGeneral : (i == 1) ? tblProduct : tblPrice;
    }

    private void showAddDialog() {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm khuyến mãi", true);
        d.setLayout(new BorderLayout()); d.setSize(550, 550); d.setLocationRelativeTo(this);
        JPanel p = new JPanel(null); p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> cbType = new JComboBox<>(new String[]{"Khuyến mãi chung", "Khuyến mãi theo sản phẩm", "Khuyến mãi theo giá tiền"});
        cbType.setSelectedIndex(tabbedPane.getSelectedIndex());
        addLabelComponent(p, "Loại KM:", cbType, 20, 20);

        JTextField txtID = new JTextField(); JTextField txtName = new JTextField();
        JTextArea txtDesc = new JTextArea(); JScrollPane spDesc = new JScrollPane(txtDesc);
        JDateChooser dcStart = new JDateChooser(); JDateChooser dcEnd = new JDateChooser();

        addLabelComponent(p, "Mã KM:", txtID, 20, 60); addLabelComponent(p, "Tên KM:", txtName, 20, 100);
        JLabel lblD = new JLabel("Mô tả:"); lblD.setBounds(20, 140, 120, 25); p.add(lblD); spDesc.setBounds(150, 140, 320, 60); p.add(spDesc);
        addLabelDateChooser(p, "Ngày bắt đầu:", dcStart, 20, 210); addLabelDateChooser(p, "Ngày kết thúc:", dcEnd, 20, 250);

        // Sub fields
        JTextField txtProdID = new JTextField(); JTextField txtProdDisc = new JTextField();
        JTextField txtMinInv = new JTextField(); JTextField txtDiscAmt = new JTextField();
        JTextField txtInvDisc = new JTextField(); JTextField txtMaxDisc = new JTextField();
        JLabel l1 = new JLabel(); JLabel l2 = new JLabel(); JLabel l3 = new JLabel(); JLabel l4 = new JLabel();

        cbType.addActionListener(e -> {
            p.remove(l1); p.remove(l2); p.remove(l3); p.remove(l4);
            p.remove(txtProdID); p.remove(txtProdDisc); p.remove(txtMinInv); p.remove(txtDiscAmt); p.remove(txtInvDisc); p.remove(txtMaxDisc);
            String t = (String) cbType.getSelectedItem();
            if (t.contains("sản phẩm")) {
                l1.setText("Mã sản phẩm:"); addLabelComponent(p, "", txtProdID, 150, 290); p.add(l1); l1.setBounds(20, 290, 120, 25);
                l2.setText("% Giảm giá:"); addLabelComponent(p, "", txtProdDisc, 150, 330); p.add(l2); l2.setBounds(20, 330, 120, 25);
            } else if (t.contains("giá tiền")) {
                l1.setText("ĐK Bill >=:"); addLabelComponent(p, "", txtMinInv, 150, 290); p.add(l1); l1.setBounds(20, 290, 120, 25);
                l2.setText("Giảm mặt $(\\u20ab):"); addLabelComponent(p, "", txtDiscAmt, 150, 330); p.add(l2); l2.setBounds(20, 330, 120, 25);
                l3.setText("% Giảm hóa đơn:"); addLabelComponent(p, "", txtInvDisc, 150, 370); p.add(l3); l3.setBounds(20, 370, 120, 25);
                l4.setText("Giảm tối đa (\\u20ab):"); addLabelComponent(p, "", txtMaxDisc, 150, 410); p.add(l4); l4.setBounds(20, 410, 120, 25);
            }
            p.revalidate(); p.repaint();
        });
        cbType.setSelectedIndex(tabbedPane.getSelectedIndex());

        JButton btnSave = new JButton("Lưu Khuyến Mãi"); btnSave.setBounds(175, 450, 200, 40);
        styleButton(btnSave, new Color(40, 167, 69), Color.WHITE); p.add(btnSave);
        btnSave.addActionListener(e -> {
            try {
                String type = cbType.getSelectedIndex() == 0 ? "General" : cbType.getSelectedIndex() == 1 ? "Product" : "Price";
                String pid = txtProdID.getText().trim();
                Double pDisc = txtProdDisc.getText().isEmpty() ? 0.0 : Double.parseDouble(txtProdDisc.getText());
                Double mInv = txtMinInv.getText().isEmpty() ? 0.0 : Double.parseDouble(txtMinInv.getText());
                Double dAmt = txtDiscAmt.getText().isEmpty() ? 0.0 : Double.parseDouble(txtDiscAmt.getText());
                Double iDisc = txtInvDisc.getText().isEmpty() ? 0.0 : Double.parseDouble(txtInvDisc.getText());
                Double mxD = txtMaxDisc.getText().isEmpty() ? 0.0 : Double.parseDouble(txtMaxDisc.getText());

                String res = promotionBUS.add(txtID.getText().trim(), txtName.getText().trim(), dcStart.getDate(), dcEnd.getDate(), txtDesc.getText().trim(), type, pid, pDisc, mInv, dAmt, iDisc, mxD);
                JOptionPane.showMessageDialog(d, res); if (res.equals("Thêm thành công")) { d.dispose(); loadData(); }
            } catch (Exception ex) { JOptionPane.showMessageDialog(d, "Lỗi định dạng số!"); }
        });
        d.add(p); d.setVisible(true);
    }

    private void showUpdateDialog(String id) {
        Object[] pr = promotionBUS.getByID(id); if (pr == null) return;
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập nhật khuyến mãi", true);
        d.setLayout(new BorderLayout()); d.setSize(550, 580); d.setLocationRelativeTo(this);
        JPanel p = new JPanel(null); p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField tid = new JTextField(id); tid.setEditable(false); addLabelComponent(p, "Mã KM:", tid, 20, 20);
        JTextField tnm = new JTextField((String)pr[1]); addLabelComponent(p, "Tên KM:", tnm, 20, 60);
        JTextArea tdc = new JTextArea((String)pr[2]); JScrollPane sp = new JScrollPane(tdc);
        JLabel ld = new JLabel("Mô tả:"); ld.setBounds(20, 100, 120, 25); p.add(ld); sp.setBounds(150, 100, 320, 60); p.add(sp);
        
        JDateChooser ds = new JDateChooser(); ds.setDate((Timestamp)pr[3]); addLabelDateChooser(p, "Ngày BĐ:", ds, 20, 170);
        JDateChooser de = new JDateChooser(); de.setDate((Timestamp)pr[4]); addLabelDateChooser(p, "Ngày KT:", de, 20, 210);

        JComboBox<String> cbst = new JComboBox<>(new String[]{"Kết thúc (0)", "Đang chạy (1)"});
        cbst.setSelectedIndex((int)pr[5]); addLabelComponent(p, "Trạng thái:", cbst, 20, 250);

        String type = (pr[6] != null) ? "Product" : (pr[8] != null && (Double)pr[8] > 0) ? "Price" : "General";
        JTextField tpid = new JTextField(pr[6] != null ? (String)pr[6] : "");
        JTextField tpds = new JTextField(pr[7] != null ? pr[7].toString() : "0");
        JTextField tmin = new JTextField(pr[8] != null ? pr[8].toString() : "0");
        JTextField tdam = new JTextField(pr[9] != null ? pr[9].toString() : "0");
        JTextField tids = new JTextField(pr[10] != null ? pr[10].toString() : "0");
        JTextField tmax = new JTextField(pr[11] != null ? pr[11].toString() : "0");

        if (type.equals("Product")) {
            addLabelComponent(p, "Mã SP:", tpid, 20, 290); addLabelComponent(p, "% Giảm SP:", tpds, 20, 330);
        } else if (type.equals("Price")) {
            addLabelComponent(p, "Điều kiện Bill:", tmin, 20, 290); addLabelComponent(p, "Giảm mặt $(\\u20ab):", tdam, 20, 330);
            addLabelComponent(p, "% Giảm Bill:", tids, 20, 370); addLabelComponent(p, "Giảm tối đa:", tmax, 20, 410);
        }

        JButton bsv = new JButton("Lưu Cập Nhật"); bsv.setBounds(175, 470, 200, 40);
        styleButton(bsv, new Color(0, 123, 255), Color.WHITE); p.add(bsv);
        bsv.addActionListener(e -> {
            try {
                String rs = promotionBUS.update(id, tnm.getText(), ds.getDate(), de.getDate(), tdc.getText(), cbst.getSelectedIndex(), type, tpid.getText(), Double.parseDouble(tpds.getText()), Double.parseDouble(tmin.getText()), Double.parseDouble(tdam.getText()), Double.parseDouble(tids.getText()), Double.parseDouble(tmax.getText()));
                JOptionPane.showMessageDialog(d, rs); if (rs.equals("Cập nhật thành công")) { d.dispose(); loadData(); }
            } catch (Exception ex) { JOptionPane.showMessageDialog(d, "Lỗi số!"); }
        });
        d.add(p); d.setVisible(true);
    }

    private void addLabelComponent(JPanel p, String text, JComponent c, int x, int y) {
        if (!text.isEmpty()) { JLabel l = new JLabel(text); l.setBounds(x, y, 120, 25); p.add(l); }
        c.setBounds(x == 150 ? x : x + 130, y, 320, 25); p.add(c);
    }

    private void addLabelDateChooser(JPanel p, String text, JDateChooser d, int x, int y) {
        JLabel l = new JLabel(text); l.setBounds(x, y, 120, 25); p.add(l);
        d.setBounds(x + 130, y, 320, 25); d.setDateFormatString("dd/MM/yyyy HH:mm"); p.add(d);
    }

    private void styleButton(JButton b, Color bg, Color fg) {
        b.setBackground(bg); b.setForeground(fg); b.setFocusPainted(false); b.setFont(new Font("Segoe UI", Font.BOLD, 12)); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
