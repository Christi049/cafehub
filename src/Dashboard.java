package src;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Dashboard extends JFrame implements ActionListener{

    private static JPanel leftPanel;
    private static JPanel rightPanel;
    private static JPanel mainPanel;
    private static JButton btnHome;
    private static JButton btnOrders;
    private java.util.Map<String, Integer> receiptMap = new java.util.LinkedHashMap<>();

    private void updateQuantity(JLabel quantityLabel, String dbName, int delta) {
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

    private void updateReceipt() {
        rightPanel.removeAll();

        for (java.util.Map.Entry<String, Integer> entry : receiptMap.entrySet()) {
            String item = entry.getKey();
            int qty = entry.getValue();
            JLabel label = new JLabel(item + " x" + qty);
            label.setFont(new Font("SansSerif", Font.PLAIN, 14));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            rightPanel.add(label);
        }

        rightPanel.revalidate();
        rightPanel.repaint();
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
        leftPanel.setPreferredSize(new Dimension(300,0));
        leftPanel.setLayout(new BorderLayout());

        JPanel buttonRow = new JPanel(new GridLayout(1, 3, 10, 10)); // 1 row, 3 columns, 10px horizontal gap

        btnHome = new JButton("Home");
        btnOrders = new JButton("Orders");
        JButton btnSettings = new JButton("Settings");

        btnHome.addActionListener(this);
        btnOrders.addActionListener(this);
        btnSettings.addActionListener(this);

        buttonRow.add(btnHome);
        buttonRow.add(btnOrders);
        buttonRow.add(btnSettings);
        buttonRow.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 10));// adds 20px padding at the top of the button row

        leftPanel.add(buttonRow, BorderLayout.NORTH);

        //main panel
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);

        //right panel
        rightPanel = new JPanel();
       // rightPanel.setPreferredSize(new Dimension(300,0));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        
        JLabel receipt = new JLabel("RECEIPT");
        receipt.setFont(new Font("SansSerif", Font.BOLD, 10));
        rightPanel.add(receipt);

        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel,BorderLayout.EAST);

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
                             receiptMap.put(displayName, receiptMap.getOrDefault(displayName, 0) + 1);
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
