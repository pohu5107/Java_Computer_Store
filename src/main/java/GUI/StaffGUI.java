package GUI;

import BUS.StaffBUS;
import com.toedter.calendar.JDateChooser; // Import thư viện lịch

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StaffGUI extends JFrame {

    private StaffBUS staffBUS = new StaffBUS();

    private JTextField txtSearch, txtID, txtFirstName, txtLastName, txtPhone;
    private JDateChooser dateChooser; // Thay thế txtDate bằng JDateChooser
    private JRadioButton radMale, radFemale;
    private ButtonGroup bgGender;
    private JTable tblStaff;
    private DefaultTableModel modelStaff;
    private JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnRefresh;

    public StaffGUI() {
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setTitle("Quản Lý Nhân Viên");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. thông tin
        JPanel pnlTop = new JPanel(new BorderLayout(5, 5));
        pnlTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Panel Search (Bên ngoài Box 1)
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(30);
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setText("Nhập tên hoặc mã nhân viên (VD: NV01)...");
        
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Nhập tên hoặc mã nhân viên (VD: NV01)...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setForeground(Color.GRAY);
                    txtSearch.setText("Nhập tên hoặc mã nhân viên (VD: NV01)...");
                }
            }
        });

        btnSearch = new JButton("Tìm Kiếm");
        btnRefresh = new JButton("Làm Mới");
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);
        pnlSearch.add(btnRefresh);
        
        pnlTop.add(pnlSearch, BorderLayout.NORTH);

        // 2. Box 1: Điền thông tin
        JPanel box1 = new JPanel(new BorderLayout());
        box1.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Thông tin nhân viên", TitledBorder.LEFT, TitledBorder.TOP));

        JPanel pnlInput = new JPanel(new GridLayout(3, 4, 10, 15));
        pnlInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnlInput.add(new JLabel("Mã Nhân Viên:"));
        txtID = new JTextField();
        pnlInput.add(txtID);

        pnlInput.add(new JLabel("Họ:"));
        txtFirstName = new JTextField();
        pnlInput.add(txtFirstName);

        pnlInput.add(new JLabel("Tên:"));
        txtLastName = new JTextField();
        pnlInput.add(txtLastName);

        pnlInput.add(new JLabel("Giới tính:"));
        JPanel pnlGender = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radMale = new JRadioButton("Nam");
        radFemale = new JRadioButton("Nữ");
        radMale.setSelected(true);
        bgGender = new ButtonGroup();
        bgGender.add(radMale);
        bgGender.add(radFemale);
        pnlGender.add(radMale);
        pnlGender.add(radFemale);
        pnlInput.add(pnlGender);

        pnlInput.add(new JLabel("Ngày sinh:"));

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd"); // Quy định format hiển thị
        pnlInput.add(dateChooser);

        pnlInput.add(new JLabel("Số điện thoại:"));
        txtPhone = new JTextField();
        pnlInput.add(txtPhone);

        box1.add(pnlInput, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        
        btnAdd.setBackground(new Color(34, 139, 34)); btnAdd.setForeground(Color.BLACK);
        btnUpdate.setBackground(new Color(0, 102, 204)); btnUpdate.setForeground(Color.BLACK);
        btnDelete.setBackground(new Color(204, 0, 0)); btnDelete.setForeground(Color.BLACK);

        pnlButtons.add(btnAdd);
        pnlButtons.add(btnUpdate);
        pnlButtons.add(btnDelete);
        
        box1.add(pnlButtons, BorderLayout.SOUTH);

        pnlTop.add(box1, BorderLayout.CENTER);
        add(pnlTop, BorderLayout.NORTH);

        // 2. Bảng dữ liệu
        JPanel box2 = new JPanel(new BorderLayout());
        box2.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Danh sách nhân viên", TitledBorder.LEFT, TitledBorder.TOP));

        String[] cols = {"Mã NV", "Họ", "Tên", "Giới Tính", "Ngày Sinh", "SĐT"};
        modelStaff = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblStaff = new JTable(modelStaff);
        tblStaff.setRowHeight(25);
        box2.add(new JScrollPane(tblStaff), BorderLayout.CENTER);
        
        add(box2, BorderLayout.CENTER);

        
        // Click vào bảng -> đẩy lên form
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
                    
                    // Logic xử lý đẩy ngày từ JTable lên JDateChooser
                    Object dateObj = modelStaff.getValueAt(row, 4);
                    if (dateObj != null) {
                        try {
                            java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateObj.toString());
                            dateChooser.setDate(date);
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        dateChooser.setDate(null);
                    }
                    
                    Object phoneObj = modelStaff.getValueAt(row, 5);
                    txtPhone.setText(phoneObj != null ? phoneObj.toString() : "");
                    
                    txtID.setEditable(false);
                }
            }
        });

        // Nút thêm
        btnAdd.addActionListener(e -> {
            String id = txtID.getText().trim();
            String first = txtFirstName.getText().trim();
            String last = txtLastName.getText().trim();
            String gender = radMale.isSelected() ? "Nam" : "Nữ";
            String phone = txtPhone.getText().trim();
            
            // Lấy ngày từ JDateChooser
            java.util.Date utilDate = dateChooser.getDate();
            if (utilDate == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày sinh từ lịch!");
                return;
            }
            Date birthDate = new Date(utilDate.getTime()); // Ép kiểu sang java.sql.Date

            String msg = staffBUS.add(id, first, last, gender, birthDate, phone);
            JOptionPane.showMessageDialog(this, msg);
            if (msg.contains("thành công")) {
                loadDataToTable();
                clearForm();
            }
        });

        // Nút sửa
        btnUpdate.addActionListener(e -> {
            String id = txtID.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!");
                return;
            }
            String first = txtFirstName.getText().trim();
            String last = txtLastName.getText().trim();
            String gender = radMale.isSelected() ? "Nam" : "Nữ";
            String phone = txtPhone.getText().trim();
            
            java.util.Date utilDate = dateChooser.getDate();
            if (utilDate == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày sinh từ lịch!");
                return;
            }
            Date birthDate = new Date(utilDate.getTime());

            String msg = staffBUS.update(id, first, last, gender, birthDate, phone);
            JOptionPane.showMessageDialog(this, msg);
            if (msg.contains("thành công")) {
                loadDataToTable();
                clearForm();
            }
        });

        // Nút xóa
        btnDelete.addActionListener(e -> {
            String id = txtID.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String msg = staffBUS.delete(id);
                JOptionPane.showMessageDialog(this, msg);
                if (msg.contains("thành công")) {
                    loadDataToTable();
                    clearForm();
                }
            }
        });

        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.equals("Nhập tên hoặc mã nhân viên (VD: NV01)...") || keyword.isEmpty()) {
                loadDataToTable();
            } else {
                fillTable(staffBUS.search(keyword));
            }
        });

        btnRefresh.addActionListener(e -> {
            clearForm();
            loadDataToTable();
        });
    }

    private void fillTable(ArrayList<Object[]> list) {
        modelStaff.setRowCount(0);
        for (Object[] row : list) {
            modelStaff.addRow(row);
        }
    }

    private void loadDataToTable() {
        fillTable(staffBUS.getAll());
    }

    private void clearForm() {
        txtID.setText("");
        txtID.setEditable(true);
        txtFirstName.setText("");
        txtLastName.setText("");
        radMale.setSelected(true);
        dateChooser.setDate(null); // Xóa ngày đã chọn trên lịch
        txtPhone.setText("");
        
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setText("Nhập tên hoặc mã nhân viên (VD: NV01)...");
        tblStaff.clearSelection();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        new StaffGUI().setVisible(true);
    }
}