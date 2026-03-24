/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BUS.PurchaseOrderBUS;
import BUS.ProductBUS;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PurchaseOrderGUI extends JFrame {

    private PurchaseOrderBUS orderBUS = new PurchaseOrderBUS();
    private DecimalFormat df = new DecimalFormat("#,###");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private JTable tblOrder, tblDetails;
    private DefaultTableModel modelOrder, modelDetails;
    private JButton btnRefresh, btnCreate;

    public PurchaseOrderGUI() {
        initComponents();
        loadOrderData();
    }

    private void initComponents() {
        setTitle("Quản Lý Nhập Hàng (Purchase Orders)");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // PHẦN TRÊN: HEADER & NÚT BẤM
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        
        JLabel lblTitle = new JLabel("LỊCH SỬ PHIẾU NHẬP KHO");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlTop.add(lblTitle);

        btnRefresh = new JButton("Làm Mới Dữ Liệu");
        btnCreate = new JButton("+ TẠO PHIẾU NHẬP MỚI");
        btnCreate.setBackground(new Color(34, 139, 34));
        btnRefresh.setBackground(new Color(34, 139, 34));
        btnCreate.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 12));

        pnlTop.add(Box.createHorizontalStrut(350)); 
        pnlTop.add(btnRefresh);
        pnlTop.add(btnCreate);

        add(pnlTop, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new GridLayout(2, 1, 0, 10));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        // Bảng phiếu nhập
        JPanel pnlMaster = new JPanel(new BorderLayout());
        pnlMaster.setBorder(BorderFactory.createTitledBorder("Danh sách Phiếu Nhập"));
        
        String[] colOrder = {"Mã Phiếu", "Nhân Viên Lập", "Nhà Cung Cấp", "Ngày Nhập", "Tổng Tiền (VNĐ)"};
        modelOrder = new DefaultTableModel(colOrder, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblOrder = new JTable(modelOrder);
        tblOrder.setRowHeight(25);
        pnlMaster.add(new JScrollPane(tblOrder), BorderLayout.CENTER);

        // Bảng chi tiết
        JPanel pnlDetail = new JPanel(new BorderLayout());
        pnlDetail.setBorder(BorderFactory.createTitledBorder("Chi tiết Sản Phẩm trong Phiếu"));
        
        String[] colDetail = {"Mã SP", "Tên Sản Phẩm", "Số Lượng", "Giá Nhập", "Thành Tiền"};
        modelDetails = new DefaultTableModel(colDetail, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblDetails = new JTable(modelDetails);
        tblDetails.setRowHeight(25);
        pnlDetail.add(new JScrollPane(tblDetails), BorderLayout.CENTER);

        DefaultTableCellRenderer rightRender = new DefaultTableCellRenderer();
        rightRender.setHorizontalAlignment(JLabel.RIGHT);
        tblOrder.getColumnModel().getColumn(4).setCellRenderer(rightRender);
        tblDetails.getColumnModel().getColumn(3).setCellRenderer(rightRender);
        tblDetails.getColumnModel().getColumn(4).setCellRenderer(rightRender);

        pnlCenter.add(pnlMaster);
        pnlCenter.add(pnlDetail);
        add(pnlCenter, BorderLayout.CENTER);

        // SỰ KIỆN BẢNG CHÍNH
        tblOrder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblOrder.getSelectedRow();
                if (row != -1) {
                    String orderId = tblOrder.getValueAt(row, 0).toString();
                    loadDetailsData(orderId);
                }
            }
        });

        btnRefresh.addActionListener(e -> {
            loadOrderData();
            modelDetails.setRowCount(0); 
        });

        btnCreate.addActionListener(e -> {
            DialogCreateOrder dialog = new DialogCreateOrder(this);
            dialog.setVisible(true);
            loadOrderData(); 
            modelDetails.setRowCount(0);
        });
    }

    private void loadOrderData() {
        ArrayList<Object[]> list = orderBUS.getAll();
        modelOrder.setRowCount(0);
        for (Object[] row : list) {
            Object[] formattedRow = new Object[5];
            formattedRow[0] = row[0];
            formattedRow[1] = row[1];
            formattedRow[2] = row[2];
            try { formattedRow[3] = sdf.format(row[3]); } catch (Exception e) { formattedRow[3] = row[3]; }
            try { formattedRow[4] = df.format(Double.parseDouble(row[4].toString())); } catch (Exception e) { formattedRow[4] = row[4]; }
            
            modelOrder.addRow(formattedRow);
        }
    }

    private void loadDetailsData(String orderId) {
        ArrayList<Object[]> list = orderBUS.getDetailsByOrderID(orderId);
        modelDetails.setRowCount(0);
        for (Object[] row : list) {
            Object[] formattedRow = new Object[5];
            formattedRow[0] = row[0];
            formattedRow[1] = row[1];
            formattedRow[2] = row[2];
            try { formattedRow[3] = df.format(Double.parseDouble(row[3].toString())); } catch (Exception e) { formattedRow[3] = row[3]; }
            try { formattedRow[4] = df.format(Double.parseDouble(row[4].toString())); } catch (Exception e) { formattedRow[4] = row[4]; }
            
            modelDetails.addRow(formattedRow);
        }
    }

    // CỬA SỔ TẠO PHIẾU NHẬP
    class DialogCreateOrder extends JDialog {
        private ProductBUS productBUS = new ProductBUS();
        
        private JTextField txtStaff, txtSupplier, txtProdID, txtQty, txtPrice;
        private JTable tblCart;
        private DefaultTableModel modelCart;
        private JLabel lblTotal;
        private double totalAmount = 0;

        // Các biến lưu chuỗi mờ (Placeholder)
        private final String PH_STAFF = "VD: NV01";
        private final String PH_SUPP = "VD: NCC01";
        private final String PH_PROD = "VD: LPT01";
        private final String PH_QTY = "VD: 10";
        private final String PH_PRICE = "VD: 15000000";

        public DialogCreateOrder(JFrame parent) {
            super(parent, "Tạo Phiếu Nhập Hàng", true);
            setSize(850, 550);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout());

            JPanel pnlInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
            pnlInfo.setBorder(BorderFactory.createTitledBorder("Thông tin Phiếu Nhập (PurchaseOrders)"));
            
            pnlInfo.add(new JLabel("Mã Nhân Viên (StaffID):"));
            txtStaff = new JTextField(10); pnlInfo.add(txtStaff);
            
            pnlInfo.add(new JLabel("Mã Nhà CC (SupplierID):"));
            txtSupplier = new JTextField(10); pnlInfo.add(txtSupplier);
            
            add(pnlInfo, BorderLayout.NORTH);

            JPanel pnlCenter = new JPanel(new BorderLayout());
            
            JPanel pnlInputProd = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            pnlInputProd.setBorder(BorderFactory.createTitledBorder("Chi Tiết Hàng Hóa (PurchaseOrderDetails)"));

            pnlInputProd.add(new JLabel("Mã SP:"));
            txtProdID = new JTextField(8); pnlInputProd.add(txtProdID);
            
            pnlInputProd.add(new JLabel("Số Lượng:"));
            txtQty = new JTextField(5); pnlInputProd.add(txtQty);
            
            pnlInputProd.add(new JLabel("Giá Nhập (VNĐ):"));
            txtPrice = new JTextField(12); pnlInputProd.add(txtPrice);
            
            JButton btnAddCart = new JButton("Thêm vào danh sách");
            btnAddCart.setBackground(new Color(0, 102, 204));
            pnlInputProd.add(btnAddCart);
            
            JButton btnRemoveCart = new JButton("Xóa dòng");
            pnlInputProd.add(btnRemoveCart);

            pnlCenter.add(pnlInputProd, BorderLayout.NORTH);

            String[] cols = {"Mã SP (ProductID)", "Tên SP", "Số Lượng (Quantity)", "Đơn Giá (UnitPrice)", "Thành Tiền"};
            modelCart = new DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            tblCart = new JTable(modelCart);
            tblCart.setRowHeight(25);
            pnlCenter.add(new JScrollPane(tblCart), BorderLayout.CENTER);
            
            add(pnlCenter, BorderLayout.CENTER);

            //  Panel tổng tiền + Lưu 
            JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
            lblTotal = new JLabel("TỔNG TIỀN (TotalAmount): 0 VNĐ");
            lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
            lblTotal.setForeground(Color.RED);
            pnlBottom.add(lblTotal);
            
            JButton btnSave = new JButton("LƯU PHIẾU NHẬP");
            btnSave.setBackground(new Color(34, 139, 34));
            btnSave.setFont(new Font("Arial", Font.BOLD, 14));
            pnlBottom.add(btnSave);
            
            add(pnlBottom, BorderLayout.SOUTH);

            //  THIẾT LẬP CHỮ MỜ (PLACEHOLDER) 
            setupPlaceholder(txtStaff, PH_STAFF);
            setupPlaceholder(txtSupplier, PH_SUPP);
            setupPlaceholder(txtProdID, PH_PROD);
            setupPlaceholder(txtQty, PH_QTY);
            setupPlaceholder(txtPrice, PH_PRICE);

            //  SỰ KIỆN 
            btnAddCart.addActionListener(e -> {
                String pID = txtProdID.getText().trim();
                String qtyStr = txtQty.getText().trim();
                String priceStr = txtPrice.getText().trim();
                
                if (pID.isEmpty() || pID.equals(PH_PROD) || 
                    qtyStr.isEmpty() || qtyStr.equals(PH_QTY) || 
                    priceStr.isEmpty() || priceStr.equals(PH_PRICE)) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ Mã SP, Số lượng và Giá nhập!");
                    return;
                }
                
                try {
                    int qty = Integer.parseInt(qtyStr);
                    double price = Double.parseDouble(priceStr);
                    
                    if(qty <= 0 || price <= 0) {
                        JOptionPane.showMessageDialog(this, "Số lượng và Giá nhập phải lớn hơn 0!");
                        return;
                    }

                    Object[] prod = productBUS.getByID(pID);
                    if (prod == null) {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy Sản phẩm có mã: " + pID);
                        return;
                    }
                    String pName = prod[1].toString();

                    boolean found = false;
                    for (int i = 0; i < modelCart.getRowCount(); i++) {
                        if (modelCart.getValueAt(i, 0).equals(pID)) {
                            int oldQty = Integer.parseInt(modelCart.getValueAt(i, 2).toString());
                            int newQty = oldQty + qty;
                            double subTotal = newQty * price; 
                            
                            modelCart.setValueAt(newQty, i, 2);
                            modelCart.setValueAt(df.format(price), i, 3);
                            modelCart.setValueAt(df.format(subTotal), i, 4);
                            found = true;
                            break;
                        }
                    }
                    
                    if (!found) {
                        modelCart.addRow(new Object[]{pID, pName, qty, df.format(price), df.format(qty * price)});
                    }
                    
                    updateTotal();
                    
                    // Reset lại chữ mờ sau khi thêm thành công
                    txtProdID.setText(""); setupPlaceholder(txtProdID, PH_PROD);
                    txtQty.setText(""); setupPlaceholder(txtQty, PH_QTY);
                    txtPrice.setText(""); setupPlaceholder(txtPrice, PH_PRICE);
                    txtProdID.requestFocus();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Số lượng và Giá nhập phải là chữ số hợp lệ!");
                }
            });

            btnRemoveCart.addActionListener(e -> {
                int row = tblCart.getSelectedRow();
                if (row != -1) {
                    modelCart.removeRow(row);
                    updateTotal();
                }
            });

            btnSave.addActionListener(e -> {
                String staff = txtStaff.getText().trim();
                String supplier = txtSupplier.getText().trim();
                
                if (staff.isEmpty() || staff.equals(PH_STAFF) || supplier.isEmpty() || supplier.equals(PH_SUPP)) {
                    JOptionPane.showMessageDialog(this, "Vui lòng điền đúng Mã Nhân Viên và Mã Nhà Cung Cấp!");
                    return;
                }
                if (modelCart.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Danh sách sản phẩm trống!");
                    return;
                }

                ArrayList<Object[]> detailsList = new ArrayList<>();
                for (int i = 0; i < modelCart.getRowCount(); i++) {
                    String pID = modelCart.getValueAt(i, 0).toString();
                    String qty = modelCart.getValueAt(i, 2).toString();
                    String price = modelCart.getValueAt(i, 3).toString().replace(",", ""); // Xóa dấu phẩy của tiền tệ
                    
                    detailsList.add(new Object[]{pID, qty, price});
                }

                String msg = orderBUS.add(staff, supplier, totalAmount, detailsList);
                JOptionPane.showMessageDialog(this, msg);
                
                if (msg.contains("thành công")) {
                    this.dispose(); 
                }
            });
        }

        private void updateTotal() {
            totalAmount = 0;
            for (int i = 0; i < modelCart.getRowCount(); i++) {
                String subStr = modelCart.getValueAt(i, 4).toString().replace(",", "");
                totalAmount += Double.parseDouble(subStr);
            }
            lblTotal.setText("TỔNG TIỀN (TotalAmount): " + df.format(totalAmount) + " VNĐ");
        }

        private void setupPlaceholder(JTextField textField, String placeholder) {
            if (textField.getText().isEmpty()) {
                textField.setText(placeholder);
                textField.setForeground(Color.GRAY);
            }
            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (textField.getText().equals(placeholder)) {
                        textField.setText("");
                        textField.setForeground(Color.BLACK);
                    }
                }
                @Override
                public void focusLost(FocusEvent e) {
                    if (textField.getText().isEmpty()) {
                        textField.setForeground(Color.GRAY);
                        textField.setText(placeholder);
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception ignored) {}
        new PurchaseOrderGUI().setVisible(true);
    }
}