package GUI;

import BUS.ProductBUS;
import BUS.InvoiceBUS;
import DAO.InvoiceDAO;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class SaleGUI extends JPanel {
    private ProductBUS prodBUS = new ProductBUS();
    private InvoiceBUS invBUS = new InvoiceBUS();
    private InvoiceDAO invDAO = new InvoiceDAO();
    
    private JTextField txtProdID, txtQty, txtInvID, txtCustID, txtStaffID;
    private JTable tblCart;
    private DefaultTableModel modelCart;
    private JLabel lblTotal;
    private double totalAmount = 0;
    private DecimalFormat df = new DecimalFormat("#,###");

    public SaleGUI() {
        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(1000, 700)); 
        setBackground(new Color(240, 242, 245));

        initComponents();
        autoGenerateID();
        setupEvents();
    }

    private void initComponents() {
        JPanel pnlLeft = new JPanel(new BorderLayout(0, 10));
        pnlLeft.setOpaque(false);

        JPanel pnlInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setPreferredSize(new Dimension(0, 55)); 

        pnlInput.add(new JLabel("Mã SP:"));
        txtProdID = new JTextField(8); 
        pnlInput.add(txtProdID);

        pnlInput.add(new JLabel("SL:"));
        txtQty = new JTextField("1", 3); 
        pnlInput.add(txtQty);

        JButton btnAdd = new JButton("THÊM VÀO GIỎ");
        styleButton(btnAdd, new Color(0, 123, 255), Color.WHITE);
        pnlInput.add(btnAdd);

        JButton btnRemove = new JButton("XÓA DÒNG");
        styleButton(btnRemove, new Color(220, 53, 69), Color.WHITE);
        pnlInput.add(btnRemove); 

        pnlLeft.add(pnlInput, BorderLayout.NORTH);

        String[] cols = {"Mã SP", "Tên Sản Phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
        modelCart = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblCart = new JTable(modelCart);
        tblCart.setRowHeight(35);
        tblCart.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblCart.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        tblCart.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tblCart.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        JScrollPane scroll = new JScrollPane(tblCart);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        pnlLeft.add(scroll, BorderLayout.CENTER);

        add(pnlLeft, BorderLayout.CENTER);

        JPanel pnlRight = new JPanel();
        pnlRight.setPreferredSize(new Dimension(320, 0));
        pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblInfoTitle = new JLabel("THÔNG TIN HÓA ĐƠN");
        lblInfoTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblInfoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlRight.add(lblInfoTitle);
        pnlRight.add(Box.createVerticalStrut(20));

        pnlRight.add(createInputLabel("Mã Hóa Đơn:"));
        txtInvID = new JTextField(); 
        styleTextField(txtInvID);
        pnlRight.add(txtInvID);
        
        pnlRight.add(Box.createVerticalStrut(10));
        pnlRight.add(createInputLabel("Mã Khách Hàng:"));
        txtCustID = new JTextField(); 
        styleTextField(txtCustID);
        pnlRight.add(txtCustID);
        
        pnlRight.add(Box.createVerticalStrut(10));
        pnlRight.add(createInputLabel("Mã Nhân Viên:"));
        txtStaffID = new JTextField(); 
        styleTextField(txtStaffID);
        pnlRight.add(txtStaffID);
        
        pnlRight.add(Box.createVerticalGlue());

        lblTotal = new JLabel("TỔNG: 0 VNĐ");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(new Color(220, 53, 69));
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlRight.add(lblTotal);

        pnlRight.add(Box.createVerticalStrut(20));
        
        JButton btnPay = new JButton("THANH TOÁN");
        btnPay.setMaximumSize(new Dimension(300, 60));
        styleButton(btnPay, new Color(40, 167, 69), Color.WHITE);
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnPay.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlRight.add(btnPay);
        
        add(pnlRight, BorderLayout.EAST);

        btnAdd.addActionListener(e -> addToCart());
        btnRemove.addActionListener(e -> removeFromCart());
        btnPay.addActionListener(e -> processPayment());
    }

    private void setupEvents() {
        KeyAdapter enterKey = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) addToCart();
                if (e.getKeyCode() == KeyEvent.VK_F9) processPayment();
            }
        };
        txtProdID.addKeyListener(enterKey);
        txtQty.addKeyListener(enterKey);
    }

    private JLabel createInputLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void styleTextField(JTextField tf) {
        tf.setMaximumSize(new Dimension(300, 35));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
    }

    private void addToCart() {
        String pId = txtProdID.getText().trim();
        String qtyStr = txtQty.getText().trim();
        if(pId.isEmpty()) return;

        try {
            int buyQty = Integer.parseInt(qtyStr);
            if(buyQty <= 0) return;

            Object[] p = prodBUS.getByID(pId); 
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Sản phẩm không tồn tại!");
                return;
            }           
            double price = Double.parseDouble(p[2].toString()); 
            int stock = Integer.parseInt(p[3].toString());   
           
            int currentInCart = 0;
            int existingRow = -1;
            for (int i = 0; i < modelCart.getRowCount(); i++) {
                if (modelCart.getValueAt(i, 0).toString().equals(pId)) {
                    currentInCart = Integer.parseInt(modelCart.getValueAt(i, 2).toString());
                    existingRow = i;
                    break;
                }
            }

            if(buyQty + currentInCart > stock) {
                JOptionPane.showMessageDialog(this, "Kho không đủ! (Tồn: " + stock + ")");
                return;
            }

            if (existingRow != -1) {
                int newQty = currentInCart + buyQty;
                modelCart.setValueAt(newQty, existingRow, 2);
                modelCart.setValueAt(df.format(newQty * price), existingRow, 4);
            } else {
                modelCart.addRow(new Object[]{
                    p[0], 
                    p[1], 
                    buyQty, 
                    df.format(price), 
                    df.format(buyQty * price)
                });
            }

            updateTotal();
            resetInput();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập liệu hoặc dữ liệu sản phẩm!");
            e.printStackTrace();
        }
    }
    private void autoGenerateID() {
        String lastID = invDAO.getLastID();
        if (lastID == null || lastID.isEmpty()) {
            txtInvID.setText("HD001");
        } else {
            try {
                int num = Integer.parseInt(lastID.substring(2)) + 1;
                txtInvID.setText(String.format("HD%03d", num));
            } catch (Exception e) {
                txtInvID.setText("HD001");
            }
        }
        txtInvID.setEditable(false);
        txtInvID.setBackground(new Color(245, 245, 245));
    }

    private void removeFromCart() {
        int row = tblCart.getSelectedRow();
        if (row != -1) {
            modelCart.removeRow(row);
            updateTotal();
        }
    }

    private void updateTotal() {
        totalAmount = 0;
        for (int i = 0; i < modelCart.getRowCount(); i++) {
            String val = modelCart.getValueAt(i, 4).toString().replace(",", "");
            totalAmount += Double.parseDouble(val);
        }
        lblTotal.setText("TỔNG: " + df.format(totalAmount) + " VNĐ");
    }

    private void resetInput() {
        txtProdID.setText("");
        txtQty.setText("1");
        txtProdID.requestFocus();
    }

    private void processPayment() {
        if (modelCart.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!");
            return;
        }
        if (txtCustID.getText().isEmpty() || txtStaffID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã KH và Mã NV!");
            return;
        }

        ArrayList<Object[]> details = new ArrayList<>();
        for (int i = 0; i < modelCart.getRowCount(); i++) {
            details.add(new Object[]{ 
                modelCart.getValueAt(i, 0), 
                modelCart.getValueAt(i, 2), 
                modelCart.getValueAt(i, 3).toString().replace(",", "")
            });
        }

        String msg = invBUS.add(txtInvID.getText(), txtCustID.getText(), txtStaffID.getText(), totalAmount, details);
        if(msg.toLowerCase().contains("thành công")) {
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
            modelCart.setRowCount(0);
            updateTotal();
            autoGenerateID();
            txtCustID.setText("");
            txtStaffID.setText("");
        } else {
            JOptionPane.showMessageDialog(this, msg);
        }
    }
}