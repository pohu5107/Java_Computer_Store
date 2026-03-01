package BUS;

import DAO.CustomerDAO;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class CustomerBUS {

    private CustomerDAO customerDAO = new CustomerDAO();

    public DefaultTableModel getAllCustomers() {

        String[] columnNames = {"CustomerID", "CustomerName", "Address", "Phone"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        ArrayList<Object[]> list = customerDAO.getAll();

        for (Object[] row : list) {
            model.addRow(row);
        }

        return model;
    }
}