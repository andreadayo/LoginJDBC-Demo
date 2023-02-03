package loginSwing;

// @author Andrea Dayo

// import packages
import java.sql.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SignupSwing implements ActionListener 
{
    // Containers
    private JFrame fSignup, fInfo;
    private JPanel p1, p2, p3, p4;

    // Components
    private JLabel lUser, lPass, lConfirm;
    private JTextField tfUser, tfPass, tfConfirm;
    private JButton bSignup;
	
    public SignupSwing()
    {
        // Containers
        fSignup = new JFrame("Signup Screen");

        p1 = new JPanel();
        p2 = new JPanel();
        p3 = new JPanel();
        p4 = new JPanel();

        // Components
        lUser = new JLabel("Username: ");
        lPass = new JLabel("Password: ");
        lConfirm = new JLabel("Confirm Password: ");

        tfUser = new JTextField("", 15);
        tfPass = new JTextField("", 15);
        tfConfirm = new JTextField("", 15);

        bSignup = new JButton("Sign up");
    }
    
    public void startApp()
    {
        // Panel 1 - Username
        p1.add(lUser);
        p1.add(tfUser);

        // Panel 2 - Password
        p2.add(lPass);
        p2.add(tfPass);

        // Panel 3 - Confirm Password
        p3.add(lConfirm);
        p3.add(tfConfirm);
        
        // Panel 4 - Button
        p4.add(bSignup);
        
        // Signup Frame
        fSignup.setLayout(new GridLayout(4,1));

        fSignup.add(p1);
        fSignup.add(p2);
        fSignup.add(p3);
        fSignup.add(p4);

        fSignup.pack();
        fSignup.setVisible(true);
        fSignup.setLocationRelativeTo(null);
        fSignup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Register Event Handler to UI (Event Source)
        bSignup.addActionListener(this);
    }
    
    // Event Handler
    public void actionPerformed(ActionEvent e) 
    {
        Object source = e.getSource();
        
        if (tfUser.getText().equals("") || tfPass.getText().equals("") || tfConfirm.getText().equals("")) 
        {
            JOptionPane.showMessageDialog(fInfo, "Please enter a username and password", "Error Screen", JOptionPane.WARNING_MESSAGE);
        }
        else if (tfUser.getText() != null && tfPass.getText() != null && tfConfirm.getText() != null) 
        {
            if (source == bSignup)
            {
                try 
                {
                    // Load Driver
                    String driver = "org.apache.derby.jdbc.ClientDriver";
                    Class.forName(driver);

                    // Establish Connection
                    String url = "jdbc:derby://localhost:1527/UserListDB";
                    String username = "app";
                    String password = "app";
                    Connection con = DriverManager.getConnection(url, username, password);


                    if (tfPass.getText().equals(tfConfirm.getText())) 
                    {
                        // Create and Execute the PreparedStatement
                        String query = "SELECT * FROM USER_INFO WHERE Username = ?";
                        PreparedStatement ps = con.prepareStatement(query);
                        ps.setString(1, tfUser.getText());

                        ResultSet rs = ps.executeQuery();

                        // Retrieve the ResultSet
                        if(rs.next()) // Check if user info exists
                        {
                            JOptionPane.showMessageDialog(fInfo, "Username already exists.", "Error Screen", JOptionPane.WARNING_MESSAGE);
                        } 
                        else 
                        {
                            String signupQuery = "INSERT INTO USER_INFO (Username, Password) VALUES (?,?)";
                            PreparedStatement su = con.prepareStatement(signupQuery);
                            su.setString(1, tfUser.getText());
                            su.setString(2, tfPass.getText());
                            su.executeUpdate(); // update table with new row

                            JOptionPane.showMessageDialog(fInfo, "You have successfully signed up, " + tfUser.getText() + "!");

                            // Close Login Frame
                            fSignup.dispose();

                            // Redirect to Login Screen
                            LoginSwing login = new LoginSwing();
                            login.startApp();
                        }

                        // Close the connection
                        rs.close();
                        ps.close();

                    } else {
                        JOptionPane.showMessageDialog(fInfo, "Your password does not match. Please try again.", "Error Screen", JOptionPane.WARNING_MESSAGE);
                    }

                    // Close the connection
                    con.close();
                } 
                catch (SQLException | ClassNotFoundException sqle) 
                {
                    sqle.printStackTrace();
                }
            }
        }
    }

    // Main Class
    public static void main(String args[]) throws IOException
    {
        SignupSwing signup = new SignupSwing();
        signup.startApp();
    }
}