/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame {

    private JPanel pnlSidebar;
    private JPanel pnlContent;
    private CardLayout cardLayout;

    private final Color COLOR_PRIMARY = new Color(0, 102, 204);
    private final Color COLOR_SIDEBAR = new Color(43, 48, 59);

    public MainGUI() {
        initComponents();
    }

    private void initComponents() {
        setTitle("HỆ THỐNG QUẢN LÝ CỬA HÀNG MÁY TÍNH");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        pnlSidebar = new JPanel();
        pnlSidebar.setBackground(COLOR_SIDEBAR);
        pnlSidebar.setPreferredSize(new Dimension(220, 0));
        pnlSidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));

        JLabel lblLogo = new JLabel("CỬA HÀNG MÁY TÍNH");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(20, 10, 30, 10));
        pnlSidebar.add(lblLogo);

        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);

        pnlContent.add(new ProductGUI(), "Sản phẩm");
        pnlContent.add(new BrandGUI(), "Thương hiệu");
        pnlContent.add(new CategoryGUI(), "Danh mục");
        pnlContent.add(new SaleGUI(), "Bán hàng");
        pnlContent.add(new InvoiceGUI(), "Hóa đơn");
        pnlContent.add(new PurchaseOrderGUI(), "Nhập kho");
        pnlContent.add(new StaffGUI(), "Nhân viên");
        pnlContent.add(new CustomerGUI(), "Khách hàng");
        pnlContent.add(new PromotionGUI(), "Khuyến mãi");
        pnlContent.add(new StatisticGUI(), "Thống kê");

        createMenuButton("Sản phẩm", e -> showCard("Sản phẩm"));
        createMenuButton("Thương hiệu", e -> showCard("Thương hiệu"));
        createMenuButton("Danh mục", e -> showCard("Danh mục"));
        createMenuButton("Bán hàng", e -> showCard("Bán hàng"));
        createMenuButton("Hóa đơn", e -> showCard("Hóa đơn"));
        createMenuButton("Nhập kho", e -> showCard("Nhập kho"));
        createMenuButton("Nhân viên", e -> showCard("Nhân viên"));
        createMenuButton("Khách hàng", e -> showCard("Khách hàng"));                
        createMenuButton("Khuyến mãi", e -> showCard("Khuyến mãi"));
        createMenuButton("Thống kê", e -> showCard("Thống kê"));

        add(pnlSidebar, BorderLayout.WEST);
        add(pnlContent, BorderLayout.CENTER);
    }

    private void createMenuButton(String text, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(220, 50));
        btn.setBackground(COLOR_SIDEBAR);
        btn.setForeground(new Color(200, 200, 200));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 20, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(60, 68, 83));
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(COLOR_SIDEBAR);
                btn.setForeground(new Color(200, 200, 200));
            }
        });

        btn.addActionListener(listener);
        pnlSidebar.add(btn);
    }

    private void showCard(String cardName) {
        cardLayout.show(pnlContent, cardName);
    }
   
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> {
            new MainGUI().setVisible(true);
        });
    }
}