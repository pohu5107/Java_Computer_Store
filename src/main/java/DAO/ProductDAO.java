/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import ConnectDB.ConnectDB;
import java.util.ArrayList;
import java.sql.*;

public class ProductDAO {
    private ConnectDB connectDB = new ConnectDB();
    
    public ArrayList<Object[]> getAll() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT p.ProductID, p.ProductName, p.Quantity, p.Price, p.Unit, p.CategoryID, p.BrandID," +
                     "pd.CPU, pd.RAM, pd.VGA" +
                     "FROM Products p" +
                     "LEFT JOIN Productdetails pd ON p.ProductID = pd.ProductID";
        try (Connection conn = connectDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                    rs.getString("ProductID"),
                    rs.getString("ProductName"),
                    rs.getInt("Quantity"),
                    rs.getDouble("Price"),
                    rs.getString("Unit"),
                    rs.getString("CPU"),
                    rs.getString("RAM"),
                    rs.getString("VGA")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean insert(String id, String name, int qty, double price, String unit, String catID, String brandID, String cpu, String ram, String vga) {   
        
        Connection conn = null;
        try{conn = connectDB.getConnection();
            conn.setAutoCommit(false); //chờ lệnh rồi mới lưu vào DB
            
            //lưu vào bảng Products
            String sqlProd = "INSERT INTO Products (ProductID, ProductName, Quantity, Price, Unit, CategoryID, BrandID) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement pstProd = conn.prepareStatement(sqlProd);
            pstProd.setString(1, id);
            pstProd.setString(2, name);
            pstProd.setInt(3, qty);
            pstProd.setDouble(4, price);
            pstProd.setString(5, unit);
            pstProd.setString(6, catID);
            pstProd.setString(7, brandID);
            pstProd.executeUpdate();
            
            //Lưu vào bảng ProductDetails
            String sqlDetails = "INSERT INTO Products (CPU, RAM, VGA) VALUES(?,?,?)";
            PreparedStatement pstDetails = conn.prepareStatement(sqlDetails);
            pstDetails.setString(1, cpu);
            pstDetails.setString(2, ram);
            pstDetails.setString(3, vga);
            pstDetails.executeUpdate();
            
            conn.commit();
            return true;
        }catch(SQLException e){
            try{
                if (conn !=null) conn.rollback();           
            } catch(SQLException ex){
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try{
                if (conn!=null) conn.setAutoCommit(true);
            } catch(SQLException e){
                e.printStackTrace();
            }
        }              
    }
    public boolean delete(String id){
        String sql = "DELETE FROM Procducts WHERE PRODUCTID = ?";
        try(Connection conn = connectDB.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateStock(String productId, int amount) {
        // amount > 0 là nhập thêm, amount < 0 là bán đi
        String sql = "UPDATE Products SET Quantity = Quantity + ? WHERE ProductID = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, amount);
            pst.setString(2, productId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
