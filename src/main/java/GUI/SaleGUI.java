/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BUS.ProductBUS;
import BUS.InvoiceBUS;
import DAO.InvoiceDAO;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class SaleGUI extends JFrame {
    private ProductBUS prodBUS = new ProductBUS();
    private InvoiceBUS invBUS = new InvoiceBUS();
    private InvoiceDAO invDAO = new InvoiceDAO();
    
    private JTextField txtProdID, txtQty, txtInvID, txtCustID, txtStaffID;
    private JTable tblCart;
    private DefaultTableModel modelCart;
    private JLabel lblTotal;
    private double totalAmount = 0;
    private DecimalFormat df = new DecimalFormat("#,###"); // Dùng chung định dạng tiền

    public SaleGUI() {
        initComponents();
        autoGenerateID();
    }

    private void initComponents() {
        setTitle("Hệ Thống Bán Hàng Chuyên Nghiệp");
        setSize(1000, 650);
//        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // --- PANEL TRÊN ---
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTop.setBorder(BorderFactory.createTitledBorder("Nhập Sản Phẩm"));
        
        pnlTop.add(new JLabel("Mã SP:"));
        txtProdID = new JTextField(10);
        pnlTop.add(txtProdID);
        
        pnlTop.add(new JLabel("Số lượng:"));
        txtQty = new JTextField(5);
        txtQty.setText("1");
        pnlTop.add(txtQty);
        
        JButton btnAdd = new JButton("Thêm vào giỏ (Enter)");
        btnAdd.setBackground(new Color(0, 102, 204));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setOpaque(true);
        btnAdd.setFont(new Font("Arial", Font.BOLD, 12));
        pnlTop.add(btnAdd);
        
        JButton btnRemove = new JButton("Xóa món đã chọn");
        btnRemove.setBackground(new Color(204, 0, 0));
        btnRemove.setForeground(Color.WHITE);
        btnRemove.setFocusPainted(false);
        btnRemove.setOpaque(true);
        btnRemove.setFont(new Font("Arial", Font.BOLD, 12));
        pnlTop.add(btnRemove);

        add(pnlTop, BorderLayout.NORTH);

        // --- PANEL GIỮA ---
        String[] cols = {"Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"};
        modelCart = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblCart = new JTable(modelCart);
        tblCart.setRowHeight(30);
        add(new JScrollPane(tblCart), BorderLayout.CENTER);

        // --- PANEL PHẢI ---
        JPanel pnlRight = new JPanel();
        pnlRight.setPreferredSize(new Dimension(320, 0));
        pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));
        pnlRight.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnlRight.add(new JLabel("Mã Hóa Đơn (Tự động):"));
        txtInvID = new JTextField(); pnlRight.add(txtInvID);
        pnlRight.add(new JLabel("Mã Khách Hàng:"));
        txtCustID = new JTextField(); pnlRight.add(txtCustID);
        pnlRight.add(new JLabel("Mã Nhân Viên:"));
        txtStaffID = new JTextField(); pnlRight.add(txtStaffID);
        
        pnlRight.add(Box.createVerticalStrut(30));
        lblTotal = new JLabel("TỔNG: 0 VNĐ");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 24));
        lblTotal.setForeground(new Color(204, 0, 0));
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlRight.add(lblTotal);

        pnlRight.add(Box.createVerticalStrut(20));
        JButton btnPay = new JButton("THANH TOÁN");
        btnPay.setMaximumSize(new Dimension(320, 70));
        btnPay.setBackground(new Color(34, 139, 34));
        btnPay.setForeground(Color.WHITE);
        btnPay.setFont(new Font("Arial", Font.BOLD, 18));
        btnPay.setFocusPainted(false);
        btnPay.setOpaque(true);
        pnlRight.add(btnPay);
        
        add(pnlRight, BorderLayout.EAST);

        // --- SỰ KIỆN ---
        btnAdd.addActionListener(e -> addToCart());
        btnRemove.addActionListener(e -> removeFromCart());
        btnPay.addActionListener(e -> processPayment());

        KeyAdapter enterKey = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) addToCart();
            }
        };
        txtProdID.addKeyListener(enterKey);
        txtQty.addKeyListener(enterKey);
    }

    private void addToCart() {
        String pId = txtProdID.getText().trim();
        String qtyStr = txtQty.getText().trim();
        if(pId.isEmpty()) return;

        try {
            int buyQty = Integer.parseInt(qtyStr);
            Object[] p = prodBUS.getByID(pId); 
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Sản phẩm không tồn tại!");
                return;
            }

            double price = Double.parseDouble(p[2].toString());
            int stock = Integer.parseInt(p[3].toString());

            if(buyQty > stock) {
                JOptionPane.showMessageDialog(this, "Kho không đủ!");
                return;
            }

            // Xử lý cộng dồn nếu SP đã có trong giỏ
            for (int i = 0; i < modelCart.getRowCount(); i++) {
                if (modelCart.getValueAt(i, 0).toString().equals(pId)) {
                    int oldQty = Integer.parseInt(modelCart.getValueAt(i, 2).toString());
                    int newQty = oldQty + buyQty;

                    if(newQty > stock) {
                        JOptionPane.showMessageDialog(this, "Tổng số lượng vượt quá tồn kho!");
                        return;
                    }

                    modelCart.setValueAt(newQty, i, 2);
                    modelCart.setValueAt(df.format(newQty * price), i, 4); // Cập nhật chuỗi có định dạng
                    updateTotal();
                    resetInput();
                    return;
                }
            }

            // Thêm mới vào giỏ
            modelCart.addRow(new Object[]{pId, p[1], buyQty, df.format(price), df.format(buyQty * price)});
            updateTotal();
            resetInput();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }
    
    private void autoGenerateID() {
        String lastID = invDAO.getLastID();
        if (lastID == null || lastID.isEmpty()) {
            txtInvID.setText("HD001");
        } else {
            String prefix = lastID.replaceAll("[0-9]", "");
            String numberStr = lastID.replaceAll("[^0-9]", "");
            int nextNumber = Integer.parseInt(numberStr) + 1;
            String nextID = prefix + String.format("%0" + numberStr.length() + "d", nextNumber);
            txtInvID.setText(nextID);
        }
        txtInvID.setEditable(false);
        txtInvID.setBackground(new Color(235, 235, 235));
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
            String subTotalStr = modelCart.getValueAt(i, 4).toString().replace(",", "");
            totalAmount += Double.parseDouble(subTotalStr);
        }
        lblTotal.setText("TỔNG: " + df.format(totalAmount) + " VNĐ");
    }

    private void resetInput() {
        txtProdID.setText("");
        txtQty.setText("1");
        txtProdID.requestFocus();
    }

    private void processPayment() {
        String invId = txtInvID.getText().trim();
        String custId = txtCustID.getText().trim();
        String staffId = txtStaffID.getText().trim();

        if (custId.isEmpty() || staffId.isEmpty() || modelCart.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã KH, Mã NV và chọn SP!");
            return;
        }

        ArrayList<Object[]> details = new ArrayList<>();
        for (int i = 0; i < modelCart.getRowCount(); i++) {
            // Khi gửi dữ liệu xuống BUS, hãy loại bỏ dấu phẩy để BUS tính toán đúng
            String priceStr = modelCart.getValueAt(i, 3).toString().replace(",", "");
            details.add(new Object[]{ 
                modelCart.getValueAt(i, 0), 
                modelCart.getValueAt(i, 1), 
                modelCart.getValueAt(i, 2), 
                priceStr // Gửi số thuần túy
            });
        }

        String msg = invBUS.add(invId, custId, staffId, totalAmount, details);
        if(msg.contains("thành công")) {
            JOptionPane.showMessageDialog(this, "Thanh toán thành công: " + invId);
            modelCart.setRowCount(0);
            updateTotal();
            autoGenerateID();
        } else {
            JOptionPane.showMessageDialog(this, msg);
        }
    }

    public static void main(String[] args) {
        new SaleGUI().setVisible(true);
    }
}