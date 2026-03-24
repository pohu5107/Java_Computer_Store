package GUI;

import BUS.StaffBUS;
import com.toedter.calendar.JDateChooser; 

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StaffGUI extends JPanel {
    private StaffBUS staffBUS = new StaffBUS();
    private JTextField txtSearch, txtID, txtFirstName, txtLastName, txtPhone;
    private JDateChooser dateChooser; 
    private JRadioButton radMale, radFemale;
    private ButtonGroup bgGender;
    private JTable tblStaff;
    private DefaultTableModel modelStaff;
    private JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnRefresh;

    public StaffGUI() {
        setLayout(new BorderLayout(0, 10)); 
        setPreferredSize(new Dimension(950, 650)); 
        setBackground(new Color(240, 242, 245));

        initComponents();
        loadDataToTable();
        setupEvents();
    }

    private void initComponents() {
        // --- PHẦN TRÊN (NORTH): TÌM KIẾM VÀ NHẬP LIỆU ---
        JPanel pnlNorth = new JPanel(new BorderLayout(10, 10));
        pnlNorth.setOpaque(false);
        pnlNorth.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        // Thanh tìm kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlSearch.setOpaque(false);
        
        txtSearch = new JTextField(30);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setText("Nhập tên hoặc mã nhân viên...");
        txtSearch.setForeground(Color.GRAY);
        
        btnSearch = new JButton("Tìm Kiếm");
        styleButton(btnSearch, new Color(0, 102, 204));
        
        btnRefresh = new JButton("Làm Mới");
        styleButton(btnRefresh, new Color(108, 117, 125));

        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);
        pnlSearch.add(btnRefresh);
        pnlNorth.add(pnlSearch, BorderLayout.NORTH);

        // Form nhập liệu
        JPanel pnlInputWrap = new JPanel(new BorderLayout());
        pnlInputWrap.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                "THÔNG TIN NHÂN VIÊN", TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 13), new Color(0, 102, 204)));
        pnlInputWrap.setBackground(Color.WHITE);

        JPanel pnlFields = new JPanel(new GridLayout(3, 4, 20, 15));
        pnlFields.setOpaque(false);
        pnlFields.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        pnlFields.add(new JLabel("Mã Nhân Viên:"));
        txtID = new JTextField(); pnlFields.add(txtID);

        pnlFields.add(new JLabel("Họ:"));
        txtFirstName = new JTextField(); pnlFields.add(txtFirstName);

        pnlFields.add(new JLabel("Tên:"));
        txtLastName = new JTextField(); pnlFields.add(txtLastName);

        pnlFields.add(new JLabel("Giới tính:"));
        JPanel pnlGender = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlGender.setOpaque(false);
        radMale = new JRadioButton("Nam"); radMale.setSelected(true);
        radFemale = new JRadioButton("Nữ");
        bgGender = new ButtonGroup(); bgGender.add(radMale); bgGender.add(radFemale);
        pnlGender.add(radMale); pnlGender.add(radFemale);
        pnlFields.add(pnlGender);

        pnlFields.add(new JLabel("Ngày sinh:"));
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd"); 
        pnlFields.add(dateChooser);

        pnlFields.add(new JLabel("Số điện thoại:"));
        txtPhone = new JTextField(); pnlFields.add(txtPhone);

        pnlInputWrap.add(pnlFields, BorderLayout.CENTER);

        // Nút chức năng (Add/Update/Delete)
        JPanel pnlAction = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        pnlAction.setOpaque(false);
        
        btnAdd = new JButton("Thêm Mới");
        styleButton(btnAdd, new Color(40, 167, 69));
        
        btnUpdate = new JButton("Cập Nhật");
        styleButton(btnUpdate, new Color(255, 153, 0));
        
        btnDelete = new JButton("Xóa");
        styleButton(btnDelete, new Color(220, 53, 69));

        pnlAction.add(btnAdd);
        pnlAction.add(btnUpdate);
        pnlAction.add(btnDelete);
        pnlInputWrap.add(pnlAction, BorderLayout.SOUTH);

        pnlNorth.add(pnlInputWrap, BorderLayout.CENTER);
        add(pnlNorth, BorderLayout.NORTH);

        // --- PHẦN GIỮA (CENTER): BẢNG DANH SÁCH ---
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setOpaque(false);
        pnlTable.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        String[] cols = {"Mã NV", "Họ", "Tên", "Giới Tính", "Ngày Sinh", "SĐT"};
        modelStaff = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblStaff = new JTable(modelStaff);
        tblStaff.setRowHeight(30);
        tblStaff.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scroll = new JScrollPane(tblStaff);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        pnlTable.add(scroll, BorderLayout.CENTER);
        
        add(pnlTable, BorderLayout.CENTER);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(110, 35));
    }

    private void setupEvents() {
        // Xử lý Placeholder cho ô tìm kiếm
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Nhập tên hoặc mã nhân viên...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setForeground(Color.GRAY);
                    txtSearch.setText("Nhập tên hoặc mã nhân viên...");
                }
            }
        });

        // Click bảng để đổ dữ liệu lên form
        tblStaff.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblStaff.getSelectedRow();
                if (row != -1) {
                    txtID.setText(modelStaff.getValueAt(row, 0).toString());
                    txtFirstName.setText(modelStaff.getValueAt(row, 1).toString());
                    txtLastName.setText(modelStaff.getValueAt(row, 2).toString());
                    String gender = modelStaff.getValueAt(row, 3).toString();
                    if (gender.equalsIgnoreCase("Nam")) radMale.setSelected(true);
                    else radFemale.setSelected(true);
                    
                    try {
                        java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(modelStaff.getValueAt(row, 4).toString());
                        dateChooser.setDate(date);
                    } catch (Exception ex) { dateChooser.setDate(null); }
                    
                    txtPhone.setText(modelStaff.getValueAt(row, 5).toString());
                    txtID.setEditable(false);
                    txtID.setBackground(new Color(235, 235, 235));
                }
            }
        });

        btnAdd.addActionListener(e -> {
            if(validateForm()) {
                String id = txtID.getText().trim();
                String first = txtFirstName.getText().trim();
                String last = txtLastName.getText().trim();
                String gender = radMale.isSelected() ? "Nam" : "Nữ";
                String phone = txtPhone.getText().trim();
                Date birthDate = new Date(dateChooser.getDate().getTime());
                
                String msg = staffBUS.add(id, first, last, gender, birthDate, phone);
                JOptionPane.showMessageDialog(this, msg);
                if (msg.contains("thành công")) { loadDataToTable(); clearForm(); }
            }
        });

        btnUpdate.addActionListener(e -> {
            String id = txtID.getText().trim();
            if (id.isEmpty()) { JOptionPane.showMessageDialog(this, "Chọn nhân viên cần sửa!"); return; }
            if(validateForm()) {
                String first = txtFirstName.getText().trim();
                String last = txtLastName.getText().trim();
                String gender = radMale.isSelected() ? "Nam" : "Nữ";
                String phone = txtPhone.getText().trim();
                Date birthDate = new Date(dateChooser.getDate().getTime());

                String msg = staffBUS.update(id, first, last, gender, birthDate, phone);
                JOptionPane.showMessageDialog(this, msg);
                if (msg.contains("thành công")) { loadDataToTable(); clearForm(); }
            }
        });

        btnDelete.addActionListener(e -> {
            String id = txtID.getText().trim();
            if (id.isEmpty()) { JOptionPane.showMessageDialog(this, "Chọn nhân viên cần xóa!"); return; }
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa nhân viên " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String msg = staffBUS.delete(id);
                JOptionPane.showMessageDialog(this, msg);
                if (msg.contains("thành công")) { loadDataToTable(); clearForm(); }
            }
        });

        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.equals("Nhập tên hoặc mã nhân viên...") || keyword.isEmpty()) {
                loadDataToTable();
            } else {
                fillTable(staffBUS.search(keyword));
            }
        });

        btnRefresh.addActionListener(e -> { clearForm(); loadDataToTable(); });
    }

    private boolean validateForm() {
        if(txtID.getText().isEmpty() || txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() || dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return false;
        }
        return true;
    }

    private void fillTable(ArrayList<Object[]> list) {
        modelStaff.setRowCount(0);
        for (Object[] row : list) modelStaff.addRow(row);
    }

    private void loadDataToTable() { fillTable(staffBUS.getAll()); }

    private void clearForm() {
        txtID.setText(""); txtID.setEditable(true);
        txtID.setBackground(Color.WHITE);
        txtFirstName.setText(""); txtLastName.setText("");
        radMale.setSelected(true); dateChooser.setDate(null);
        txtPhone.setText("");
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setText("Nhập tên hoặc mã nhân viên...");
        tblStaff.clearSelection();
    }
}