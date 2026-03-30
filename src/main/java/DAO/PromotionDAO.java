package DAO;

import ConnectDB.ConnectDB;
import java.sql.*;
import java.util.ArrayList;

/**
 * Lớp DAO: Xử lý 3 bảng Khuyến mãi (Chung, SP, Hóa đơn).
 */
public class PromotionDAO {
    // Câu truy vấn kết hợp 3 bảng
    private final String SELECT_ALL = "SELECT pc.*, pp.ProductID, pp.DiscountPercent AS ProdDiscount, " +
            "ipc.MinInvoiceValue, ipc.DiscountAmount, ipc.DiscountPercent AS InvDiscount, ipc.MaxDiscountValue " +
            "FROM PromotionCampaigns pc " +
            "LEFT JOIN ProductPromotions pp ON pc.PromotionID = pp.PromotionID " +
            "LEFT JOIN InvoicePromotionConfigs ipc ON pc.PromotionID = ipc.PromotionID";

    // Lấy toàn bộ danh sách
    public ArrayList<Object[]> getAll() {
        ArrayList<Object[]> list = new ArrayList<>();
        try (Connection conn = ConnectDB.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(SELECT_ALL)) {
            while (rs.next())
                list.add(mapRsToRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Chuyển kết quả ResultSet sang mảng Object
    private Object[] mapRsToRow(ResultSet rs) throws SQLException {
        return new Object[] {
                rs.getString("PromotionID"), rs.getString("PromotionName"), rs.getString("Description"),
                rs.getTimestamp("StartDate"), rs.getTimestamp("EndDate"), rs.getInt("Status"),
                rs.getString("ProductID"), rs.getDouble("ProdDiscount"), rs.getDouble("MinInvoiceValue"),
                rs.getDouble("DiscountAmount"), rs.getDouble("InvDiscount"), rs.getDouble("MaxDiscountValue")
        };
    }

    // Thêm mới dùng Transaction (Giao dịch)
    public boolean insert(String id, String name, String start, String end, String desc, String type,
            String pid, Double pDsc, Double mInv, Double dAmt) {
        Connection conn = ConnectDB.getConnection();
        try {
            conn.setAutoCommit(false);
            // Ghi bảng chính
            try (PreparedStatement pst = conn.prepareStatement("INSERT INTO PromotionCampaigns VALUES (?,?,?,?,?,?)")) {
                pst.setString(1, id);
                pst.setString(2, name);
                pst.setString(3, start);
                pst.setString(4, end);
                pst.setInt(5, 1);
                pst.setString(6, desc);
                pst.executeUpdate();
            }
            // Ghi bảng phụ theo loại
            if ("Product".equalsIgnoreCase(type)) {
                try (PreparedStatement pst = conn.prepareStatement("INSERT INTO ProductPromotions VALUES (?,?,?)")) {
                    pst.setString(1, id);
                    pst.setString(2, pid);
                    pst.setDouble(3, pDsc);
                    pst.executeUpdate();
                }
            } else if ("Price".equalsIgnoreCase(type)) {
                try (PreparedStatement pst = conn
                        .prepareStatement("INSERT INTO InvoicePromotionConfigs VALUES (?,?,?,0,NULL)")) {
                    pst.setString(1, id);
                    pst.setDouble(2, mInv != null ? mInv : 0);
                    pst.setDouble(3, dAmt != null ? dAmt : 0);
                    pst.executeUpdate();
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
            }
        }
    }

    // Cập nhật (Xóa cũ - Ghi mới bảng phụ)
    public boolean update(String id, String name, String start, String end, String desc, int status, String type,
            String pid, Double pDsc, Double mInv, Double dAmt) {
        Connection conn = ConnectDB.getConnection();
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement pst = conn.prepareStatement(
                    "UPDATE PromotionCampaigns SET PromotionName=?, StartDate=?, EndDate=?, Status=?, Description=? WHERE PromotionID=?")) {
                pst.setString(1, name);
                pst.setString(2, start);
                pst.setString(3, end);
                pst.setInt(4, status);
                pst.setString(5, desc);
                pst.setString(6, id);
                pst.executeUpdate();
            }
            // Làm sạch bảng phụ trước khi ghi mới
            try (PreparedStatement pst = conn.prepareStatement("DELETE FROM ProductPromotions WHERE PromotionID=?")) {
                pst.setString(1, id);
                pst.executeUpdate();
            }
            try (PreparedStatement pst = conn
                    .prepareStatement("DELETE FROM InvoicePromotionConfigs WHERE PromotionID=?")) {
                pst.setString(1, id);
                pst.executeUpdate();
            }

            if ("Product".equalsIgnoreCase(type)) {
                try (PreparedStatement pst = conn.prepareStatement("INSERT INTO ProductPromotions VALUES (?,?,?)")) {
                    pst.setString(1, id);
                    pst.setString(2, pid);
                    pst.setDouble(3, pDsc);
                    pst.executeUpdate();
                }
            } else if ("Price".equalsIgnoreCase(type)) {
                try (PreparedStatement pst = conn
                        .prepareStatement("INSERT INTO InvoicePromotionConfigs VALUES (?,?,?,0,NULL)")) {
                    pst.setString(1, id);
                    pst.setDouble(2, mInv != null ? mInv : 0);
                    pst.setDouble(3, dAmt != null ? dAmt : 0);
                    pst.executeUpdate();
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
            }
        }
    }

    // Xóa theo ID
    public boolean delete(String id) {
        try (Connection conn = ConnectDB.getConnection();
                PreparedStatement pst = conn.prepareStatement("DELETE FROM PromotionCampaigns WHERE PromotionID=?")) {
            pst.setString(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tìm kiếm cụ thể theo ID
    public Object[] getByID(String id) {
        try (Connection conn = ConnectDB.getConnection();
                PreparedStatement pst = conn.prepareStatement(SELECT_ALL + " WHERE pc.PromotionID=?")) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next())
                    return mapRsToRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tự động quét cập nhật trạng thái hết hạn
    public void updateStatusByDate() {
        String sql = "UPDATE PromotionCampaigns SET Status = CASE WHEN EndDate < CURRENT_TIMESTAMP THEN 0 ELSE Status END";
        try (Connection conn = ConnectDB.getConnection(); Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
