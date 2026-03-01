///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package GUI;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import BUS.BrandsBUS; // Gọi lớp BUS của bạn
//
//public class BrandsGUI extends JFrame {
//    // 1. Khai báo các thành phần giao diện
//    private JTable table;
//    private JScrollPane scrollPane;
//    private JButton btnRefresh;
//    private BrandsBUS brandBus = new BrandsBUS(); // Kết nối với lớp BUS
//
//    public BrandsGUI() {
//        initComponents(); // Khởi tạo các thành phần
//        loadData();       // Đổ dữ liệu vào bảng
//    }
//
//    private void initComponents() {
//        // Cấu hình cửa sổ chính
//        setTitle("Quản Lý Thương Hiệu - Java Computer Store");
//        setSize(800, 500);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null); // Hiển thị giữa màn hình
//        setLayout(new BorderLayout()); // Sử dụng BorderLayout để quản lý vị trí
//
//        // Khởi tạo bảng và thanh cuộn
//        table = new JTable();
//        scrollPane = new JScrollPane(table);
//        add(scrollPane, BorderLayout.CENTER); // Bảng nằm ở giữa
//
//        // Khởi tạo nút bấm và thanh công cụ phía dưới
//        btnRefresh = new JButton("Tải lại dữ liệu");
//        JPanel panelBottom = new JPanel();
//        panelBottom.add(btnRefresh);
//        add(panelBottom, BorderLayout.SOUTH); // Nút bấm nằm ở phía dưới
//
//        // Sự kiện khi nhấn nút Refresh
//        btnRefresh.addActionListener(e -> loadData());
//    }
//
//    private void loadData() {
//        // Gọi hàm từ lớp BUS để lấy dữ liệu
//        DefaultTableModel model = brandBus.getAllBrands();
//        if (model != null) {
//            table.setModel(model); // Gán dữ liệu 4 cột vào bảng
//        }
//    }
//
//    public static void main(String[] args) {
//        // Chạy giao diện
//        SwingUtilities.invokeLater(() -> {
//            new BrandsGUI().setVisible(true);
//        });
//    }
//}
