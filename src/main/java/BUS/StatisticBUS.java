
package BUS;
import DAO.StatisticDAO;
import java.util.ArrayList;
import java.time.YearMonth;

public class StatisticBUS {
    private StatisticDAO statisticDAO = new StatisticDAO();
    
    // 1. thong ke tong quan (Doanh thu, chi phi,, loi nhuan)
    public Object[] getOverview(String fromDate, String toDate) {
        double rev  = statisticDAO.getTotalRevenue(fromDate, toDate);
        double exp = statisticDAO.getTotalExpenditure(fromDate, toDate);
        double profit = rev - exp;
        return new Object[]{
            rev, exp, profit
        };      
    }
    
    // nguoi dung chi chon thang va nam, BUS tu tinh ngay dau vaf cuoi thang
    public ArrayList<Object[]> getReportByMonth(int month, int year){
        YearMonth yearMonth = YearMonth.of(year, month);
        String fromDate = yearMonth.atDay(1).toString();
        String toDate = yearMonth.atEndOfMonth().toString();
        
        return statisticDAO.getProfitReport(fromDate, toDate);
    }
    
    // thong ke theo Quys
    public ArrayList<Object[]> getReportByQuarter(int quarter,int year){
        String fromDate="";
        String toDate="";
        switch(quarter){
            case 1: fromDate = year + "-01-01"; toDate = year + "-03-31"; break;
            case 2: fromDate = year + "-01-01"; toDate = year + "-06-30"; break;
            case 3: fromDate = year + "-01-01"; toDate = year + "-09-30"; break;
            case 4: fromDate = year + "-01-01"; toDate = year + "-12-31"; break;
                    
        } 
        return statisticDAO.getProfitReport(fromDate, toDate);
    }
    
    // tim kiem san pham ban chay nhat (logic bo sung)
    public ArrayList<Object[]> getTopSellingProducts(String fromDate, String toDate){
        ArrayList<Object[]> list = statisticDAO.getProfitReport(fromDate, toDate);
        list.sort((o1, o2) -> Integer.compare((int)o2[2], (int)o1[2]));
        return list;
    }
    
}
