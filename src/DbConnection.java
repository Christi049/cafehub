package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/cafe_hub";
    private static final String USER = "root";
    private static final String PASSWORD = "******";
    public static void main(String[] args) {
        String[] cafeItems={
            "Espresso",
            "Croissant",
            "Matcha Latte",
            "Pancakes",
            "Veg Burger",
            "French Fries",
        };

        int[] prices={
            120,
            90,
            130,
            110,
            140,
            70,
        };
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn =DriverManager.getConnection(URL, USER, PASSWORD);
            String sql ="insert into cafe_items (name,price,quantity) values(?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            for(int i=0; i<cafeItems.length; i++){
                pstmt.setString(1,cafeItems[i]);
                pstmt.setInt(2,prices[i]);
                pstmt.setInt(3,0);
                pstmt.executeUpdate();
            }

            System.out.println("Items inserted successfully.");

            // Close
            pstmt.close();
            conn.close();
        } 
        
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
 }

