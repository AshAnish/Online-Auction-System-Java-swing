import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AuctionListPage {
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";  // Update with your Oracle DB details
    static final String DB_USER = "system"; // Your Oracle DB username
    static final String DB_PASSWORD = "AshAnish123"; // Your Oracle DB password

    public static void main(String[] args) {
        JFrame frame = new JFrame("Available Auctions");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Label for available auctions
        JLabel label = new JLabel("Available Auctions");
        label.setBounds(50, 20, 200, 30);
        frame.add(label);

        // List to display auction items
        DefaultListModel<String> auctionListModel = new DefaultListModel<>();
        JList<String> auctionList = new JList<>(auctionListModel);
        auctionList.setBounds(50, 60, 400, 200);
        frame.add(auctionList);

        // Load auctions from the database
        loadAuctions(auctionListModel);

        // Button to place a bid
        JButton bidButton = new JButton("Place Bid");
        bidButton.setBounds(50, 280, 100, 30);
        bidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = auctionList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedAuction = auctionList.getSelectedValue();
                    String auctionId = selectedAuction.split(":")[0]; // Assuming format "1: Item Name"
                    BidPage.main(new String[]{auctionId}); // Pass auction ID
                    frame.dispose(); // Close auction list frame
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select an auction to bid on.");
                }
            }
        });
        frame.add(bidButton);
        

        // Set the frame visible
        frame.setVisible(true);
    }

    // Method to load auctions from the database
    public static void loadAuctions(DefaultListModel<String> auctionListModel) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT auction_id, item_name, min_bid FROM auctions")) {

            while (rs.next()) {
                int auctionId = rs.getInt("auction_id");
                String itemName = rs.getString("item_name");
                double minBid = rs.getDouble("min_bid");
                auctionListModel.addElement(auctionId + ": " + itemName + " (Min Bid: $" + minBid + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
