package loginSwing;

// @author Andrea Dayo

// import packages
import java.sql.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginSwing implements ActionListener 
{
    // Containers
    private JFrame fLogin, fInfo;
    private JPanel p1, p2, p3;

    // Components
    private JLabel lUser, lPass;
    private JTextField tfUser, tfPass;
    private JButton bLogin;
    
    // Variables
    private static int ctr = 0;
	
    public LoginSwing()
    {
        // Containers
        fLogin = new JFrame("Login Screen");

        p1 = new JPanel();
        p2 = new JPanel();
        p3 = new JPanel();

        // Components
        lUser = new JLabel("Username: ");
        lPass = new JLabel("Password: ");

        tfUser = new JTextField("", 15);
        tfPass = new JTextField("", 15);

        bLogin = new JButton("Login");
    }
    
    public void startApp()
    {
        // Panel 1 - Username
        p1.add(lUser);
        p1.add(tfUser);

        // Panel 2 - Password
        p2.add(lPass);
        p2.add(tfPass);

        // Panel 3 - Button
        p3.add(bLogin);
        
        // Login Frame
        fLogin.setLayout(new GridLayout(3,1));

        fLogin.add(p1);
        fLogin.add(p2);
        fLogin.add(p3);

        fLogin.pack();
        fLogin.setVisible(true);
        fLogin.setLocationRelativeTo(null);
        fLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Register Event Handler to UI (Event Source)
        bLogin.addActionListener(this);
    }
    
    // Event Handler
    public void actionPerformed(ActionEvent e) 
    {
       Object source = e.getSource();
        
        if (tfUser.getText().equals("") || tfPass.getText().equals("")) 
        {
            JOptionPane.showMessageDialog(fInfo, "Please enter a username and password", "Error Screen", JOptionPane.WARNING_MESSAGE);
        }
        else if (tfUser.getText() != null && tfPass.getText() != null) 
        {
            if (source == bLogin)
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

                    // Create and Execute the PreparedStatement
                    String query = "SELECT * FROM USER_INFO WHERE Username = ? AND Password = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, tfUser.getText());
                    ps.setString(2, tfPass.getText());

                    ResultSet rs = ps.executeQuery();

                    // Retrieve the ResultSet                  
                    if (ctr < 3) 
                    {
                        // Check if login info exists
                        if(rs.next()) 
                        {
                            JOptionPane.showMessageDialog(fInfo, "Welcome back, " + tfUser.getText() + "!");

                            // Close Login Frame
                            fLogin.dispose();
                            
                            // Redirect to new page
                            // ListRecord fList = new ListRecord();
                            // fList.startApp(true);
                        } 
                        else 
                        {
                            JOptionPane.showMessageDialog(fInfo, "Your login details are incorrect. Please try again.", "Error Screen", JOptionPane.WARNING_MESSAGE);
                            ctr++;
                        }
                    } 
                    else 
                    {
                        JOptionPane.showMessageDialog(fInfo, "Sorry, you have reached the limit of 3 tries, good bye!", "Error Screen", JOptionPane.ERROR_MESSAGE);
                        System.exit(0);
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
    }

    // Main Class
    public static void main(String args[]) throws IOException {
        LoginSwing login = new LoginSwing();
        login.startApp();
    }
}
