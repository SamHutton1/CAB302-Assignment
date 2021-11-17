package ControlPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class loginPage {

    //initialise variables
    private static JLabel label;
    private static JTextField userName;
    private static JLabel label2;
    private static JPasswordField password;
    private static JButton button;
    private static JLabel success;

    /**
     * This constructor is the first thing run when the main of the control panel is run. It present a text box to enter
     * a username and a password that can be sent to the server for verification. If verified, the main control panel
     * will be opened, otherwise they will receive an error message telling them the login is incorrect
     */
    public loginPage() {

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        frame.setTitle("Login Page");
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //move to middle of screen
        frame.setLocationRelativeTo(null);

        frame.add(panel);

        panel.setLayout(null);

        label = new JLabel("Username");
        label.setBounds(10, 20, 80, 25);
        panel.add(label);

        userName = new JTextField(20);
        userName.setBounds(100, 20, 165, 25);
        panel.add(userName);

        label2 = new JLabel("Password");
        label2.setBounds(10, 50, 80, 25);
        panel.add(label2);

        password = new JPasswordField();
        password.setBounds(100, 50, 165, 25);
        panel.add(password);

        button = new JButton("Login");
        button.setBounds(140, 80, 80, 25);
        panel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                try {
                    Socket socket = new Socket("localhost", 4444);
                    OutputStream outputStream = socket.getOutputStream();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                    String pass = new String (password.getText());
                    oos.writeUTF("loginRequest");
                    //hash the password before sending it to the server
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    byte[] hashedPassword = md.digest(pass.getBytes());
                    oos.writeUTF(userName.getText());
                    //instead send hashed password
                    oos.writeUTF(Server.server.bytesToString(hashedPassword));
                    oos.flush();
                    String validated = ois.readUTF();
                    if (validated.equals("verified")) {

                        controlPanel controlPanel = new controlPanel();
                        frame.dispose();
                    }
                    else {
                        System.out.println("invalid login");
                        
                    }


                    oos.close();
                    ois.close();
                    socket.close();
                } catch (IOException | NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                }

            }
        });


        success = new JLabel("");
        success.setBounds(10, 110, 300, 25);
        panel.add(success);


        frame.setVisible(true);

    }


}
