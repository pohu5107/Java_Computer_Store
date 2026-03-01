///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package GUI;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import BUS.CategoriesBUS;
//
//public class CategoriesGUI extends JFrame {
//    private JTable table;
//    private JScrollPane scrollPane;
//    private JButton btnRefresh;
//    private CategoriesBUS ctgrbus = new CategoriesBUS();
//    
//    public CategoriesGUI(){
//        initComponents();
//        loadData();
//    }
//    
//    private void initComponents(){
//        setTitle("Quanr lys danh mucj sanr phaamr ");
//        setSize(700, 450);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // chi dong cua so nay chu ko thoat app
//        setLocationRelativeTo(null);
//        setLayout(new BorderLayout());
//        
//        JLabel lblTittle = new JLabel("Danh sachs danh mucj", JLabel.CENTER);
//        lblTittle.setFont(new Font("Arial", Font.BOLD, 20));
//        add(lblTittle, BorderLayout.NORTH);
//        
//        // hien thi o giua
//        table = new JTable();
//        scrollPane = new JScrollPane(table);
//        add(scrollPane, BorderLayout.CENTER);
//        
//        btnRefresh = new JButton("Tair laij danh sachs");
//        JPanel panelBottom = new JPanel();
//        panelBottom.add(btnRefresh);
//        add(panelBottom, BorderLayout.SOUTH);
//        
//        btnRefresh.addActionListener(e -> loadData());
//        
//    }
//    
//    private void loadData() {
//        DefaultTableModel model = ctgrbus.getAllCategories();
//        if (model != null){
//            table.setModel(model);
//        } else {
//            JOptionPane.showMessageDialog(this, "Ko theer laays duwx lieeuj!");
//        }
//        
//    }
//    
//    public static void main (String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new CategoriesGUI().setVisible(true);
//        });
//    }
//    
//    
//}
