/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this 
*/
package DAO;
import ConnectDB.ConnectDB;
import java.util.ArrayList;
import java.sql.*;

public class ProductDAO {    
    public ArrayList<Object[]> getAll() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT p.ProductID, p.ProductName, p.Quantity, p.Price, p.Unit, p.CategoryID, p.BrandID," +
                     "pd.CPU, pd.RAM, pd.VGA" +
                     "FROM Products p" +
                     "LEFT JOIN Productdetails pd ON p.ProductID = pd.ProductID";
        Connection conn = ConnectDB.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                    rs.getString("ProductID"),
                    rs.getString("ProductName"),
                    rs.getInt("Quantity"),
                    rs.getDouble("Price"),
                    rs.getString("Unit"),
                    rs.getString("CategoryID"),
                    rs.getString("BrandID"),
                    rs.getString("CPU"),
                    rs.getString("RAM"),
                    rs.getString("VGA"),
                    rs.getString("Mainboard")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean insert(String id, String name, int qty, double price, String unit, String catID, String brandID, String cpu, String ram, String vga, String mainboard) {
        Connection conn = ConnectDB.getConnection();
        try{ conn.setAutoCommit(false); //chờ lệnh rồi mới lưu vào DB
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
            String sqlDetails = "INSERT INTO Products (ProductID, CPU, RAM, VGA, Mainboard) VALUES(?,?,?,?,?)";
            PreparedStatement pstDetails = conn.prepareStatement(sqlDetails);
            pstDetails.setString(1, id);
            pstDetails.setString(2, cpu);
            pstDetails.setString(3, ram);
            pstDetails.setString(4, vga);
            pstDetails.setString(5, mainboard);
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
    
    public boolean update(String id, String name, int qty, double price, String unit, String catID, String brandID, String cpu, String ram, String vga, String mainboard){
        Connection conn = ConnectDB.getConnection();
        try{
            conn.setAutoCommit(false);
            String sqlProd = "UPDATE Products SET ProductName = ?, Quantity = ?, Price = ?, Unit = ?, CategoryID = ?, BrandID = ? WHERE ProductID = ?";
            PreparedStatement pstProd = conn.prepareStatement(sqlProd);
            pstProd.setString(1, name);
            pstProd.setInt(2, qty);
            pstProd.setDouble(3,price);
            pstProd.setString(4, unit);
            pstProd.setString(5, catID);
            pstProd.setString(6, brandID);
            pstProd.setString(7, id);
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
        } catch(SQLException e){
            try{
                if(conn!=null) conn.rollback(); 
            } catch(SQLException ex){
            ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally{
            try{
                if(conn!=null) conn.setAutoCommit(true);
            } catch(SQLException e){
                e.printStackTrace();
            }
        }    
    }
    
    public boolean delete(String id){
        String sql = "DELETE FROM Procducts WHERE PRODUCTID = ?";
        Connection conn = ConnectDB.getConnection();
        try(PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public Object[] getByID(String id){
        String sql =  "SELECT ProductID, ProductName, Price, Quantity FROM Products WHERE ProductID = ?";
        Connection conn = ConnectDB.getConnection();
        try(PreparedStatement pst = conn.prepareCall(sql)){
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return new Object[]{
                    rs.getString("ProductID"),
                    rs.getString("ProductName"),
                    rs.getString("Price"),
                    rs.getString("Quantity")
                };
            }
        } catch (SQLException e){
                e.printStackTrace();
                }
         return null;
    }
    
    
//        public boolean updateStock(String productId, int amount) {
//        // amount > 0 là nhập thêm, amount < 0 là bán đi
//        String sql = "UPDATE Products SET Quantity = Quantity + ? WHERE ProductID = ?";
//        try (Connection conn = ConnectDB.getConnection();
//             PreparedStatement pst = conn.prepareStatement(sql)) {
//            pst.setInt(1, amount);
//            pst.setString(2, productId);
//            return pst.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
          
        
    }
