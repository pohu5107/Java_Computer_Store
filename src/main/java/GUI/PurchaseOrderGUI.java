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

public class PurchaseOrderGUI extends JPanel {

    private PurchaseOrderBUS orderBUS = new PurchaseOrderBUS();
    private DecimalFormat df = new DecimalFormat("#,###");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private JTable tblOrder, tblDetails;
    private DefaultTableModel modelOrder, modelDetails;
    private JButton btnRefresh, btnCreate;

    public PurchaseOrderGUI() {
        setLayout(new BorderLayout(0, 10));
        setPreferredSize(new Dimension(950, 650));
        setBackground(new Color(240, 242, 245));

        initComponents();
        loadOrderData();
        setupEvents();
    }

    private void initComponents() {
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setOpaque(false);
        pnlTop.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JLabel lblTitle = new JLabel("LỊCH SỬ PHIẾU NHẬP KHO");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlTop.add(lblTitle, BorderLayout.WEST);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);

        btnRefresh = new JButton("Làm Mới");
        styleButton(btnRefresh, new Color(108, 117, 125));
        
        btnCreate = new JButton("+ TẠO PHIẾU NHẬP MỚI");
        styleButton(btnCreate, new Color(40, 167, 69));

        pnlButtons.add(btnRefresh);
        pnlButtons.add(btnCreate);
        pnlTop.add(pnlButtons, BorderLayout.EAST);

