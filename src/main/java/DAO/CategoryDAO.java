package DAO;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.*;
import java.util.ArrayList;
import ConnectDB.ConnectDB;

public class CategoryDAO {
    public ArrayList<Object[]> getAll() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM Categories";
        Connection conn = ConnectDB.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("CategoryID"),
                    rs.getString("CategoryName"),
                    rs.getString("Description"),
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(String id, String name, String description) {
        String sql = "INSERT INTO Categories (CategoryID, CategoryName, Description) VALUES (?, ?, ?)";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, id);
            pst.setString(2, name);
            pst.setString(3, description);
            
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM Categories WHERE CategoryID = ?";
        Connection conn = ConnectDB.getConnection();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean update(String id, String name, String description){
        String sql = "UPDATE FROM Categories SET CategoryName = ?, Description = ? WHERE CategoryID = ?";
        Connection conn = ConnectDB.getConnection();
        try(PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1, name);
            pst.setString(2, description);
            pst.setString(3, id);
            return pst.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;   

        }
    }   
        
}