/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package loginjdbc;

/**
 * INFO: Check if login info from database matches
 * @author Andrea Dayo
 */

// import packages
import java.sql.*;
import java.util.*;

public class LoginJDBC 
{
    public static void main(String[] args) 
    {
        try 
        {
            // Load Driver
            String driver = "org.apache.derby.jdbc.ClientDriver";
            Class.forName(driver);
            System.out.println("Loaded Driver: " + driver);

            // Establish Connection
            String url = "jdbc:derby://localhost:1527/UserListDB";
            String username = "app";
            String password = "app";
            Connection con = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to: " + url);
            
            // Form (convert to Swing)
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            System.out.println("LOGIN");
            System.out.println("Enter username: ");
            String userName = myObj.nextLine();  // Read user input
            System.out.println("Enter password: ");
            String passWord = myObj.nextLine();  // Read user input

            // Create and Execute the PreparedStatement
            String query = "SELECT * FROM USER_INFO WHERE Username = ? AND Password = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userName);
            ps.setString(2, passWord);

            ResultSet rs = ps.executeQuery();
            // System.out.println("Executed Query: " + query);
            
            // Retrieve the ResultSet
            System.out.println("Processing The Result Set: ");

            // Check if login info exists
            if(rs.next())
            {
                System.out.println("You have successfully logged in.");
            } 
            else 
            {
               System.out.println("Your login details are incorrect.");
            }

            // Close the connection
            rs.close();
            ps.close();
            con.close();
        } 
        catch (SQLException | ClassNotFoundException sqle) 
        {
            sqle.printStackTrace();
        }
    }
}
