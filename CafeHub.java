import java.awt.*;
import javax.swing.*;
import java.awt.event.* ;

class CafeHub extends JFrame implements ActionListener{
  CafeHub(){
    // JFrame jfrm = new JFrame("CafeHub");
    setLayout(new FlowLayout());
    setSize(300,300);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    JLabel jtf1 = new JLabel("Username:");
    JLabel jtf2 = new JLabel("Password:");

    JTextField usernameField = new JTextField(20);
    JTextField passwordField = new JTextField(20);

    JButton loginbtn = new JButton("Login");
    JButton clearbtn = new JButton("Clear")

    add(jtf1);
    add(usernameField);
    add(jtf2);
    add(passwordField);
    add(loginbtn);
    add(clearbtn);
    

    loginbtn.addActionListener(this);
    clearbtn.addActionListener(this);

    setVisible(true);  
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    System.out.println("Button clicked: " + e.getActionCommand());
  }
  public static void main(String args[]){
    new CafeHub();
  }
}