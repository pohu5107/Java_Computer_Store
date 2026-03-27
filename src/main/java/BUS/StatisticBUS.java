/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.StatisticDAO;
import java.util.ArrayList;
import java.time.YearMonth;

public class StatisticBUS {
    private StatisticDAO statisticDAO = new StatisticDAO();

    // 1. Thống kê tổng quan (Doanh thu, Chi phí, Lợi nhuận)
    public Object[] getOverview(String fromDate, String toDate) {
        ArrayList<Object[]> details = statisticDAO.getProfitReport(fromDate, toDate);
        double totalRev = 0;
        double totalProfit = 0;
        double totalCost = 0;
        for (Object[] row : details) {
            totalRev += (double) row[3];    // Cột Revenue
            totalProfit += (double) row[4]; // Cột Profit
        }
        totalCost = totalRev - totalProfit; 
        return new Object[]{ totalRev, totalCost, totalProfit };
    }

    // Người dùng chỉ cần chọn Tháng và Năm, BUS tự tính ngày đầu và cuối tháng
    public ArrayList<Object[]> getReportByMonth(int month, int year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        String fromDate = yearMonth.atDay(1).toString(); // Ví dụ: 2024-03-01
        String toDate = yearMonth.atEndOfMonth().toString(); // Ví dụ: 2024-03-31
        
        return statisticDAO.getProfitReport(fromDate, toDate);
    }

    public ArrayList<Object[]> getReportByQuarter(int quarter, int year) {
        String fromDate = "";
        String toDate = "";
        switch (quarter) {
            case 1: fromDate = year + "-01-01"; toDate = year + "-03-31"; break;
            case 2: fromDate = year + "-04-01"; toDate = year + "-06-30"; break;
            case 3: fromDate = year + "-07-01"; toDate = year + "-09-30"; break;
            case 4: fromDate = year + "-10-01"; toDate = year + "-12-31"; break;
        }
        return statisticDAO.getProfitReport(fromDate, toDate);
    }

    // Tìm kiếm sản phẩm bán chạy nhất (Logic bổ sung)
//    public ArrayList<Object[]> getTopSellingProducts(String fromDate, String toDate) {
//        // Tận dụng hàm profit report và sắp xếp lại theo số lượng bán (QtySold)
//        ArrayList<Object[]> list = statisticDAO.getProfitReport(fromDate, toDate);
//        list.sort((o1, o2) -> Integer.compare((int)o2[2], (int)o1[2])); // Giảm dần
//        return list;
//    }
}