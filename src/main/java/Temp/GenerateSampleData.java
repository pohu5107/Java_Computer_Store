package Temp;

import DAO.PromotionDAO;
import DAO.ProductDAO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class GenerateSampleData {
    public static void main(String[] args) {
        PromotionDAO proDAO = new PromotionDAO();
        ProductDAO prodDAO = new ProductDAO();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Random rand = new Random();
        
        // Lấy 10 mã SP đầu tiên
        ArrayList<Object[]> products = prodDAO.getAll();
        ArrayList<String> pids = new ArrayList<>();
        for (int i = 0; i < Math.min(products.size(), 10); i++) {
            pids.add(products.get(i)[0].toString());
        }

        System.out.println("Đang tạo 30 khuyến mãi mẫu...");

        for (int i = 1; i <= 10; i++) {
            String start = getRandomDate(0); // Tháng này
            String end = getRandomDate(1);   // Tháng sau

            // 1. Khuyến mãi Chung
            proDAO.insert("KM_GEN_" + i, "Siêu Sale Tổng Hợp " + i, start, end, "KM đại trà cho toàn shop", "General", null, 0.0, 0.0, 0.0);
            
            // 2. Khuyến mãi Sản phẩm
            if (!pids.isEmpty()) {
                String pid = pids.get(rand.nextInt(pids.size()));
                Double disc = 5.0 + rand.nextInt(45); // 5% - 50%
                proDAO.insert("KM_PROD_" + i, "Sale Sản phẩm Hot " + i, start, end, "Giảm giá đặc biệt cho linh kiện", "Product", pid, disc, 0.0, 0.0);
            }

            // 3. Khuyến mãi Hóa đơn
            Double minInv = 500000.0 + rand.nextInt(95) * 100000; // 500k - 10M
            Double discAmt = 50000.0 + rand.nextInt(45) * 10000; // 50k - 500k
            proDAO.insert("KM_PRICE_" + i, "Ưu đãi Hóa đơn Vàng " + i, start, end, "Giảm tiền trực tiếp khi mua nhiều", "Price", null, 0.0, minInv, discAmt);
        }

        System.out.println("Đã chèn 30 khuyến mãi mẫu thành công!");
    }

    private static String getRandomDate(int monthOffset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, monthOffset);
        cal.set(Calendar.DAY_OF_MONTH, new Random().nextInt(28) + 1);
        cal.set(Calendar.HOUR_OF_DAY, new Random().nextInt(24));
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
    }
}
