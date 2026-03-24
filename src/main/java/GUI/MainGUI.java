/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    private JPanel pnlContent; // Panel chính chứa các giao diện con
    private CardLayout cardLayout;

    public MainGUI() {
        initComponents();
    }

    private void initComponents() {
        setTitle("HỆ THỐNG QUẢN LÝ MÁY TÍNH 2026");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 1. THANH MENU BÊN TRÁI (SIDEBAR)
        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setBackground(new Color(45, 52, 54)); // Màu tối sang trọng
        pnlSidebar.setPreferredSize(new Dimension(200, 800));

        // 2. VÙNG HIỂN THỊ NỘI DUNG (DÙNG CARDLAYOUT)
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);

        // THÊM CÁC GUI CON VÀO CARDLAYOUT
        pnlContent.add(new BrandGUI(), "Brand");
        // pnlContent.add(new ProductGUI(), "Product");
        pnlContent.add(new StatisticGUI(), "Statistic");

        // 3. TẠO CÁC NÚT MENU
        addMenuButton(pnlSidebar, "Quản Lý Thương Hiệu", "Brand");
        addMenuButton(pnlSidebar, "Quản Lý Sản Phẩm", "Product");
        addMenuButton(pnlSidebar, "Thống Kê Doanh Thu", "Statistic");

        // Gắn vào Frame chính
        add(pnlSidebar, BorderLayout.WEST);
        add(pnlContent, BorderLayout.CENTER);
    }

    private void addMenuButton(JPanel sidebar, String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 50));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(45, 52, 54));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        
        // Sự kiện chuyển đổi giao diện
        btn.addActionListener(e -> cardLayout.show(pnlContent, cardName));
        
        sidebar.add(btn);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e) {}
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}