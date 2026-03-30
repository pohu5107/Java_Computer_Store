import DAO.ProductDAO;
import java.util.ArrayList;

public class GetProductIDs {
    public static void main(String[] args) {
        ProductDAO dao = new ProductDAO();
        ArrayList<Object[]> list = dao.getAll();
        for (int i = 0; i < Math.min(list.size(), 10); i++) {
            System.out.println(list.get(i)[0]);
        }
    }
}
