/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BUS.StatisticBUS;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class StatisticGUI extends JFrame {
    private StatisticBUS statisticBUS = new StatisticBUS();
    private DecimalFormat df = new DecimalFormat("#,### VNĐ");

    private JTable tblReport;
    private DefaultTableModel model;
    private JLabel lblRevenue, lblExpenditure, lblProfit;
    private JComboBox<String> cbMonth, cbYear, cbQuarter;
    private JButton btnViewMonth, btnViewQuarter, btnRefresh;

    public StatisticGUI() {
        initComponents();
        loadOverview(LocalDate.now().withDayOfMonth(1).toString(), LocalDate.now().toString());
    }

    private void initComponents() {
        setTitle("Hệ Thống Thống Kê Doanh Thu & Lợi Nhuận");
        setSize(1100, 750);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 242, 245));

        // --- 1. DASHBOARD CARDS (HIỂN THỊ TỔNG QUAN) ---
        JPanel pnlCards = new JPanel(null);
        pnlCards.setOpaque(false);
        pnlCards.setBounds(20, 20, 1045, 120);
        add(pnlCards);

        lblRevenue = createCard(pnlCards, "TỔNG DOANH THU", new Color(40, 167, 69), 0); // Xanh lá
        lblExpenditure = createCard(pnlCards, "TỔNG CHI PHÍ", new Color(220, 53, 69), 355); // Đỏ
        lblProfit = createCard(pnlCards, "LỢI NHUẬN", new Color(0, 123, 255), 710); // Xanh dương

        // --- 2. BỘ LỌC THỐNG KÊ ---
        JPanel pnlFilter = new JPanel(null);
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createTitledBorder("Bộ lọc thống kê"));
        pnlFilter.setBounds(20, 150, 1045, 80);
        add(pnlFilter);

        // Lọc theo tháng
        pnlFilter.add(new JLabel("Tháng:"));
        cbMonth = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
        cbMonth.setBounds(70, 30, 60, 25);
        pnlFilter.add(cbMonth);

        pnlFilter.add(new JLabel("Năm:"));
        cbYear = new JComboBox<>(new String[]{"2023", "2024", "2025", "2026"});
        cbYear.setSelectedItem("2026");
        cbYear.setBounds(180, 30, 80, 25);
        pnlFilter.add(cbYear);

        btnViewMonth = new JButton("Xem theo Tháng");
        btnViewMonth.setBounds(270, 25, 140, 35);
        styleButton(btnViewMonth, new Color(108, 117, 125));
        pnlFilter.add(btnViewMonth);

        // Lọc theo quý
        pnlFilter.add(new JLabel("Quý:"));
        cbQuarter = new JComboBox<>(new String[]{"1", "2", "3", "4"});
        cbQuarter.setBounds(550, 30, 60, 25);
        pnlFilter.add(cbQuarter);

        btnViewQuarter = new JButton("Xem theo Quý");
        btnViewQuarter.setBounds(630, 25, 140, 35);
        styleButton(btnViewQuarter, new Color(108, 117, 125));
        pnlFilter.add(btnViewQuarter);

        btnRefresh = new JButton("Làm Mới");
        btnRefresh.setBounds(900, 25, 120, 35);
        styleButton(btnRefresh, new Color(40, 167, 69));
        pnlFilter.add(btnRefresh);

        // --- 3. BẢNG CHI TIẾT ---
        String[] columns = {"Mã SP", "Tên Sản Phẩm", "Số Lượng Bán", "Doanh Thu", "Lợi Nhuận Dự Tính"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblReport = new JTable(model);
        tblReport.setRowHeight(30);
        tblReport.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(tblReport);
        scroll.setBounds(20, 250, 1045, 430);
        add(scroll);

        // --- SỰ KIỆN ---
        btnViewMonth.addActionListener(e -> {
            int m = Integer.parseInt(cbMonth.getSelectedItem().toString());
            int y = Integer.parseInt(cbYear.getSelectedItem().toString());
            fillTable(statisticBUS.getReportByMonth(m, y));
            updateOverviewByMonth(m, y);
        });

        btnViewQuarter.addActionListener(e -> {
            int q = Integer.parseInt(cbQuarter.getSelectedItem().toString());
            int y = Integer.parseInt(cbYear.getSelectedItem().toString());
            fillTable(statisticBUS.getReportByQuarter(q, y));
            // Cần tính overview cho quý (fromDate, toDate tương ứng logic BUS)
            updateOverviewByQuarter(q, y);
        });

        btnRefresh.addActionListener(e -> {
            loadOverview("2023-01-01", "2026-12-31");
            model.setRowCount(0);
        });
    }

    private JLabel createCard(JPanel parent, String title, Color color, int x) {
        JPanel card = new JPanel(null);
        card.setBackground(color);
        card.setBounds(x, 0, 330, 110);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setBounds(20, 15, 200, 25);
        card.add(lblTitle);

        JLabel lblValue = new JLabel("0 VNĐ");
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblValue.setBounds(20, 50, 300, 40);
        card.add(lblValue);

        parent.add(card);
        return lblValue;
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false); // Tắt render mặc định
        btn.setOpaque(true);             // Ép màu nền
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void fillTable(ArrayList<Object[]> list) {
        model.setRowCount(0);
        for (Object[] row : list) {
            model.addRow(new Object[]{
                row[0], row[1], row[2], 
                df.format(row[3]), 
                df.format(row[4])
            });
        }
    }

    private void loadOverview(String from, String to) {
        Object[] data = statisticBUS.getOverview(from, to);
        lblRevenue.setText(df.format(data[0]));
        lblExpenditure.setText(df.format(data[1]));
        lblProfit.setText(df.format(data[2]));
    }

    private void updateOverviewByMonth(int m, int y) {
        String from = y + "-" + (m < 10 ? "0" + m : m) + "-01";
        LocalDate end = java.time.YearMonth.of(y, m).atEndOfMonth();
        loadOverview(from, end.toString());
    }

    private void updateOverviewByQuarter(int q, int y) {
        String from = "", to = "";
        if(q == 1) { from = y+"-01-01"; to = y+"-03-31"; }
        else if(q == 2) { from = y+"-04-01"; to = y+"-06-30"; }
        else if(q == 3) { from = y+"-07-01"; to = y+"-09-30"; }
        else { from = y+"-10-01"; to = y+"-12-31"; }
        loadOverview(from, to);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e) {}
        SwingUtilities.invokeLater(() -> new StatisticGUI().setVisible(true));
    }
}