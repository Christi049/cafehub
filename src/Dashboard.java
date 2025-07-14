package src;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Dashboard extends JFrame implements ActionListener{

    private static JPanel leftPanel;
    private static JPanel rightPanel;
    private static JPanel mainPanel;
    private static JPanel contentPanel;
    private static JButton btnHome;
    private JLabel totalAmount;

    private java.util.Map<String, Integer> receiptMap = new java.util.LinkedHashMap<>();

    private void updateQuantity(JLabel quantityLabel, String dbName, int delta ) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafe_hub", "root", "Cmt@049");
            int qty = Integer.parseInt(quantityLabel.getText()) + delta;
            if (qty < 0) return; // Prevent negative quantity

            quantityLabel.setText(String.valueOf(qty));

            String sql = "UPDATE cafe_items SET quantity = ? WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, qty);
            pstmt.setString(2, dbName);
            pstmt.executeUpdate();
            pstmt.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int getPriceForItem(String displayName) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafe_hub", "root", "Cmt@049")) {
            String sql = "SELECT price FROM cafe_items WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, displayName.replaceAll("[^a-zA-Z ]", "").trim()); // Remove emoji
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("price");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private void updateReceipt() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        int total = 0;
        for (java.util.Map.Entry<String, Integer> entry : receiptMap.entrySet()) {
            String item = entry.getKey();
            int qty = entry.getValue();
            if (qty <= 0) continue; 

            int price = getPriceForItem(item);
            int itemTotal = qty * price;
            total += itemTotal;

            JPanel itemRow = new JPanel();
            itemRow.setLayout(new BoxLayout(itemRow, BoxLayout.X_AXIS));
            itemRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // consistent height
            itemRow.setAlignmentX(Component.LEFT_ALIGNMENT); // align to left
            itemRow.setOpaque(false);
            itemRow.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); 

            JLabel leftLabel = new JLabel(item + " x" + qty);
            leftLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            leftLabel.setHorizontalAlignment(SwingConstants.LEFT);

            JLabel rightLabel = new JLabel("₹" + itemTotal);
            rightLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            rightLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            itemRow.add(leftLabel);
             itemRow.add(Box.createHorizontalGlue());
            itemRow.add(rightLabel);

            contentPanel.add(itemRow);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // adds spacing between rows
        }

        contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Adds vertical spacing between items

        totalAmount.setText("₹" + total);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void resetQuantities() {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafe_hub", "root", "Cmt@049")) {
        String sql = "UPDATE cafe_items SET quantity = 0";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
        pstmt.close();
    } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Dashboard(){
        setTitle("CafeHub Dashboard");
        setSize(600,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.setPreferredSize(new Dimension(600,50));
        topPanel.setLayout(new GridBagLayout());
        JLabel title = new JLabel("Welcome to CafeHub");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        topPanel.add(title);

        //Left Panel
        leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(283,0));
        leftPanel.setLayout(new BorderLayout());
        
        JPanel buttonRow = new JPanel(new BorderLayout());

        btnHome = new JButton("Menu");
        btnHome.addActionListener(this);

        buttonRow.add(btnHome);
        buttonRow.setBorder(BorderFactory.createEmptyBorder(12, 10, 5, 10));// adds 20px padding at the top of the button row

        leftPanel.add(buttonRow, BorderLayout.NORTH);

        //main panel
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);

        //right panel
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.lightGray);
        rightPanel.setPreferredSize(new Dimension(300, 0));

        JPanel titlePanel = new JPanel(new BorderLayout()); // Changed to BorderLayout
        titlePanel.setPreferredSize(new Dimension(300, 40));
        titlePanel.setMaximumSize(new Dimension(300, 40));
        titlePanel.setBackground(Color.WHITE);
    
        JLabel receipt = new JLabel("RECEIPT");
        receipt.setFont(new Font("SansSerif", Font.BOLD, 13));
        receipt.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(receipt, BorderLayout.CENTER);
    
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setPreferredSize(new Dimension(300, 250));
        contentPanel.setMaximumSize(new Dimension(300, 250));
        contentPanel.setMinimumSize(new Dimension(300, 250));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(300, 230)); // Set fixed height
        scrollPane.setMaximumSize(new Dimension(300, 230));
        scrollPane.setMinimumSize(new Dimension(300, 230));
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER); 

        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setPreferredSize(new Dimension(300, 40));
        totalPanel.setMaximumSize(new Dimension(300, 40));
        totalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        totalPanel.setOpaque(false);

        JLabel totalLabel = new JLabel("Total:");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalAmount = new JLabel("₹0"); // reference for updates
        totalAmount.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalAmount.setHorizontalAlignment(SwingConstants.RIGHT);

        totalPanel.add(totalLabel, BorderLayout.WEST);
        totalPanel.add(totalAmount, BorderLayout.EAST);

        rightPanel.add(totalPanel, BorderLayout.SOUTH); 
        rightPanel.add(titlePanel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel,BorderLayout.EAST);
        add(mainPanel,BorderLayout.CENTER);
        resetQuantities();

        setVisible(true);
    } 
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == btnHome){

            JPanel itemsPanel = new JPanel();
            itemsPanel.setLayout(new  BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));

            String[] cafeItems = {
                "☕ Espresso",
                "🥐 Croissant",
                "🍵 Matcha Latte",
                "🥞 Pancakes",
                "🍔 Veg Burger",
                "🍟 French Fries",
            };

            String[] dbNames = {
                "Espresso",
                "Croissant",
                "Matcha Latte",
                "Pancakes",
                "Veg Burger",
                "French Fries"
            };

            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafe_hub", "root", "Cmt@049");

                for (int i = 0; i < cafeItems.length; i++) {
                    String displayName = cafeItems[i];   // with emoji
                    String dbName = dbNames[i]; 
                    String sql = "SELECT price, quantity FROM cafe_items WHERE name = ?";
                    PreparedStatement pstmt =  conn.prepareStatement(sql);
                    pstmt.setString(1,dbName);
                    ResultSet rs = pstmt.executeQuery();

                    if(rs.next()){
                        int price = rs.getInt("price");
                        int quantity = rs.getInt("quantity");

                        JPanel itemRow = new JPanel();
                        itemRow.setLayout(new BoxLayout(itemRow, BoxLayout.X_AXIS));
                        itemRow.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                        itemRow.setAlignmentY(Component.CENTER_ALIGNMENT);
                        itemRow.setPreferredSize(new Dimension(280, 40));

                        JLabel itemLabel = new JLabel(displayName + " - ₹" + String.valueOf(price));
                        itemLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                        itemLabel.setVerticalAlignment(SwingConstants.CENTER);
                        itemLabel.setHorizontalAlignment(SwingConstants.LEFT);

                        itemRow.add(itemLabel);
                        itemRow.add(Box.createHorizontalGlue());

                        JPanel toggleRow = new JPanel(); 
                        toggleRow.setLayout(new BoxLayout(toggleRow, BoxLayout.X_AXIS));
                        toggleRow.setOpaque(false);
                        toggleRow.setAlignmentY(Component.CENTER_ALIGNMENT);

                        JButton minusButton = new JButton("-");
                        JLabel quantityLabel = new JLabel(String.valueOf(quantity));
                        JButton plusButton = new JButton("+");

                        minusButton.addActionListener(ae -> {
                            updateQuantity(quantityLabel, dbName, -1);
                            int currentQty = receiptMap.getOrDefault(displayName, 0);
                            if (currentQty > 1) {
                                receiptMap.put(displayName, currentQty - 1);
                            } else {
                                receiptMap.remove(displayName); //Remove item completely if qty is 0 or 1
                            }
                             updateReceipt();
                        });

                        plusButton.addActionListener(ae -> {
                            updateQuantity(quantityLabel, dbName, +1);
                            receiptMap.put(displayName, receiptMap.getOrDefault(displayName, 0) + 1);
                            updateReceipt();
                        });

                        toggleRow.add(minusButton);
                        toggleRow.add(Box.createRigidArea(new Dimension(5, 0)));
                        toggleRow.add(quantityLabel);
                        toggleRow.add(Box.createRigidArea(new Dimension(5, 0)));
                        toggleRow.add(plusButton);

                        itemRow.add(toggleRow);
                        itemsPanel.add(itemRow);
                    }
                    rs.close();
                    pstmt.close();
                }
                conn.close();
                leftPanel.add(itemsPanel, BorderLayout.CENTER);
                leftPanel.revalidate();
                leftPanel.repaint();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}