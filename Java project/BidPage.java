import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BidPage {
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";  // Update with your Oracle DB details
    static final String DB_USER = "system"; // Your Oracle DB username
    static final String DB_PASSWORD = "AshAnish123"; // Your Oracle DB password

    public static void main(String[] args) {
        String auctionId = args[0]; // Get the auction ID from AuctionListPage

        JFrame frame = new JFrame("Place Bid");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Create Bid Amount label and text field
        JLabel bidAmountLabel = new JLabel("Bid Amount:");
        bidAmountLabel.setBounds(50, 50, 100, 30);
        JTextField bidAmountText = new JTextField();
        bidAmountText.setBounds(150, 50, 100, 30);

        // Create Place Bid button
        JButton placeBidButton = new JButton("Place Bid");
        placeBidButton.setBounds(80, 100, 120, 30);

        // Add action listener to Place Bid button
        placeBidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double bidAmount = Double.parseDouble(bidAmountText.getText());

                if (placeBid(auctionId, bidAmount)) {
                    JOptionPane.showMessageDialog(frame, "Bid Placed Successfully!");
                    frame.dispose(); // Close bid frame
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to Place Bid.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add components to the frame
        frame.add(bidAmountLabel);
        frame.add(bidAmountText);
        frame.add(placeBidButton);
        

        // Set the frame visible
        frame.setVisible(true);
    }

    // Method to place a bid
    public static boolean placeBid(String auctionId, double bidAmount) {
        // You would normally fetch the user_id from the session or database
        int userId = 1; // Placeholder user ID for testing

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO bids (auction_id, user_id, bid_amount) VALUES (?, ?, ?)")) {

            stmt.setInt(1, Integer.parseInt(auctionId));
            stmt.setInt(2, userId);
            stmt.setDouble(3, bidAmount);
            int result = stmt.executeUpdate();
            return result > 0; // Return true if the bid was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Bidding failed
        }
    }
}
