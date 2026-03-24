package DAO;

import ConnectDB.ConnectDB;
import java.util.ArrayList;
import java.sql.*;

public class ProductDAO {

    public ArrayList<Object[]> getAll() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT p.ProductID, p.ProductName, p.Quantity, p.Price, p.Unit, p.CategoryID, p.BrandID, " +
                     "pd.CPU, pd.RAM, pd.VGA, pd.Mainboard " +
                     "FROM Products p " +
                     "LEFT JOIN Productdetails pd ON p.ProductID = pd.ProductID";
        Connection conn = ConnectDB.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Object[] row = {
                    rs.getString("ProductID"), rs.getString("ProductName"),
                    rs.getInt("Quantity"), rs.getDouble("Price"),
                    rs.getString("Unit"), rs.getString("CategoryID"),
                    rs.getString("BrandID"), rs.getString("CPU"),
                    rs.getString("RAM"), rs.getString("VGA"), rs.getString("Mainboard")
                };
                list.add(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insert(String id, String name, int qty, double price, String unit, String catID, String brandID, String cpu, String ram, String vga, String mainboard) {   
        Connection conn = ConnectDB.getConnection();
        try {
            conn.setAutoCommit(false); 
            String sqlProd = "INSERT INTO Products (ProductID, ProductName, Quantity, Price, Unit, CategoryID, BrandID) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement pstProd = conn.prepareStatement(sqlProd);
            pstProd.setString(1, id);
            pstProd.setString(2, name);
            pstProd.setInt(3, qty); // Thường truyền vào 0 từ BUS
            pstProd.setDouble(4, price);
            pstProd.setString(5, unit);
            pstProd.setString(6, catID);
            pstProd.setString(7, brandID);
            pstProd.executeUpdate();
            
            String sqlDetails = "INSERT INTO Productdetails (ProductID, CPU, RAM, VGA, Mainboard) VALUES(?,?,?,?,?)";
            PreparedStatement pstDetails = conn.prepareStatement(sqlDetails);
            pstDetails.setString(1, id);
            pstDetails.setString(2, cpu);
            pstDetails.setString(3, ram);
            pstDetails.setString(4, vga);
            pstDetails.setString(5, mainboard);
            pstDetails.executeUpdate();
            
            conn.commit();
            return true;
        } catch(SQLException e) {
            try { if (conn != null) conn.rollback(); } catch(SQLException ex) {}
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch(SQLException e) {}
        }                
    }

    // --- SỬA ĐỔI: Loại bỏ Quantity khỏi hàm update thông tin sản phẩm ---
    public boolean update(String id, String name, double price, String unit, String catID, String brandID, String cpu, String ram, String vga, String mainboard) {
        Connection conn = ConnectDB.getConnection();
        try {
            conn.setAutoCommit(false);
            String sqlProd = "UPDATE Products SET ProductName = ?, Price = ?, Unit = ?, CategoryID = ?, BrandID = ? WHERE ProductID = ?";
            PreparedStatement pstProd = conn.prepareStatement(sqlProd);
            pstProd.setString(1, name);
            pstProd.setDouble(2, price);
            pstProd.setString(3, unit);
            pstProd.setString(4, catID);
            pstProd.setString(5, brandID);
            pstProd.setString(6, id);
            pstProd.executeUpdate();

            String sqlDetails = "UPDATE Productdetails SET CPU = ?, RAM = ?, VGA = ?, Mainboard = ? WHERE ProductID = ?";
            PreparedStatement pstDetails = conn.prepareStatement(sqlDetails);
            pstDetails.setString(1, cpu);
            pstDetails.setString(2, ram);
            pstDetails.setString(3, vga);
            pstDetails.setString(4, mainboard);
            pstDetails.setString(5, id);
            pstDetails.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch(SQLException e) {}
        }
    }

    // --- HÀM MỚI: Cập nhật số lượng (Dùng cho Bán hàng / Nhập hàng) ---
    public boolean updateQuantity(String id, int qtyChange) {
        String sql = "UPDATE Products SET Quantity = Quantity + ? WHERE ProductID = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, qtyChange);
            pst.setString(2, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM Products WHERE ProductID = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public Object[] getByID(String id) {
        String sql = "SELECT ProductID, ProductName, Price, Quantity FROM Products WHERE ProductID = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Object[] {
                    rs.getString("ProductID"), rs.getString("ProductName"),
                    rs.getDouble("Price"), rs.getInt("Quantity")
                };
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}