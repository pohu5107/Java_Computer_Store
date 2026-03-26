
package GUI;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {

    public MainGUI() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Trang Chủ - Quản Lý Cửa Hàng Máy Tính");
        setSize(500, 450);
        // Trang chủ thì dùng EXIT_ON_CLOSE để tắt hẳn chương trình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null); // Hiển thị ở giữa màn hình
        setLayout(new BorderLayout());

        // --- PANEL TIÊU ĐỀ ---
        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 102, 204));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0)); // Tạo khoảng cách
        add(lblTitle, BorderLayout.NORTH);

        // --- PANEL MENU (Chứa 3 nút bấm) ---
        // Dùng GridLayout chia làm 3 hàng, 1 cột, khoảng cách giữa các nút là 20px
        JPanel pnlMenu = new JPanel(new GridLayout(3, 1, 20, 20));
        pnlMenu.setBorder(BorderFactory.createEmptyBorder(10, 80, 50, 80)); // Canh lề 2 bên

        JButton btnSale = createMenuButton("Bán Hàng (POS)", new Color(34, 139, 34)); // Màu xanh lá
        JButton btnInvoice = createMenuButton("Quản Lý Hóa Đơn", new Color(220, 100, 0)); // Màu cam
        JButton btnBrand = createMenuButton("Quản Lý Thương Hiệu", new Color(100, 100, 100)); // Màu xám

        pnlMenu.add(btnSale);
        pnlMenu.add(btnInvoice);
        pnlMenu.add(btnBrand);
        
        add(pnlMenu, BorderLayout.CENTER);

        // --- SỰ KIỆN CHUYỂN TRANG ---
        // Khi bấm nút, khởi tạo giao diện tương ứng và cho hiển thị lên
        btnSale.addActionListener(e -> new SaleGUI().setVisible(true));
        btnInvoice.addActionListener(e -> new InvoiceGUI().setVisible(true));
        btnBrand.addActionListener(e -> new BrandGUI().setVisible(true));
    }

    // Hàm phụ trợ để làm đẹp các nút bấm cho đồng bộ
    private JButton createMenuButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(bgColor);
        btn.setForeground(Color.black);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Đổi con trỏ chuột thành hình bàn tay
        return btn;
    }

    // Chạy thử Trang chủ
    public static void main(String[] args) {
        // Đặt giao diện theo giao diện gốc của hệ điều hành (Windows/Mac)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
        new MainGUI().setVisible(true);
    }
}
