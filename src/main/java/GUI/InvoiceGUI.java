package GUI;

import BUS.InvoiceBUS;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.text.DecimalFormat;

public class InvoiceGUI extends JPanel {
    private InvoiceBUS invoiceBUS = new InvoiceBUS();
    private DecimalFormat df = new DecimalFormat("#,###");
    
    private JTable tblInvoice, tblDetails;
    private DefaultTableModel modelInvoice, modelDetails;
    private JTextField txtSearch;
    private JButton btnSearch, btnDelete, btnRefresh;

    public InvoiceGUI() {
        setLayout(new BorderLayout(0, 10)); 
        setPreferredSize(new Dimension(950, 650)); 
        setBackground(new Color(240, 242, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        loadInvoiceData();
        setupEvents();
    }

    private void initComponents() {
        // --- PHẦN NORTH: TÌM KIẾM ---
        JPanel pnlNorth = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlNorth.setBackground(Color.WHITE); // Đặt nền trắng cho thanh tìm kiếm
        pnlNorth.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        JLabel lblSearch = new JLabel("Mã Hóa Đơn:");
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnlNorth.add(lblSearch);

        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(200, 35)); // Tăng chiều cao ô nhập
        pnlNorth.add(txtSearch);

        btnSearch = new JButton("Tìm Kiếm");
        styleButton(btnSearch, new Color(0, 123, 255));
        btnSearch.setPreferredSize(new Dimension(120, 35)); // Fix kích thước hiển thị đủ chữ
        pnlNorth.add(btnSearch);

        btnRefresh = new JButton("Làm Mới");
        styleButton(btnRefresh, new Color(108, 117, 125));
        btnRefresh.setPreferredSize(new Dimension(120, 35)); // Fix kích thước hiển thị đủ chữ
        pnlNorth.add(btnRefresh);

        add(pnlNorth, BorderLayout.NORTH);

        // --- PHẦN CENTER: BẢNG DỮ LIỆU ---
        JPanel pnlCenter = new JPanel(new GridLayout(2, 1, 0, 10));
        pnlCenter.setOpaque(false);

        // 1. Bảng Hóa Đơn
        JPanel pnlInvoiceTable = new JPanel(new BorderLayout());
        pnlInvoiceTable.setOpaque(false);
        JLabel lblMain = new JLabel(" DANH SÁCH HÓA ĐƠN");
        lblMain.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMain.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        pnlInvoiceTable.add(lblMain, BorderLayout.NORTH);

        String[] colInv = {"Mã HĐ", "Mã KH", "Mã NV", "Ngày Lập", "Tổng Tiền (VNĐ)"};
        modelInvoice = new DefaultTableModel(colInv, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblInvoice = new JTable(modelInvoice);
        tblInvoice.setRowHeight(30);
        
        JScrollPane scrollInv = new JScrollPane(tblInvoice);
        pnlInvoiceTable.add(scrollInv, BorderLayout.CENTER);
        pnlCenter.add(pnlInvoiceTable);

        // 2. Bảng Chi Tiết
        JPanel pnlDetailTable = new JPanel(new BorderLayout());
        pnlDetailTable.setOpaque(false);
        JLabel lblSub = new JLabel(" CHI TIẾT SẢN PHẨM TRONG HÓA ĐƠN");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSub.setForeground(new Color(0, 102, 204));
        lblSub.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        pnlDetailTable.add(lblSub, BorderLayout.NORTH);

        String[] colDet = {"Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        modelDetails = new DefaultTableModel(colDet, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblDetails = new JTable(modelDetails);
        tblDetails.setRowHeight(30);
        
        JScrollPane scrollDet = new JScrollPane(tblDetails);
        pnlDetailTable.add(scrollDet, BorderLayout.CENTER);
        pnlCenter.add(pnlDetailTable);

        add(pnlCenter, BorderLayout.CENTER);

        // --- PHẦN SOUTH: NÚT XÓA ---
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        pnlSouth.setOpaque(false);
        btnDelete = new JButton("Xóa Hóa Đơn");
        styleButton(btnDelete, new Color(220, 53, 69));
        btnDelete.setPreferredSize(new Dimension(140, 40));
        pnlSouth.add(btnDelete);
        
        add(pnlSouth, BorderLayout.SOUTH);

        // Căn lề phải cho cột tiền
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblInvoice.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        tblDetails.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tblDetails.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); // Tắt viền mặc định của Windows
        btn.setOpaque(true);         // Quan trọng: Để màu nền hiển thị rõ
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupEvents() {
        tblInvoice.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblInvoice.getSelectedRow();
                if (row != -1) {
                    String id = tblInvoice.getValueAt(row, 0).toString();
                    loadDetailsData(id);
                }
            }
        });

        btnSearch.addActionListener(e -> {
            String id = txtSearch.getText().trim();
            if (id.isEmpty()) loadInvoiceData();
            else {
                fillTableInvoice(invoiceBUS.searchByID(id));
                modelDetails.setRowCount(0); 
            }
        });

        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadInvoiceData();
            modelDetails.setRowCount(0);
        });

        btnDelete.addActionListener(e -> {
            int row = tblInvoice.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn!");
                return;
            }
            String id = tblInvoice.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Xóa hóa đơn " + id + "?", "Cảnh báo", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, invoiceBUS.delete(id));
                loadInvoiceData();
                modelDetails.setRowCount(0);
            }
        });
    }

    private void fillTableInvoice(ArrayList<Object[]> list) {
        modelInvoice.setRowCount(0);
        if (list != null) {
            for (Object[] r : list) {
                Object[] rowData = new Object[5];
                rowData[0] = r[0]; rowData[1] = r[1]; rowData[2] = r[2]; rowData[3] = r[3];
                try {
                    rowData[4] = df.format(Double.parseDouble(r[4].toString()));
                } catch (Exception e) { rowData[4] = r[4]; }
                modelInvoice.addRow(rowData);
            }
        }
    }

    private void fillTableDetails(ArrayList<Object[]> list) {
        modelDetails.setRowCount(0);
        if (list != null) {
            for (Object[] r : list) {
                Object[] rowData = new Object[5];
                rowData[0] = r[0]; rowData[1] = r[1]; rowData[2] = r[2];
                try {
                    rowData[3] = df.format(Double.parseDouble(r[3].toString()));
                    rowData[4] = df.format(Double.parseDouble(r[4].toString()));
                } catch (Exception e) {
                    rowData[3] = r[3]; rowData[4] = r[4];
                }
                modelDetails.addRow(rowData);
            }
        }
    }

    private void loadInvoiceData() { fillTableInvoice(invoiceBUS.getAll()); }
    private void loadDetailsData(String id) { fillTableDetails(invoiceBUS.searchDetailByID(id)); }
}