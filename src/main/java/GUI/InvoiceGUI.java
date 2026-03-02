/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import BUS.InvoiceBUS;

public class InvoiceGUI extends JFrame {

    private JTable table;
    private JScrollPane scrollPane;
    private JButton btnRefresh;
    private InvoiceBUS invoiceBus = new InvoiceBUS();

    public InvoiceGUI() {
        initComponents();
        loadData();
    }

    private void initComponents() {

        setTitle("Quản Lý Hóa Đơn - Java Computer Store");
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
        DefaultTableModel model = invoiceBus.getAllInvoices();
        if (model != null) {
            table.setModel(model);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InvoiceGUI().setVisible(true);
        });
    }
}
