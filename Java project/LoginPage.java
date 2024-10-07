import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginPage {

    // JDBC URL, username, and password for Oracle database
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";  // Update with your Oracle DB details
    static final String DB_USER = "system"; // Your Oracle DB username
    static final String DB_PASSWORD = "AshAnish123"; // Your Oracle DB password

    public static void main(String[] args) {
        // Create the main frame for login
        JFrame frame = new JFrame("Login Screen");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Create Username label and text field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 30);
        JTextField userText = new JTextField();
        userText.setBounds(150, 50, 150, 30);

        // Create Password label and password field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 30);
        JPasswordField passText = new JPasswordField();
        passText.setBounds(150, 100, 150, 30);

        // Create Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(50, 150, 100, 30);

        // Create Register button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(200, 150, 100, 30);

        // Add action listener to Login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passText.getPassword());

                // Hash the password before verifying
                String hashedPassword = hashPassword(password);

                // Verify username and password
                if (verifyLogin(username, hashedPassword)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful!");
                    HomePage.main(new String[]{username});
                    frame.dispose(); // Close the login window
                     // Proceed to the home page
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add action listener to Register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the RegisterPage class
                frame.dispose(); // Close the current login window
                RegisterPage.main(null); // Open the register page
            }
        });

        // Add components to the frame
        frame.add(userLabel);
        frame.add(userText);
        frame.add(passLabel);
        frame.add(passText);
        frame.add(loginButton);
        frame.add(registerButton);

        // Set the frame visible
        frame.setVisible(true);
    }

    // Method to hash the password using SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to verify login by checking username and password hash in the database
    public static boolean verifyLogin(String username, String passwordHash) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE username = ? AND password_hash = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, passwordHash);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Return true if a matching user is found

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Login failed due to an SQL error
        }
    }
}