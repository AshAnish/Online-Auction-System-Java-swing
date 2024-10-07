import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class RegisterPage {

    // JDBC URL, username, and password for Oracle database
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";  // Update with your Oracle DB details
    static final String DB_USER = "system"; // Your Oracle DB username
    static final String DB_PASSWORD = "AshAnish123"; // Your Oracle DB password

    public static void main(String[] args) {
        // Create the main frame for registration
        JFrame frame = new JFrame("Register Screen");
        frame.setSize(450, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Create Username label and text field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 30);
        JTextField userText = new JTextField();
        userText.setBounds(180, 50, 150, 30);

        // Create Password label and password field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 30);
        JPasswordField passText = new JPasswordField();
        passText.setBounds(180, 100, 150, 30);

        // Create Confirm Password label and password field
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setBounds(50, 150, 150, 30);
        JPasswordField confirmPassText = new JPasswordField();
        confirmPassText.setBounds(180, 150, 150, 30);

        // Checkbox to toggle password visibility
        JCheckBox viewPassword = new JCheckBox("Show Password");
        viewPassword.setBounds(180, 180, 150, 30);
        viewPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (viewPassword.isSelected()) {
                    passText.setEchoChar((char) 0);
                    confirmPassText.setEchoChar((char) 0);
                } else {
                    passText.setEchoChar('*');
                    confirmPassText.setEchoChar('*');
                }
            }
        });

        // Create Email label and text field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 220, 100, 30);
        JTextField emailText = new JTextField();
        emailText.setBounds(180, 220, 150, 30);

        // Create Contact Number label and text field
        JLabel contactLabel = new JLabel("Contact Number:");
        contactLabel.setBounds(50, 270, 120, 30);
        JTextField contactText = new JTextField();
        contactText.setBounds(180, 270, 150, 30);

        // Create Register button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(150, 320, 100, 30);

        // Add action listener to Register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passText.getPassword());
                String confirmPassword = new String(confirmPassText.getPassword());
                String email = emailText.getText();
                String contactNumber = contactText.getText();

                // Perform validation
                if (!validateUsername(username)) {
                    JOptionPane.showMessageDialog(frame, "Username must contain only alphabets!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!validatePassword(password)) {
                    JOptionPane.showMessageDialog(frame, "Password must be at least 8 characters long and contain at least one uppercase letter, one number, and one special character!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(frame, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!validateEmail(email)) {
                    JOptionPane.showMessageDialog(frame, "Email must end with '.com'", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!validateContactNumber(contactNumber)) {
                    JOptionPane.showMessageDialog(frame, "Contact number must be exactly 10 digits!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Hash the password before storing it
                String hashedPassword = hashPassword(password);

                // Try to register the user in the database
                if (registerUser(username, hashedPassword, email, contactNumber)) {
                    JOptionPane.showMessageDialog(frame, "Registration Successful");

                    // After successful registration, return to login page
                    frame.dispose();  // Close the register window
                    LoginPage.main(null);  // Open the login page
                } else {
                    JOptionPane.showMessageDialog(frame, "Registration Failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add components to the frame
        frame.add(userLabel);
        frame.add(userText);
        frame.add(passLabel);
        frame.add(passText);
        frame.add(confirmPassLabel);
        frame.add(confirmPassText);
        frame.add(viewPassword);
        frame.add(emailLabel);
        frame.add(emailText);
        frame.add(contactLabel);
        frame.add(contactText);
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

    // Method to register a new user into the database
    public static boolean registerUser(String username, String passwordHash, String email, String contactNumber) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (username, password_hash, email, contact_number)" +
                     "VALUES (?, ?, ?, ?)")) {

            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            stmt.setString(3, email);
            stmt.setString(4, contactNumber);

            int result = stmt.executeUpdate(); // Returns the number of rows affected
            return result > 0; // Registration successful if a row was inserted
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Registration failed due to an SQL error
        }
    }

    // Validation methods

    // Validate username (only alphabets)
    public static boolean validateUsername(String username) {
        return Pattern.matches("^[a-zA-Z]+$", username);
    }

    // Validate password (at least 8 characters, 1 uppercase letter, 1 number, 1 special character)
    public static boolean validatePassword(String password) {
        return Pattern.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", password);
    }

    // Validate email (must end with ".com")
    public static boolean validateEmail(String email) {
        return email.endsWith(".com");
    }

    // Validate contact number (must be exactly 10 digits)
    public static boolean validateContactNumber(String contactNumber) {
        return Pattern.matches("^\\d{10}$", contactNumber);
    }
}
