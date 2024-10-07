import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class HomePage {
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";  // Update with your Oracle DB details
    static final String DB_USER = "system"; // Your Oracle DB username
    static final String DB_PASSWORD = "AshAnish123"; // Your Oracle DB password

    public static void main(String[] args) {
        String username = args[0]; // Get the username from the LoginPage

        JFrame frame = new JFrame("Home Page");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setBounds(50, 20, 300, 30);
        frame.add(welcomeLabel);

        // Create a button to list auctions
        JButton listAuctionsButton = new JButton("View Auctions");
        listAuctionsButton.setBounds(50, 70, 200, 30);
        frame.add(listAuctionsButton);

        // Create a button for listing an item for auction
        JButton listItemButton = new JButton("List Item for Auction");
        listItemButton.setBounds(50, 120, 200, 30);
        frame.add(listItemButton);

        // Create a button for logging out
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(50, 170, 200, 30);
        frame.add(logoutButton);

        // Action listener to view auctions
        listAuctionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AuctionListPage.main(null); // Call AuctionListPage
            }
        });

        // Action listener to list an item
        listItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListItemPage.main(null); // Call ListItemPage
            }
        });

        // Action listener for logout
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginPage.main(null); // Return to login page
                frame.dispose(); // Close home page
            }
        });

        frame.setVisible(true);
    }
}
