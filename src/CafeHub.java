package src;
import javax.swing.*;
import java.awt.event.*;

class CafeHub extends JFrame implements ActionListener{

  private static JTextField usernameField;
  private static JPasswordField passwordField;
  private static JButton loginbtn , clearbtn;
  private static JLabel jlbl1 , jlbl2 , success;


  CafeHub(){
    setTitle("CafeHub");
    setSize(350,200);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(null);

    jlbl1 = new JLabel("Username:");
    jlbl1.setBounds(15,20,80,25);
    jlbl2 = new JLabel("Password:");
    jlbl2.setBounds(15,50,80,25);

    usernameField = new JTextField(20);
    usernameField.setBounds(150,20,165,25);
    passwordField = new JPasswordField(20);
    passwordField.setBounds(150,50,165,25);

    loginbtn = new JButton("Login");
    loginbtn.setBounds(89,100,80,25);
    clearbtn = new JButton("Clear");
    clearbtn.setBounds(167,100,80,25);

    success = new JLabel("");
    success.setBounds(118,125,300,25);
    
    add(jlbl1);
    add(usernameField);
    add(jlbl2);
    add(passwordField);
    add(loginbtn);
    add(success);
    add(clearbtn);

    loginbtn.addActionListener(this);
    clearbtn.addActionListener(this);

    setVisible(true);  
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == loginbtn){ 
      String user = usernameField.getText();
      String pass = String.valueOf(passwordField.getPassword());
      if (user.equals("Christi") && pass.equals("Cmt049")) {
        success.setText("login successful");
        dispose(); 
        new Dashboard();
      }
      else{
        success.setText("Invalid Credentials");
      } 
    }
    else if(e.getSource() == clearbtn){
      usernameField.setText("");
      passwordField.setText("");
    } 
  }  

  public static void main(String args[]){
    try{
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e){
       try{
       // Fallback to cross-platform LAF
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
      }
    new CafeHub();
  }
}