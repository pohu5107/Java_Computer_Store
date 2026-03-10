/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConnectDB;
import java.sql.Connection;

public class testDB{
    public static void main(String[] args){
        ConnectDB db = new ConnectDB();
         
        System.out.println("dang thu ket noi ...");
        Connection conn = db.getConnection();
        
        if(conn!=null){
            System.out.println("> test thanh cong");
        } else {
            System.out.println("> test that bai.");
        }
    }
}

