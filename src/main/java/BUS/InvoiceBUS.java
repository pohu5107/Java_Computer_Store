package BUS;

import DAO.InvoiceDAO;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class InvoiceBUS {

    private InvoiceDAO invoiceDAO = new InvoiceDAO();

    public DefaultTableModel getAllInvoices() {

        String[] columnNames = {"InvoiceID", "CustomerID", "InvoiceDate", "TotalAmount"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        ArrayList<Object[]> list = invoiceDAO.getAll();

        for (Object[] row : list) {
            model.addRow(row);
        }

        return model;
    }
}