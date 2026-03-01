package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import BUS.CustomerBUS;

public class CustomerGUI extends JFrame {

    private JTable table;
    private JScrollPane scrollPane;
    private JButton btnRefresh;
    private CustomerBUS customerBus = new CustomerBUS();

    public CustomerGUI() {
        initComponents();
        loadData();
    }

    private void initComponents() {

        setTitle("Quản Lý Khách Hàng - Java Computer Store");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        table = new JTable();
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        btnRefresh = new JButton("Tải lại dữ liệu");
        JPanel panelBottom = new JPanel();
        panelBottom.add(btnRefresh);
        add(panelBottom, BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> loadData());
    }

    private void loadData() {
        DefaultTableModel model = customerBus.getAllCustomers();
        if (model != null) {
            table.setModel(model);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CustomerGUI().setVisible(true);
        });
    }
}