import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ListItemPage {
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";  // Update with your Oracle DB details
    static final String DB_USER = "system"; // Your Oracle DB username
    static final String DB_PASSWORD = "AshAnish123"; // Your Oracle DB password

    public static void main(String[] args) {
        JFrame frame = new JFrame("List Item for Auction");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Create Item Name label and text field
        JLabel itemLabel = new JLabel("Item Name:");
        itemLabel.setBounds(50, 50, 100, 30);
        JTextField itemText = new JTextField();
        itemText.setBounds(150, 50, 200, 30);

        // Create Minimum Bid label and text field
        JLabel minBidLabel = new JLabel("Minimum Bid:");
        minBidLabel.setBounds(50, 100, 100, 30);
        JTextField minBidText = new JTextField();
        minBidText.setBounds(150, 100, 200, 30);

        // Create Time Limit label and text field
        JLabel timeLimitLabel = new JLabel("Time Limit (minutes):");
        timeLimitLabel.setBounds(50, 150, 150, 30);
        JTextField timeLimitText = new JTextField();
        timeLimitText.setBounds(200, 150, 150, 30);

        // Create List Item button
        JButton listItemButton = new JButton("List Item");
        listItemButton.setBounds(150, 200, 100, 30);

        // Add action listener to List Item button
        listItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemName = itemText.getText();
                double minBid = Double.parseDouble(minBidText.getText());
                int timeLimit = Integer.parseInt(timeLimitText.getText());

                if (listAuctionItem(itemName, minBid, timeLimit)) {
                    JOptionPane.showMessageDialog(frame, "Item Listed Successfully!");
                    frame.dispose(); // Close listing frame
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to List Item.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add components to the frame
        frame.add(itemLabel);
        frame.add(itemText);
        frame.add(minBidLabel);
        frame.add(minBidText);
        frame.add(timeLimitLabel);
        frame.add(timeLimitText);
        frame.add(listItemButton);
        

        // Set the frame visible
        frame.setVisible(true);
    }

    // Method to list an auction item
    public static boolean listAuctionItem(String itemName, double minBid, int timeLimit) {
        long currentTime = System.currentTimeMillis();
        long endTime = currentTime + (timeLimit * 60 * 1000); // Convert minutes to milliseconds
        Timestamp endTimestamp = new Timestamp(endTime);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO auctions (item_name, min_bid, end_time) VALUES (?, ?, ?)")) {

            stmt.setString(1, itemName);
            stmt.setDouble(2, minBid);
            stmt.setTimestamp(3, endTimestamp);
            int result = stmt.executeUpdate();
            return result > 0; // Return true if the listing was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Listing failed
        }
    }
}
