package src;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Dashboard extends JFrame implements ActionListener{

    private static JPanel leftPanel;
    private static JButton btnHome;

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
        JButton btnOrders = new JButton("Orders");
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
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);

        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == btnHome){

            JPanel itemsPanel = new JPanel();
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

            String[] cafeItems = {
                "☕ Espresso - ₹120",
                "🥐 Croissant - ₹90",
                "🍵 Matcha Latte - ₹130",
                "🥞 Pancakes - ₹110",
                "🍔 Veg Burger - ₹140",
                "🍟 French Fries - ₹70",
            };

            for (String item : cafeItems) {
                JLabel itemLabel = new JLabel(item);
                itemLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                itemLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // ✅ Center in BoxLayout
                itemLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 15)); // spacing
                //itemLabel.setBorder(BorderFactory.createEmptyBorder(5, 10,5, 10)); // padding

                JPanel toggleRow = new JPanel(new GridLayout(1, 2, 5, 0)); 
                JButton minusButton = new JButton("-");
                JButton plusButton = new JButton("+");
                toggleRow.add(minusButton);
                toggleRow.add(plusButton);

                itemsPanel.add(itemLabel,BorderLayout.WEST);
                itemsPanel.add(toggleRow,BorderLayout.EAST);
            }

            leftPanel.add(itemsPanel, BorderLayout.CENTER);

            setVisible(true); 
        }
    }
}
