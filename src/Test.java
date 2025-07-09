package src;
import java.sql.*;

public class Test {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/testdb"; // Change to your DB
        String user = "root";
        String password = "Cmt@049";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL successfully!");
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
