package ControlPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

public class createUser {

    private JLabel username;
    private JLabel password;
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton goBack;
    private JCheckBox createBillboards;
    private JCheckBox editAllBillboards;
    private JCheckBox scheduleBillboards;
    private JCheckBox editUsers;
    private JLabel permissions;
    private JButton submit;

    /**
     * This constructor is called any time that a user has decided to create a new user. Provides them text options
     * to enter a username and password and four check boxes to determine the types of permissions that user should
     * have. If they click submit, it communicates with the server and sends the information provided to the server
     * including the hashed password. There is also a back option to return to the control panel
     */
    public createUser() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        frame.setTitle("List Users");
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.add(panel);
        panel.setLayout(null);

        username = new JLabel("Username");
        username.setBounds(10, 20, 80, 25);
        panel.add(username);

        usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 165, 25);
        panel.add(usernameField);

        password = new JLabel("Password");
        password.setBounds(10, 50, 80, 25);
        panel.add(password);

        passwordField = new JTextField();
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        goBack = new JButton("Back");
        goBack.setBounds(175, 225, 75, 25);
        panel.add(goBack);
        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlPanel controlPanel = new controlPanel();
                frame.dispose();
            }
        });

        permissions = new JLabel("Which permissions does user have:");
        permissions.setBounds(10, 90, 400, 20);
        panel.add(permissions);

        createBillboards = new JCheckBox("Create Billboards");
        createBillboards.setBounds(10, 110, 150, 20);
        panel.add(createBillboards);


        editAllBillboards = new JCheckBox("Edit All Billboards");
        editAllBillboards.setBounds(10, 130, 150, 20);
        panel.add(editAllBillboards);

        scheduleBillboards = new JCheckBox("Schedule Billboards");
        scheduleBillboards.setBounds(10, 150, 150, 20);
        panel.add(scheduleBillboards);

        editUsers = new JCheckBox("Edit Users");
        editUsers.setBounds(10, 170, 150, 20);
        panel.add(editUsers);

        submit = new JButton("Submit");
        submit.setBounds(20, 225, 75, 25);
        panel.add(submit);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)  {
                //HERE IS WHERE WE SENT INFORMATION TO THE DATABASE <-----------
                try {
                    Socket socket = new Socket("localhost", 4444);
                    OutputStream outputStream = socket.getOutputStream();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                    String pass = new String (passwordField.getText());
                    oos.writeUTF("createUser");

                    oos.writeUTF(usernameField.getText());
                    //hash the password
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    byte[] hashedPassword = md.digest(pass.getBytes());

                    //send the sha hashed password to the server
                    oos.writeUTF(Server.server.bytesToString(hashedPassword));


                    oos.writeBoolean(createBillboards.isSelected());
                    oos.writeBoolean(scheduleBillboards.isSelected());
                    oos.writeBoolean(editAllBillboards.isSelected());
                    oos.writeBoolean(editUsers.isSelected());
                    oos.flush();

                    if (ois.readUTF().equals("good")) {
                    }
                    else {
                        JOptionPane.showMessageDialog(panel, "please enter a unique username");
                    }

                    oos.close();
                    ois.close();
                    socket.close();
                } catch (IOException | NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                }






            }
        });


        frame.setVisible(true);


    }
}
