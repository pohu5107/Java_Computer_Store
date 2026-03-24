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

public class ProductGUI extends JFrame {
    private ProductBUS productBUS = new ProductBUS();
    private DecimalFormat df = new DecimalFormat("#,###");

    private JTable tblProduct;
    private DefaultTableModel model;
    private JTextField txtID, txtName, txtQty, txtPrice, txtUnit, txtCatID, txtBrandID;
    private JTextField txtCPU, txtRAM, txtVGA, txtMainboard, txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnSearch;

    public ProductGUI() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Quản Lý Sản Phẩm - Hệ Thống Máy Tính");
        setSize(1100, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JPanel pnlInput = new JPanel(null);
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));
        pnlInput.setBounds(20, 20, 1040, 220);
        add(pnlInput);

        addLabelTextField(pnlInput, "Mã SP:", txtID = new JTextField(), 20, 30);
        addLabelTextField(pnlInput, "Tên SP:", txtName = new JTextField(), 20, 70);
        
        addLabelTextField(pnlInput, "Số lượng:", txtQty = new JTextField("0"), 20, 110);
        txtQty.setEditable(false);
        txtQty.setBackground(new Color(240, 240, 240)); 
        txtQty.setToolTipText("Số lượng chỉ thay đổi khi Nhập hàng hoặc Bán hàng");

        addLabelTextField(pnlInput, "Đơn giá:", txtPrice = new JTextField(), 20, 150);
        addLabelTextField(pnlInput, "Đơn vị:", txtUnit = new JTextField(), 360, 30);
        addLabelTextField(pnlInput, "Mã Loại:", txtCatID = new JTextField(), 360, 70);
        addLabelTextField(pnlInput, "Mã Hiệu:", txtBrandID = new JTextField(), 360, 110);
        addLabelTextField(pnlInput, "Mainboard:", txtMainboard = new JTextField(), 360, 150);
        addLabelTextField(pnlInput, "CPU:", txtCPU = new JTextField(), 700, 30);
        addLabelTextField(pnlInput, "RAM:", txtRAM = new JTextField(), 700, 70);
        addLabelTextField(pnlInput, "VGA:", txtVGA = new JTextField(), 700, 110);

        // --- TÌM KIẾM ---
        JLabel lblS = new JLabel("Tìm kiếm:");
        lblS.setBounds(20, 255, 70, 25);
        add(lblS);

        txtSearch = new JTextField();
        txtSearch.setToolTipText("Nhập tên, id hoặc giá (Ví dụ: 15000000 hoặc >5000000)");
        txtSearch.setBounds(90, 255, 250, 25);
        add(txtSearch);

        btnSearch = new JButton("Tìm Kiếm");
        btnSearch.setBounds(350, 255, 90, 25);
        add(btnSearch);

        // --- NÚT BẤM ---
        btnAdd = new JButton("Thêm Mới");
        btnAdd.setBounds(460, 250, 110, 35);
        styleButton(btnAdd, new Color(40, 167, 69));

        btnUpdate = new JButton("Cập Nhật");
        btnUpdate.setBounds(580, 250, 110, 35);
        styleButton(btnUpdate, new Color(0, 123, 255));

        btnDelete = new JButton("Xóa SP");
        btnDelete.setBounds(700, 250, 110, 35);
        styleButton(btnDelete, new Color(220, 53, 69));

        btnRefresh = new JButton("Làm Mới");
        btnRefresh.setBounds(820, 250, 110, 35);
        styleButton(btnRefresh, new Color(108, 117, 125));
        
        add(btnAdd); add(btnUpdate); add(btnDelete); add(btnRefresh);

        String[] columns = {"Mã SP", "Tên SP", "SL", "Giá", "Đơn vị", "Loại", "Hiệu", "CPU", "RAM", "VGA", "Main"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblProduct = new JTable(model);
        tblProduct.setRowHeight(25);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblProduct.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        JScrollPane scroll = new JScrollPane(tblProduct);
        scroll.setBounds(20, 300, 1040, 330);
        add(scroll);

        // --- EVENTS ---

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
                }
            }
        });

        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                loadData();
            } else if (keyword.matches("\\d+")) {
                fillTable(productBUS.searchByPrice("=" + keyword));
            } else if (keyword.startsWith(">") || keyword.startsWith("<") || keyword.startsWith("=")) {
                fillTable(productBUS.searchByPrice(keyword));
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
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số cho Đơn giá!"); }
        });

        btnUpdate.addActionListener(e -> {
            try {
                String res = productBUS.update(txtID.getText(), txtName.getText(), 
                        Double.parseDouble(txtPrice.getText()), 
                        txtUnit.getText(), txtCatID.getText(), txtBrandID.getText(), 
                        txtCPU.getText(), txtRAM.getText(), txtVGA.getText(), txtMainboard.getText());
                JOptionPane.showMessageDialog(this, res);
                loadData();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số!"); }
        });

        btnDelete.addActionListener(e -> {
            if (txtID.getText().isEmpty()) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, productBUS.delete(txtID.getText()));
                refreshForm();
                loadData();
            }
        });

        btnRefresh.addActionListener(e -> { refreshForm(); loadData(); });
    }

    private String safeToString(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
    }

    private void addLabelTextField(JPanel p, String label, JTextField t, int x, int y) {
        JLabel lbl = new JLabel(label); lbl.setBounds(x, y, 80, 25); p.add(lbl);
        t.setBounds(x + 80, y, 180, 25); p.add(t);
    }

    private void fillTable(ArrayList<Object[]> list) {
        model.setRowCount(0);
        if (list != null) {
            for (Object[] row : list) {
                Object[] displayRow = row.clone();
                try {
                    displayRow[3] = df.format(Double.parseDouble(row[3].toString()));
                } catch (Exception e) {}
                model.addRow(displayRow);
            }
        }
    }

    private void loadData() { fillTable(productBUS.getAll()); }

    private void refreshForm() {
        txtID.setText(""); txtName.setText(""); 
        txtQty.setText("0");
        txtPrice.setText("");
        txtUnit.setText(""); txtCatID.setText(""); txtBrandID.setText("");
        txtCPU.setText(""); txtRAM.setText(""); txtVGA.setText(""); txtMainboard.setText("");
        txtID.setEditable(true);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e) {}
        new ProductGUI().setVisible(true);
    }
}