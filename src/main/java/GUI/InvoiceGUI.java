package GUI;

import BUS.InvoiceBUS;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;

public class InvoiceGUI extends JFrame {
    private InvoiceBUS invoiceBUS = new InvoiceBUS();
    
    // Components cho Bảng Hóa Đơn (Main)
    private JTable tblInvoice;
    private DefaultTableModel modelInvoice;
    
    // Components cho Bảng Chi Tiết (Sub)
    private JTable tblDetails;
    private DefaultTableModel modelDetails;
    
    private JTextField txtSearch;
    private JButton btnSearch, btnDelete, btnRefresh;

    public InvoiceGUI() {
        initComponents();
        loadInvoiceData(); // Tự động đổ dữ liệu khi mở giao diện
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Hóa Đơn");
        setSize(900, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // --- KHU VỰC TÌM KIẾM ---
        JLabel lblSearch = new JLabel("Mã Hóa Đơn:");
        lblSearch.setBounds(20, 20, 100, 25);
        add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setBounds(110, 20, 200, 25);
        add(txtSearch);

        btnSearch = new JButton("Tìm Kiếm");
        btnSearch.setBounds(320, 20, 100, 25);
        add(btnSearch);

        btnRefresh = new JButton("Làm Mới");
        btnRefresh.setBounds(430, 20, 100, 25);
        add(btnRefresh);

        // --- BẢNG HÓA ĐƠN (MAIN TABLE) ---
        JLabel lblMain = new JLabel("DANH SÁCH HÓA ĐƠN");
        lblMain.setBounds(20, 65, 250, 25);
        lblMain.setFont(new Font("Arial", Font.BOLD, 14));
        add(lblMain);

        String[] colInv = {"Mã HĐ", "Mã KH", "Ngày Lập", "Tổng Tiền (VNĐ)"};
        modelInvoice = new DefaultTableModel(colInv, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Chặn không cho sửa trực tiếp trên ô của bảng
            }
        };
        tblInvoice = new JTable(modelInvoice);
        JScrollPane scrollInv = new JScrollPane(tblInvoice);
        scrollInv.setBounds(20, 95, 840, 180);
        add(scrollInv);

        // --- BẢNG CHI TIẾT (SUB TABLE) ---
        JLabel lblSub = new JLabel("CHI TIẾT SẢN PHẨM TRONG HÓA ĐƠN");
        lblSub.setBounds(20, 300, 300, 25);
        lblSub.setFont(new Font("Arial", Font.BOLD, 14));
        lblSub.setForeground(new Color(0, 102, 204));
        add(lblSub);

        String[] colDet = {"Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        modelDetails = new DefaultTableModel(colDet, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDetails = new JTable(modelDetails);
        JScrollPane scrollDet = new JScrollPane(tblDetails);
        scrollDet.setBounds(20, 330, 840, 180);
        add(scrollDet);

        // --- NÚT CHỨC NĂNG ---
        btnDelete = new JButton("Xóa Hóa Đơn");
        btnDelete.setBounds(740, 530, 120, 35);
        btnDelete.setBackground(new Color(204, 0, 0));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        add(btnDelete);

        // --- XỬ LÝ SỰ KIỆN ---

        // 1. Click vào bảng trên hiện chi tiết bảng dưới
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

        // 2. Tìm kiếm (Lọc trên ArrayList của BUS)
        btnSearch.addActionListener(e -> {
            String id = txtSearch.getText().trim();
            if (id.isEmpty()) {
                loadInvoiceData();
            } else {
                fillTable(invoiceBUS.searchByID(id), modelInvoice);
                modelDetails.setRowCount(0); 
            }
        });

        // 3. Làm mới dữ liệu
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadInvoiceData();
            modelDetails.setRowCount(0);
        });

        // 4. Xóa hóa đơn
        btnDelete.addActionListener(e -> {
            int row = tblInvoice.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa!");
                return;
            }
            
            String id = tblInvoice.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc muốn xóa hóa đơn " + id + "?", 
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                String result = invoiceBUS.delete(id);
                JOptionPane.showMessageDialog(this, result);
                loadInvoiceData();
                modelDetails.setRowCount(0);
            }
        });
    }

    private void fillTable(ArrayList<Object[]> list, DefaultTableModel model) {
        model.setRowCount(0);
        if (list != null) {
            for (Object[] r : list) {
                model.addRow(r);
            }
        }
    }

    private void loadInvoiceData() {
        fillTable(invoiceBUS.getAll(), modelInvoice);
    }

    private void loadDetailsData(String id) {
        fillTable(invoiceBUS.searchDetailByID(id), modelDetails);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e) {}
        SwingUtilities.invokeLater(() -> {
            new InvoiceGUI().setVisible(true);
        });
    }
}