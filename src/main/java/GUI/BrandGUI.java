package GUI;

import BUS.BrandBUS;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BrandGUI extends JPanel {
    private BrandBUS brandBUS = new BrandBUS();
    private JTable tblBrand;
    private DefaultTableModel model;
    private JTextField txtID, txtName, txtAddress, txtPhone, txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnSearch;

    public BrandGUI() {
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
        pnlInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Thông tin thương hiệu"));
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBounds(20, 10, 895, 130);
        
        addLabelTextField(pnlInput, "Mã Thương Hiệu:", txtID = new JTextField(), 20, 35);
        addLabelTextField(pnlInput, "Tên Thương Hiệu:", txtName = new JTextField(), 20, 75);
        addLabelTextField(pnlInput, "Địa Chỉ:", txtAddress = new JTextField(), 450, 35);
        addLabelTextField(pnlInput, "Số ĐT:", txtPhone = new JTextField(), 450, 75);
        pnlNorth.add(pnlInput);

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

        pnlNorth.add(btnAdd); pnlNorth.add(btnUpdate); pnlNorth.add(btnDelete); pnlNorth.add(btnRefresh);
        add(pnlNorth, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        pnlCenter.setOpaque(false);

        String[] columns = {"Mã Thương Hiệu", "Tên Thương Hiệu", "Địa Chỉ", "Số Điện Thoại"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblBrand = new JTable(model);
        tblBrand.setRowHeight(30);
        pnlCenter.add(new JScrollPane(tblBrand), BorderLayout.CENTER);
        
        add(pnlCenter, BorderLayout.CENTER);
    }

    private void setupEvents() {
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
            if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0) {
                JOptionPane.showMessageDialog(this, brandBUS.delete(id));
                refreshForm();
                loadData();
            }
        });

        btnSearch.addActionListener(e -> fillTable(brandBUS.search(txtSearch.getText().trim())));
        btnRefresh.addActionListener(e -> { refreshForm(); loadData(); });
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
}