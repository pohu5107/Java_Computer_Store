package GUI;

import BUS.CategoryBUS;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CategoryGUI extends JPanel {

    private CategoryBUS categoryBUS = new CategoryBUS();

    // Các thành phần giao diện
    private JTextField txtSearch, txtID, txtName, txtDescription;
    private JTable tblCategory;
    private DefaultTableModel modelCategory;
    private JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnRefresh;

    public CategoryGUI() {
        setLayout(new BorderLayout(0, 10)); 
        setPreferredSize(new Dimension(950, 650)); 
        setBackground(new Color(240, 242, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        loadData();      
        setupEvents();    
    }

    private void initComponents() { 
        // Panel phía trên chứa Tìm kiếm và Nhập liệu
        JPanel pnlTop = new JPanel(new BorderLayout(5, 5));
        pnlTop.setOpaque(false);
        pnlTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Thanh tìm kiếm - Chỉnh lại để không bị khuất chữ
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        txtSearch = new JTextField(30);
        txtSearch.setPreferredSize(new Dimension(300, 35));
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setText("Nhập tên hoặc mã danh mục (VD: LPT)...");
        
        btnSearch = new JButton("Tìm Kiếm");
        styleButton(btnSearch, new Color(0, 123, 255));
        btnSearch.setPreferredSize(new Dimension(120, 35)); // Đảm bảo đủ rộng

        btnRefresh = new JButton("Làm Mới");
        styleButton(btnRefresh, new Color(108, 117, 125));
        btnRefresh.setPreferredSize(new Dimension(120, 35)); // Đảm bảo đủ rộng

        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);
        pnlSearch.add(btnRefresh);
        pnlTop.add(pnlSearch, BorderLayout.NORTH);

        // 2. Khu vực nhập liệu
        JPanel box1 = new JPanel(new BorderLayout());
        box1.setBackground(Color.WHITE);
        box1.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Thông tin Danh mục", TitledBorder.LEFT, TitledBorder.TOP));

        JPanel pnlInput = new JPanel(new GridLayout(3, 2, 20, 15));
        pnlInput.setOpaque(false);
        pnlInput.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        pnlInput.add(new JLabel("Mã Danh Mục:"));
        txtID = new JTextField(); 
        txtID.setPreferredSize(new Dimension(0, 30));
        pnlInput.add(txtID);

        pnlInput.add(new JLabel("Tên Danh Mục:"));
        txtName = new JTextField(); pnlInput.add(txtName);

        pnlInput.add(new JLabel("Mô tả:"));
        txtDescription = new JTextField(); pnlInput.add(txtDescription);

        box1.add(pnlInput, BorderLayout.CENTER);

        // 3. Cụm nút bấm - Tăng kích thước để hiện đủ chữ "Thêm", "Sửa", "Xóa"
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlButtons.setOpaque(false);
        
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        
        styleButton(btnAdd, new Color(40, 167, 69));  // Xanh lá (Success)
        styleButton(btnUpdate, new Color(0, 123, 255)); // Xanh dương (Primary)
        styleButton(btnDelete, new Color(220, 53, 69));  // Đỏ (Danger)

        // Đặt kích thước cố định cho các nút chức năng chính
        Dimension btnSize = new Dimension(110, 40);
        btnAdd.setPreferredSize(btnSize);
        btnUpdate.setPreferredSize(btnSize);
        btnDelete.setPreferredSize(btnSize);

        pnlButtons.add(btnAdd);
        pnlButtons.add(btnUpdate);
        pnlButtons.add(btnDelete);
        box1.add(pnlButtons, BorderLayout.SOUTH);

        pnlTop.add(box1, BorderLayout.CENTER);
        add(pnlTop, BorderLayout.NORTH);

        // --- PHẦN GIỮA: BẢNG DỮ LIỆU ---
        JPanel box2 = new JPanel(new BorderLayout());
        box2.setBackground(Color.WHITE);
        box2.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Danh sách Danh mục", TitledBorder.LEFT, TitledBorder.TOP));

        String[] cols = {"Mã Danh Mục", "Tên Danh Mục", "Mô Tả"};
        modelCategory = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblCategory = new JTable(modelCategory);
        tblCategory.setRowHeight(30);
        tblCategory.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scroll = new JScrollPane(tblCategory);
        scroll.getViewport().setBackground(Color.WHITE);
        box2.add(scroll, BorderLayout.CENTER);
        
        add(box2, BorderLayout.CENTER);
    }

    // Hàm style nút bấm giúp nút hiển thị rõ màu và không bị mất chữ
    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); // Xóa viền mặc định
        btn.setOpaque(true);         // Ép hiển thị màu nền
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupEvents() {
        // Placeholder cho thanh tìm kiếm
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Nhập tên hoặc mã danh mục (VD: LPT)...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setForeground(Color.GRAY);
                    txtSearch.setText("Nhập tên hoặc mã danh mục (VD: LPT)...");
                }
            }
        });

        // Click bảng -> Đổ dữ liệu lên form
        tblCategory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblCategory.getSelectedRow();
                if (row != -1) {
                    txtID.setText(modelCategory.getValueAt(row, 0).toString());
                    txtName.setText(modelCategory.getValueAt(row, 1).toString());
                    Object desc = modelCategory.getValueAt(row, 2);
                    txtDescription.setText(desc != null ? desc.toString() : "");
                    txtID.setEditable(false);
                }
            }
        });

        // Nút Thêm
        btnAdd.addActionListener(e -> {
            String id = txtID.getText().trim();
            String name = txtName.getText().trim();
            String desc = txtDescription.getText().trim();
            if(id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã và Tên không được để trống!");
                return;
            }
            String msg = categoryBUS.add(id, name, desc);
            JOptionPane.showMessageDialog(this, msg);
            if (msg.toLowerCase().contains("thành công")) {
                loadData();
                clearForm();
            }
        });

        // Nút Sửa
        btnUpdate.addActionListener(e -> {
            String id = txtID.getText().trim();
            if (id.isEmpty()) { JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục!"); return; }
            String name = txtName.getText().trim();
            String desc = txtDescription.getText().trim();
            String msg = categoryBUS.update(id, name, desc);
            JOptionPane.showMessageDialog(this, msg);
            if (msg.toLowerCase().contains("thành công")) {
                loadData();
                clearForm();
            }
        });

        // Nút Xóa
        btnDelete.addActionListener(e -> {
            String id = txtID.getText().trim();
            if (id.isEmpty()) { JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục!"); return; }
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa danh mục " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String msg = categoryBUS.delete(id);
                JOptionPane.showMessageDialog(this, msg);
                if (msg.toLowerCase().contains("thành công")) {
                    loadData();
                    clearForm();
                }
            }
        });

        // Nút Tìm kiếm
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.equals("Nhập tên hoặc mã danh mục (VD: LPT)...") || keyword.isEmpty()) {
                loadData();
            } else {
                fillTable(categoryBUS.search(keyword));
            }
        });

        // Nút Làm mới
        btnRefresh.addActionListener(e -> { clearForm(); loadData(); });
    }

    private void fillTable(ArrayList<Object[]> list) {
        modelCategory.setRowCount(0);
        if (list != null) {
            for (Object[] row : list) modelCategory.addRow(row);
        }
    }

    private void loadData() {
        fillTable(categoryBUS.getAll());
    }

    private void clearForm() {
        txtID.setText("");
        txtID.setEditable(true);
        txtName.setText("");
        txtDescription.setText("");
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setText("Nhập tên hoặc mã danh mục (VD: LPT)...");
        tblCategory.clearSelection();
    }
}