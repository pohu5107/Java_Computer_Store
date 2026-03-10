/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;
import java.util.ArrayList;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import BUS.InvoiceBUS;

public class InvoiceGUI extends JFrame {

    private InvoiceBUS invoiceBUS = new InvoiceBUS();
    private DecimalFormat df = new DecimalFormat("#,###");
    
    private JTable tblInvoice;
    private DefaultTableModel modelInvoice;
    private JTable tblDetails;
    private DefaultTableModel modelDetails;
    private JTextField txtSearch;
    private JButton btnSearch, btnDelete, btnRefresh;
    
    public InvoiceGUI() {
        initComponents();
        loadInvoiceData();
    }

    private void initComponents() {

        setTitle("Quản Lý Hóa Đơn - Java Computer Store");
        setSize(950, 640);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblSearch = new JLabel("Ma Hoa Don");
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

        JLabel lblMain = new JLabel("DANH SÁCH HÓA ĐƠN");
        lblMain.setBounds(20, 65, 250, 25);
        lblMain.setFont(new Font("Arial", Font.BOLD, 14));
        add(lblMain);
        
String[] colInv = {"Mã HĐ", "Mã KH", "Mã NV", "Ngày Lập", "Tổng Tiền (VNĐ)"};
        modelInvoice = new DefaultTableModel(colInv, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblInvoice = new JTable(modelInvoice);
        tblInvoice.setRowHeight(25);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        JScrollPane scrollInv = new JScrollPane(tblInvoice);
        scrollInv.setBounds(20, 95, 890, 180);
        add(scrollInv);

        // Định dạng cột tiền cho bảng chính sau khi add vào ScrollPane
        tblInvoice.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        JLabel lblSub = new JLabel("CHI TIẾT SẢN PHẨM TRONG HÓA ĐƠN");
        lblSub.setBounds(20, 300, 400, 25);
        lblSub.setFont(new Font("Arial", Font.BOLD, 14));
        lblSub.setForeground(new Color(0, 102, 204));
        add(lblSub);

        String[] colDet = {"Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        modelDetails = new DefaultTableModel(colDet, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblDetails = new JTable(modelDetails);
        tblDetails.setRowHeight(25);
        
        JScrollPane scrollDet = new JScrollPane(tblDetails);
        scrollDet.setBounds(20, 330, 890, 180);
        add(scrollDet);
        
        // Định dạng cột tiền cho bảng chi tiết
        tblDetails.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tblDetails.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        btnDelete = new JButton("Xóa Hóa Đơn");
        btnDelete.setBounds(790, 530, 120, 35);
        btnDelete.setBackground(new Color(180, 0, 0)); // Màu đỏ đậm hơn
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("Arial", Font.BOLD, 12));
        btnDelete.setFocusPainted(false);
        btnDelete.setOpaque(true);
        btnDelete.setBorderPainted(false);
        add(btnDelete);

        // Sự kiện
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
                rowData[0] = r[0]; // ID
                rowData[1] = r[1]; // CustID
                rowData[2] = r[2]; // StaffID
                rowData[3] = r[3]; // Date
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
                rowData[0] = r[0];
                rowData[1] = r[1];
                rowData[2] = r[2];
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

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e) {}
        new InvoiceGUI().setVisible(true);
    }
}