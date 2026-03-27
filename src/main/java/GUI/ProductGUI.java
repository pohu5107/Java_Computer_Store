package GUI;

import BUS.ProductBUS;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

public class ProductGUI extends JPanel {
    private ProductBUS productBUS = new ProductBUS();
    private DecimalFormat df = new DecimalFormat("#,###");

    private JTable tblProduct;
    private DefaultTableModel model;
    private JTextField txtID, txtName, txtQty, txtPrice, txtUnit, txtCatID, txtBrandID;
    private JTextField txtCPU, txtRAM, txtVGA, txtMainboard, txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnSearch;

    public ProductGUI() {
        setLayout(new BorderLayout(0, 10)); 
        setPreferredSize(new Dimension(950, 650)); 
        setBackground(new Color(240, 242, 245));

        initComponents();
        loadData();
        setupEvents();
    }

    private void initComponents() {
        JPanel pnlNorth = new JPanel(null);
        pnlNorth.setPreferredSize(new Dimension(950, 280));
        pnlNorth.setOpaque(false);

        JPanel pnlInput = new JPanel(null);
        pnlInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Thông tin chi tiết sản phẩm"));
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBounds(20, 10, 910, 210);
        
        addLabelTextField(pnlInput, "Mã SP:", txtID = new JTextField(), 20, 30);
        addLabelTextField(pnlInput, "Tên SP:", txtName = new JTextField(), 20, 70);
        addLabelTextField(pnlInput, "Số lượng:", txtQty = new JTextField("0"), 20, 110);
        txtQty.setEditable(false);
        txtQty.setBackground(new Color(245, 245, 245));
        addLabelTextField(pnlInput, "Đơn giá:", txtPrice = new JTextField(), 20, 150);

        addLabelTextField(pnlInput, "Đơn vị:", txtUnit = new JTextField(), 320, 30);
        addLabelTextField(pnlInput, "Mã Loại:", txtCatID = new JTextField(), 320, 70);
        addLabelTextField(pnlInput, "Mã Hiệu:", txtBrandID = new JTextField(), 320, 110);
        addLabelTextField(pnlInput, "Mainboard:", txtMainboard = new JTextField(), 320, 150);

        addLabelTextField(pnlInput, "CPU:", txtCPU = new JTextField(), 620, 30);
        addLabelTextField(pnlInput, "RAM:", txtRAM = new JTextField(), 620, 70);
        addLabelTextField(pnlInput, "VGA:", txtVGA = new JTextField(), 620, 110);
        pnlNorth.add(pnlInput);

        JLabel lblS = new JLabel("Tìm kiếm:");
        lblS.setBounds(30, 235, 70, 25);
        pnlNorth.add(lblS);

        txtSearch = new JTextField();
        txtSearch.setBounds(100, 235, 200, 25);
        pnlNorth.add(txtSearch);

        btnSearch = new JButton("Tìm Kiếm");
        btnSearch.setBounds(310, 235, 90, 25);
        styleButton(btnSearch, new Color(240, 240, 240), Color.BLACK);
        pnlNorth.add(btnSearch);

        btnAdd = new JButton("Thêm Mới");
        btnAdd.setBounds(435, 230, 110, 35);
        styleButton(btnAdd, new Color(40, 167, 69), Color.WHITE);

        btnUpdate = new JButton("Cập Nhật");
        btnUpdate.setBounds(555, 230, 110, 35);
        styleButton(btnUpdate, new Color(0, 123, 255), Color.WHITE);

        btnDelete = new JButton("Xóa SP");
        btnDelete.setBounds(675, 230, 110, 35);
        styleButton(btnDelete, new Color(220, 53, 69), Color.WHITE);

        btnRefresh = new JButton("Làm Mới");
        btnRefresh.setBounds(795, 230, 110, 35);
        styleButton(btnRefresh, new Color(108, 117, 125), Color.WHITE);

        pnlNorth.add(btnAdd); pnlNorth.add(btnUpdate); pnlNorth.add(btnDelete); pnlNorth.add(btnRefresh);
        add(pnlNorth, BorderLayout.NORTH);

        String[] columns = {"Mã SP", "Tên SP", "SL", "Giá", "Đơn vị", "Loại", "Hiệu", "CPU", "RAM", "VGA", "Main"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblProduct = new JTable(model);
        tblProduct.setRowHeight(30);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblProduct.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        JScrollPane scroll = new JScrollPane(tblProduct);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        add(scroll, BorderLayout.CENTER);
    }

    private void setupEvents() {
        tblProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblProduct.getSelectedRow();
                if (row != -1) {
                    txtID.setText(safeToString(model.getValueAt(row, 0)));
                    txtName.setText(safeToString(model.getValueAt(row, 1)));
                    txtQty.setText(safeToString(model.getValueAt(row, 2)));
                    String priceStr = safeToString(model.getValueAt(row, 3)).replace(",", "");
                    txtPrice.setText(priceStr);
                    txtUnit.setText(safeToString(model.getValueAt(row, 4)));
                    txtCatID.setText(safeToString(model.getValueAt(row, 5)));
                    txtBrandID.setText(safeToString(model.getValueAt(row, 6)));
                    txtCPU.setText(safeToString(model.getValueAt(row, 7)));
                    txtRAM.setText(safeToString(model.getValueAt(row, 8)));
                    txtVGA.setText(safeToString(model.getValueAt(row, 9)));
                    txtMainboard.setText(safeToString(model.getValueAt(row, 10)));
                    txtID.setEditable(false);
                    txtID.setBackground(new Color(245, 245, 245));
                }
            }
        });

        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) loadData();
            else if (keyword.matches("\\d+") || keyword.startsWith(">") || keyword.startsWith("<") || keyword.startsWith("=")) {
                fillTable(productBUS.searchByPrice(keyword.matches("\\d+") ? "=" + keyword : keyword));
            } else {
                fillTable(productBUS.search(keyword));
            }
        }); 

        btnAdd.addActionListener(e -> {
            try {
                String res = productBUS.add(txtID.getText(), txtName.getText(), 
                    Double.parseDouble(txtPrice.getText()), 
                    txtUnit.getText(), txtCatID.getText(), txtBrandID.getText(), 
                    txtCPU.getText(), txtRAM.getText(), txtVGA.getText(), txtMainboard.getText());
                JOptionPane.showMessageDialog(this, res);
                loadData();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi định dạng Đơn giá!"); }
        });

        btnUpdate.addActionListener(e -> {
            try {
                String res = productBUS.update(txtID.getText(), txtName.getText(), 
                        Double.parseDouble(txtPrice.getText()), 
                        txtUnit.getText(), txtCatID.getText(), txtBrandID.getText(), 
                        txtCPU.getText(), txtRAM.getText(), txtVGA.getText(), txtMainboard.getText());
                JOptionPane.showMessageDialog(this, res);
                loadData();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi định dạng số!"); }
        });

        btnDelete.addActionListener(e -> {
            if (txtID.getText().isEmpty()) return;
            if (JOptionPane.showConfirmDialog(this, "Xóa sản phẩm này?", "Xác nhận", 0) == 0) {
                JOptionPane.showMessageDialog(this, productBUS.delete(txtID.getText()));
                refreshForm();
                loadData();
            }
        });

        btnRefresh.addActionListener(e -> { refreshForm(); loadData(); });
    }

    private void styleButton(JButton btn, Color bgColor, Color fgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
    }

    private void addLabelTextField(JPanel p, String label, JTextField t, int x, int y) {
        JLabel lbl = new JLabel(label); lbl.setBounds(x, y, 70, 25); p.add(lbl);
        t.setBounds(x + 75, y, 190, 25); p.add(t);
    }

    private void fillTable(ArrayList<Object[]> list) {
        model.setRowCount(0);
        if (list != null) {
            for (Object[] row : list) {
                Object[] displayRow = row.clone();
                try { displayRow[3] = df.format(Double.parseDouble(row[3].toString())); } catch (Exception e) {}
                model.addRow(displayRow);
            }
        }
    }

    private void loadData() { fillTable(productBUS.getAll()); }

    private void refreshForm() {
        txtID.setText(""); txtName.setText(""); txtQty.setText("0"); txtPrice.setText("");
        txtUnit.setText(""); txtCatID.setText(""); txtBrandID.setText("");
        txtCPU.setText(""); txtRAM.setText(""); txtVGA.setText(""); txtMainboard.setText("");
        txtID.setEditable(true); txtID.setBackground(Color.WHITE);
    }

    private String safeToString(Object obj) { return (obj == null) ? "" : obj.toString(); }
}