///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
package GUI;
import BUS.BrandBUS;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class BrandGUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private BrandBUS brandBUS = new BrandBUS();
    
    public BrandGUI(){
        setTitle("Test Ket Noi Du Lieu - Brand");
        setSize(600,400);
        //        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); //Lệnh DISPOSE_ON_CLOSE giúp khi bấm dấu "X", nó chỉ tắt đúng cái cửa sổ đó thôi, các cửa sổ khác vẫn giữ nguyên
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        //1. tao bang va header
        String[] columns = {"Ma Hang", "Ten Hang", "Dia Chi", "SDT"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        //2. tao button de test
        JButton btnLoad = new JButton("Nap du lieu tu MySQL");
        btnLoad.addActionListener(e -> loadData());
        add(btnLoad, BorderLayout.SOUTH);
    }
    
    private void loadData(){
        ArrayList<Object[]> list = brandBUS.getAll(); // goi Bus lay du lieu
        model.setRowCount(0);//xoa du lieu cux tren table
        for(Object[] row : list){ // door du lieu moi vaof
            model.addRow(row);
        }
        if(list.isEmpty()){
            JOptionPane.showMessageDialog(this, "Du lieu trongg hoac loi ket noi!");
        }
    }
    
    public static void main(String[] args){
        
        SwingUtilities.invokeLater(() -> {new BrandGUI().setVisible(true);
    
       });
    }
    
    
}






