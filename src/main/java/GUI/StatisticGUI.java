/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BUS.StatisticBUS;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;

public class StatisticGUI extends JPanel {
    private StatisticBUS statisticBUS = new StatisticBUS();
    private DecimalFormat df = new DecimalFormat("#,### VNĐ");

    private JTable tblReport;
    private DefaultTableModel model;
    private JLabel lblRevenue, lblExpenditure, lblProfit;
    private JComboBox<String> cbMonth, cbYear, cbQuarter;
    private JButton btnViewMonth, btnViewQuarter, btnRefresh;

    public StatisticGUI() {
        setLayout(new BorderLayout(10, 15));
        setPreferredSize(new Dimension(950, 650)); 
        setBackground(new Color(240, 242, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        updateOverviewByMonth(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
    }

    private void initComponents() {
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlCards.setOpaque(false);
        pnlCards.setPreferredSize(new Dimension(0, 120));

        lblRevenue = createCard(pnlCards, "TỔNG DOANH THU", new Color(40, 167, 69)); 
        lblExpenditure = createCard(pnlCards, "TỔNG CHI PHÍ (NHẬP)", new Color(220, 53, 69)); 
        lblProfit = createCard(pnlCards, "LỢI NHUẬN THUẦN", new Color(0, 123, 255)); 

        JPanel pnlCenter = new JPanel(new BorderLayout(0, 15));
        pnlCenter.setOpaque(false);

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        pnlFilter.add(new JLabel("Tháng:"));
        cbMonth = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
        cbMonth.setSelectedItem(String.valueOf(LocalDate.now().getMonthValue()));
        cbMonth.setPreferredSize(new Dimension(70, 30)); 
        pnlFilter.add(cbMonth);

        pnlFilter.add(new JLabel("Năm:"));
        cbYear = new JComboBox<>(new String[]{"2023", "2024", "2025", "2026"});
        cbYear.setSelectedItem("2026");
        cbYear.setPreferredSize(new Dimension(90, 30));
        pnlFilter.add(cbYear);

        btnViewMonth = new JButton("Lọc theo Tháng");
        styleButton(btnViewMonth, new Color(108, 117, 125));
        btnViewMonth.setPreferredSize(new Dimension(150, 35)); 
        pnlFilter.add(btnViewMonth);

        JSeparator sep = new JSeparator(JSeparator.VERTICAL);
        sep.setPreferredSize(new Dimension(2, 30));
        pnlFilter.add(sep);

        pnlFilter.add(new JLabel("Quý:"));
        cbQuarter = new JComboBox<>(new String[]{"1", "2", "3", "4"});
        cbQuarter.setPreferredSize(new Dimension(70, 30));
        pnlFilter.add(cbQuarter);

        btnViewQuarter = new JButton("Lọc theo Quý");
        styleButton(btnViewQuarter, new Color(108, 117, 125));
        btnViewQuarter.setPreferredSize(new Dimension(150, 35));
        pnlFilter.add(btnViewQuarter);

        btnRefresh = new JButton("Làm Mới");
        styleButton(btnRefresh, new Color(40, 167, 69));
        btnRefresh.setPreferredSize(new Dimension(120, 35));
        pnlFilter.add(btnRefresh);

        String[] columns = {"Mã SP", "Tên Sản Phẩm", "Số Lượng Bán", "Doanh Thu", "Lợi Nhuận Dự Tính"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblReport = new JTable(model);
        tblReport.setRowHeight(35);
        tblReport.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        for(int i=2; i<=4; i++) tblReport.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);

        JScrollPane scroll = new JScrollPane(tblReport);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        pnlCenter.add(pnlFilter, BorderLayout.NORTH);
        pnlCenter.add(scroll, BorderLayout.CENTER);

        add(pnlCards, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);

        
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
            updateOverviewByQuarter(q, y);
        });

        btnRefresh.addActionListener(e -> {
            model.setRowCount(0);
            updateOverviewByMonth(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
        });
    }

    private JLabel createCard(JPanel parent, String title, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(new Color(255, 255, 255, 200));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblValue = new JLabel("0 VNĐ");
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(10));
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
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void fillTable(ArrayList<Object[]> list) {
        model.setRowCount(0);
        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu trong khoảng thời gian này!");
            return;
        }
        for (Object[] row : list) {
            model.addRow(new Object[]{
                row[0], row[1], row[2], 
                df.format(row[3]), 
                df.format(row[4])
            });
        }
    }

    private void updateOverviewByMonth(int m, int y) {
        String from = y + "-" + (m < 10 ? "0" + m : m) + "-01";
        LocalDate end = java.time.YearMonth.of(y, m).atEndOfMonth();
        Object[] data = statisticBUS.getOverview(from, end.toString());
        displayOverview(data);
    }

    private void updateOverviewByQuarter(int q, int y) {
        String from = "", to = "";
        switch (q) {
            case 1 -> { from = y+"-01-01"; to = y+"-03-31"; }
            case 2 -> { from = y+"-04-01"; to = y+"-06-30"; }
            case 3 -> { from = y+"-07-01"; to = y+"-09-30"; }
            case 4 -> { from = y+"-10-01"; to = y+"-12-31"; }
        }
        Object[] data = statisticBUS.getOverview(from, to);
        displayOverview(data);
    }

    private void displayOverview(Object[] data) {
        if(data != null && data.length >= 3) {
            lblRevenue.setText(df.format(data[0]));
            lblExpenditure.setText(df.format(data[1]));
            lblProfit.setText(df.format(data[2]));
        }
    }
}