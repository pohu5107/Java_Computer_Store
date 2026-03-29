package GUI;

import BUS.CustomerBUS;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomerGUI extends JPanel {
    private CustomerBUS custBUS = new CustomerBUS();
    private JTable tblCustomer;
    private DefaultTableModel model;
    private JTextField txtID, txtFirstName, txtLastName, txtPhone, txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnSearch;

    public CustomerGUI() {
        setLayout(new BorderLayout(0, 10)); 
        setPreferredSize(new Dimension(950, 650)); 
        setBackground(new Color(240, 242, 245));

        initComponents();
        loadData();      
        setupEvents();
    }

    private void initComponents() {
        JPanel pnlNorth = new JPanel(null);
        pnlNorth.setPreferredSize(new Dimension(950, 230)); 
        pnlNorth.setOpaque(false);

        JPanel pnlInput = new JPanel(null);
        pnlInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Thông tin khách hàng"));
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBounds(20, 10, 895, 120);
        
        // SỬA NHÃN: Đổi vị trí và tên gọi để khớp với ý đồ của Phát
        addLabelTextField(pnlInput, "Mã Khách Hàng:", txtID = new JTextField(), 20, 30);
        addLabelTextField(pnlInput, "Họ và Tên đệm:", txtLastName = new JTextField(), 20, 70); // Lưu vào LastName
        
        addLabelTextField(pnlInput, "Tên:", txtFirstName = new JTextField(), 450, 30); // Lưu vào FirstName
        addLabelTextField(pnlInput, "Số Điện Thoại:", txtPhone = new JTextField(), 450, 70);
        
        pnlNorth.add(pnlInput);

        // --- Tìm kiếm & Nút bấm ---
        JLabel lblS = new JLabel("Tìm kiếm:");
        lblS.setBounds(30, 150, 70, 30);
        pnlNorth.add(lblS);

        txtSearch = new JTextField();
        txtSearch.setBounds(100, 150, 200, 30);
        pnlNorth.add(txtSearch);

        btnSearch = new JButton("Tìm Kiếm");
        btnSearch.setBounds(310, 150, 100, 30);
        styleButton(btnSearch, new Color(240, 240, 240), Color.BLACK);
        pnlNorth.add(btnSearch);

        btnAdd = new JButton("Thêm Mới");
        btnAdd.setBounds(435, 145, 110, 40);
        styleButton(btnAdd, new Color(40, 167, 69), Color.WHITE);

        btnUpdate = new JButton("Cập Nhật");
        btnUpdate.setBounds(555, 145, 110, 40);
        styleButton(btnUpdate, new Color(0, 123, 255), Color.WHITE);

        btnDelete = new JButton("Xóa");
        btnDelete.setBounds(675, 145, 110, 40);
        styleButton(btnDelete, new Color(220, 53, 69), Color.WHITE);

        btnRefresh = new JButton("Làm Mới");
        btnRefresh.setBounds(795, 145, 110, 40);
        styleButton(btnRefresh, new Color(108, 117, 125), Color.WHITE);

        pnlNorth.add(btnAdd); pnlNorth.add(btnUpdate); pnlNorth.add(btnDelete); pnlNorth.add(btnRefresh);
        add(pnlNorth, BorderLayout.NORTH);

        // --- Bảng hiển thị ---
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        pnlCenter.setOpaque(false);

        // Đổi thứ tự cột trên bảng để nhìn cho thuận mắt: Họ tên đệm -> Tên
        String[] columns = {"Mã KH", "Họ và Tên đệm", "Tên", "Số Điện Thoại"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblCustomer = new JTable(model);
        tblCustomer.setRowHeight(30);
        pnlCenter.add(new JScrollPane(tblCustomer), BorderLayout.CENTER);
        
        add(pnlCenter, BorderLayout.CENTER);
    }

    private void setupEvents() {
        tblCustomer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblCustomer.getSelectedRow();
                if (row != -1) {
                    txtID.setText(safeToString(model.getValueAt(row, 0)));
                    // Lấy lại đúng cột: Cột 1 là LastName, Cột 2 là FirstName
                    txtLastName.setText(safeToString(model.getValueAt(row, 1)));
                    txtFirstName.setText(safeToString(model.getValueAt(row, 2)));
                    txtPhone.setText(safeToString(model.getValueAt(row, 3)));
                    
                    txtID.setEditable(false);
                    txtID.setBackground(new Color(245, 245, 245));
                }
            }
        });

        btnAdd.addActionListener(e -> {
            // Truyền đúng thứ tự biến vào hàm add(id, firstName, lastName, phone)
            String msg = custBUS.add(txtID.getText(), txtFirstName.getText(), txtLastName.getText(), txtPhone.getText());
            JOptionPane.showMessageDialog(this, msg);
            loadData();
        });

        btnUpdate.addActionListener(e -> {
            String msg = custBUS.update(txtID.getText(), txtFirstName.getText(), txtLastName.getText(), txtPhone.getText());
            JOptionPane.showMessageDialog(this, msg);
            loadData();
        });

        btnDelete.addActionListener(e -> {
            String id = txtID.getText();
            if (id.isEmpty()) return;
            if (JOptionPane.showConfirmDialog(this, "Xác nhận xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0) {
                JOptionPane.showMessageDialog(this, custBUS.delete(id));
                refreshForm();
                loadData();
            }
        });

        btnSearch.addActionListener(e -> fillTable(custBUS.search(txtSearch.getText().trim())));
        btnRefresh.addActionListener(e -> { refreshForm(); loadData(); });
    }

    private void styleButton(JButton btn, Color bgColor, Color fgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); 
        btn.setOpaque(true);     
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void addLabelTextField(JPanel p, String label, JTextField t, int x, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(x, y, 110, 25);
        p.add(lbl);
        t.setBounds(x + 110, y, 250, 25);
        p.add(t);
    }

    private void loadData() {
        DefaultTableModel data = custBUS.getAll();
        model.setRowCount(0);
        for (int i = 0; i < data.getRowCount(); i++) {
            // Chú ý: Dữ liệu từ DAO/BUS trả về thứ tự: ID, FirstName, LastName, Phone
            // Chúng ta muốn hiển thị: ID, LastName, FirstName, Phone
            Object[] row = new Object[4];
            row[0] = data.getValueAt(i, 0); // ID
            row[1] = data.getValueAt(i, 2); // LastName (Họ tên đệm)
            row[2] = data.getValueAt(i, 1); // FirstName (Tên)
            row[3] = data.getValueAt(i, 3); // Phone
            model.addRow(row);
        }
    }

    private void fillTable(ArrayList<Object[]> list) {
        model.setRowCount(0);
        if (list != null) {
            for (Object[] obj : list) {
                // Tương tự hàm loadData, đổi vị trí 1 và 2 để hiển thị Họ trước Tên sau
                Object[] row = {obj[0], obj[2], obj[1], obj[3]};
                model.addRow(row);
            }
        }
    }

    private void refreshForm() {
        txtID.setText(""); txtFirstName.setText(""); txtLastName.setText(""); txtPhone.setText("");
        txtSearch.setText("");
        txtID.setEditable(true); txtID.setBackground(Color.WHITE);
        tblCustomer.clearSelection();
    }

    private String safeToString(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }
}