        add(pnlTop, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new GridLayout(2, 1, 0, 15));
        pnlCenter.setOpaque(false);
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 20));

        JPanel pnlMaster = new JPanel(new BorderLayout());
        pnlMaster.setBorder(BorderFactory.createTitledBorder("Danh sách Phiếu Nhập"));
        
        String[] colOrder = {"Mã Phiếu", "Nhân Viên", "Nhà Cung Cấp", "Ngày Nhập", "Tổng Tiền (VNĐ)"};
        modelOrder = new DefaultTableModel(colOrder, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblOrder = new JTable(modelOrder);
        tblOrder.setRowHeight(30);
        pnlMaster.add(new JScrollPane(tblOrder), BorderLayout.CENTER);

        JPanel pnlDetail = new JPanel(new BorderLayout());
        pnlDetail.setBorder(BorderFactory.createTitledBorder("Chi tiết Sản Phẩm trong Phiếu"));
        
        String[] colDetail = {"Mã SP", "Tên Sản Phẩm", "Số Lượng", "Giá Nhập", "Thành Tiền"};
        modelDetails = new DefaultTableModel(colDetail, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblDetails = new JTable(modelDetails);
        tblDetails.setRowHeight(30);
        pnlDetail.add(new JScrollPane(tblDetails), BorderLayout.CENTER);

        DefaultTableCellRenderer rightRender = new DefaultTableCellRenderer();
        rightRender.setHorizontalAlignment(JLabel.RIGHT);
        tblOrder.getColumnModel().getColumn(4).setCellRenderer(rightRender);
        tblDetails.getColumnModel().getColumn(3).setCellRenderer(rightRender);
        tblDetails.getColumnModel().getColumn(4).setCellRenderer(rightRender);

        pnlCenter.add(pnlMaster);
        pnlCenter.add(pnlDetail);
        add(pnlCenter, BorderLayout.CENTER);
    }

    private void setupEvents() {
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
            Window parentWindow = SwingUtilities.getWindowAncestor(this); 
            DialogCreateOrder dialog = new DialogCreateOrder((Frame) parentWindow);
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

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
    }

    class DialogCreateOrder extends JDialog {
        private ProductBUS productBUS = new ProductBUS();
        private JTextField txtStaff, txtSupplier, txtProdID, txtQty, txtPrice;
        private JTable tblCart;
        private DefaultTableModel modelCart;
        private JLabel lblTotal;
        private double totalAmount = 0;

        private final String PH_STAFF = "VD: NV01", PH_SUPP = "VD: NCC01", PH_PROD = "VD: LPT01", PH_QTY = "1", PH_PRICE = "0";

        public DialogCreateOrder(Frame parent) {
            super(parent, "Tạo Phiếu Nhập Hàng", true);
            setSize(900, 600);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout(10, 10));

            JPanel pnlNorth = new JPanel(new GridLayout(1, 2, 20, 0));
            pnlNorth.setBorder(BorderFactory.createTitledBorder("Thông tin chung"));
            pnlNorth.add(createInputGroup("Mã Nhân Viên:", txtStaff = new JTextField()));
            pnlNorth.add(createInputGroup("Mã Nhà Cung Cấp:", txtSupplier = new JTextField()));
            add(pnlNorth, BorderLayout.NORTH);

            JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
            pnlCenter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel pnlInputProd = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            pnlInputProd.add(new JLabel("Mã SP:"));
            txtProdID = new JTextField(8); pnlInputProd.add(txtProdID);
            pnlInputProd.add(new JLabel("SL:"));
            txtQty = new JTextField(4); pnlInputProd.add(txtQty);
            pnlInputProd.add(new JLabel("Giá:"));
            txtPrice = new JTextField(10); pnlInputProd.add(txtPrice);

            JButton btnAddCart = new JButton("Thêm");
            styleButton(btnAddCart, new Color(0, 123, 255));
            pnlInputProd.add(btnAddCart);

            JButton btnRemove = new JButton("Xóa dòng");
            styleButton(btnRemove, new Color(220, 53, 69));
            pnlInputProd.add(btnRemove);

            pnlCenter.add(pnlInputProd, BorderLayout.NORTH);

            String[] cols = {"Mã SP", "Tên SP", "Số Lượng", "Đơn Giá", "Thành Tiền"};
            modelCart = new DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            tblCart = new JTable(modelCart);
            tblCart.setRowHeight(28);
            pnlCenter.add(new JScrollPane(tblCart), BorderLayout.CENTER);
            add(pnlCenter, BorderLayout.CENTER);

            JPanel pnlSouth = new JPanel(new BorderLayout());
            pnlSouth.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
            lblTotal = new JLabel("TỔNG TIỀN: 0 VNĐ");
            lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lblTotal.setForeground(Color.RED);
            pnlSouth.add(lblTotal, BorderLayout.WEST);

            JButton btnSave = new JButton("XÁC NHẬN NHẬP KHO");
            styleButton(btnSave, new Color(40, 167, 69));
            btnSave.setPreferredSize(new Dimension(200, 40));
            pnlSouth.add(btnSave, BorderLayout.EAST);
            add(pnlSouth, BorderLayout.SOUTH);

            btnAddCart.addActionListener(e -> {
                try {
                    String pID = txtProdID.getText().trim();
                    int qty = Integer.parseInt(txtQty.getText().trim());
                    double price = Double.parseDouble(txtPrice.getText().trim());

                    Object[] prod = productBUS.getByID(pID);
                    if (prod == null) {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy SP!");
                        return;
                    }
                    
                    modelCart.addRow(new Object[]{pID, prod[1], qty, df.format(price), df.format(qty * price)});
                    updateTotal();
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi nhập liệu!"); }
            });

            btnRemove.addActionListener(e -> {
                int r = tblCart.getSelectedRow();
                if (r != -1) { modelCart.removeRow(r); updateTotal(); }
            });

            btnSave.addActionListener(e -> {
                if (modelCart.getRowCount() == 0) return;
                ArrayList<Object[]> detailsList = new ArrayList<>();
                for (int i = 0; i < modelCart.getRowCount(); i++) {
                    detailsList.add(new Object[]{
                        modelCart.getValueAt(i, 0), modelCart.getValueAt(i, 2), 
                        modelCart.getValueAt(i, 3).toString().replace(",", "")
                    });
                }
                String msg = orderBUS.add(txtStaff.getText(), txtSupplier.getText(), totalAmount, detailsList);
                JOptionPane.showMessageDialog(this, msg);
                if (msg.contains("thành công")) dispose();
            });
        }

        private JPanel createInputGroup(String label, JTextField tf) {
            JPanel p = new JPanel(new BorderLayout(5, 5));
            p.add(new JLabel(label), BorderLayout.NORTH);
            p.add(tf, BorderLayout.CENTER);
            return p;
        }

        private void updateTotal() {
            totalAmount = 0;
            for (int i = 0; i < modelCart.getRowCount(); i++) {
                totalAmount += Double.parseDouble(modelCart.getValueAt(i, 4).toString().replace(",", ""));
            }
            lblTotal.setText("TỔNG TIỀN: " + df.format(totalAmount) + " VNĐ");
        }
    }
}