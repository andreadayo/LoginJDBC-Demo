/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loginjdbc;

/**
 * INFO: Let users sign up (error if confirm password does not match and if user already exists)
 * @author Andrea Dayo
 */

// import packages
import java.sql.*;
import java.util.*;

public class SignupJDBC {
    public static void main(String[] args) {
        try {
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
            System.out.println("SIGN UP");
            System.out.println("Enter username: ");
            String userName = myObj.nextLine();  // Read user input
            System.out.println("Enter password: ");
            String passWord = myObj.nextLine();  // Read user input
            
            System.out.println("Confirm password: ");
            String confirmPassword = myObj.nextLine();  // Read user input
            
            if (passWord.equals(confirmPassword)) {
                // Create and Execute the PreparedStatement
                String query = "SELECT * FROM USER_INFO WHERE Username = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, userName);

                ResultSet rs = ps.executeQuery();

                // Retrieve the ResultSet
                System.out.println("Checking if user exists...");

                // Check if user info exists
                if(rs.next()) {
                    System.out.println("Username already exists.");
                } else {
                    String signupQuery = "INSERT INTO USER_INFO (Username, Password) VALUES (?,?)";
                    PreparedStatement su = con.prepareStatement(signupQuery);
                    su.setString(1, userName);
                    su.setString(2, passWord);
                    su.executeUpdate(); // update table with new row
                    System.out.println("You have successfully signed up.");
                }
                
                // Close the connection
                rs.close();
                ps.close();
                
            } else {
                System.out.println("Your password does not match.");
            }

            // Close the connection
            con.close();
        } catch (SQLException | ClassNotFoundException sqle) {
            sqle.printStackTrace();
        }
    }
}

