package ControlPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import javax.swing.*;


public class controlPanel {

    private JLabel users;
    private JLabel billBoard;

    private JButton listUsers;
    private JButton createUsers;
    private JButton deleteUsers;
    private JButton editUsers;


    private JButton listBillboards;
    private JButton getBillboardInfo;
    private JButton createBillboard;
    private JButton editBillboard;
    private JButton scheduleBillboard;
    private JButton removeBillboard;

    private JButton logOut;


    private Font heading = new Font("Serif", Font.PLAIN, 20);

    /**
     * This is the contructor for the control panel and is called when a user logs in successfully and any time that
     * a user presses the back button from one of the other methods. The control panel is where a user will decide
     * what action they wish to take. When a user clicked on any action, the control panel will communicate with the
     * server and determine whether or not they have the permission to do that.
     */
    public controlPanel() {

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        frame.setTitle("Control Panel");
        frame.setSize(575, 345);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.add(panel);
        panel.setLayout(null);



        users = new JLabel("User Options");
        users.setFont(heading);
        users.setBounds(89, 10, 150, 25);
        panel.add(users);

        billBoard = new JLabel("Billboard Options");
        billBoard.setFont(heading);
        billBoard.setBounds(322, 10, 200, 25);
        panel.add(billBoard);

        listUsers = new JButton("List Users");
        listUsers.setBounds(60, 45, 150, 25);
        panel.add(listUsers);
        listUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Socket socket = new Socket("localhost", 4444);
                    OutputStream outputStream = socket.getOutputStream();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                    boolean permitted;

                    oos.writeUTF("clickedListUsers");
                    oos.flush();
                    permitted = ois.readBoolean();

                    oos.close();
                    ois.close();
                    socket.close();
                    if (permitted) {
                        listUsers listUsers = new listUsers();
                        frame.dispose();
                    }
                    else {
                        JOptionPane.showMessageDialog(panel, "you are not permitted to do this");
                    }


                } catch (IOException | SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });


        createUsers = new JButton("Create Users");
        createUsers.setBounds(60, 75, 150, 25);
        panel.add(createUsers);
        createUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //check that the current user is permitted to do this
                try {
                    Socket socket = new Socket("localhost", 4444);
                    OutputStream outputStream = socket.getOutputStream();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                    boolean permitted;

                    oos.writeUTF("clickedCreateUser");
                    oos.flush();
                    permitted = ois.readBoolean();

                    oos.close();
                    ois.close();
                    socket.close();
                    if (permitted) {
                        createUser createUser = new createUser();
                        frame.dispose();
                    }
                    else {
                        JOptionPane.showMessageDialog(panel, "you are not permitted to do this");
                    }


                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        deleteUsers = new JButton("Delete Users");
        deleteUsers.setBounds(60, 105, 150, 25);
        panel.add(deleteUsers);
        deleteUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Socket socket = new Socket("localhost", 4444);
                    OutputStream outputStream = socket.getOutputStream();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                    boolean permitted;

                    oos.writeUTF("clickedDeleteUser");
                    oos.flush();
                    permitted = ois.readBoolean();

                    oos.close();
                    ois.close();
                    socket.close();
                    if (permitted) {
                        deleteUsers deleteUsers = new deleteUsers();
                        frame.dispose();
                    }
                    else {
                        JOptionPane.showMessageDialog(panel, "you are not permitted to do this");
                    }

                } catch (IOException | SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });





        listBillboards = new JButton("List Billboards");
        listBillboards.setBounds(320, 45, 150, 25);
        panel.add(listBillboards);
        listBillboards.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    listBillboards listBillboards = new listBillboards();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                frame.dispose();
            }
        });

        createBillboard = new JButton("Create Billboard");
        createBillboard.setBounds(320, 105, 150, 25);
        panel.add(createBillboard);
        createBillboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //check that the user is permitted
                    Socket socket = new Socket("localhost", 4444);
                    OutputStream outputStream = socket.getOutputStream();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                    boolean permitted;

                    oos.writeUTF("clickedCreateBillboard");
                    oos.flush();
                    permitted = ois.readBoolean();

                    oos.close();
                    ois.close();
                    socket.close();
                    if (permitted) {
                        createBillboard createBillboard = new createBillboard();
                        frame.dispose();
                    }
                    else {
                        JOptionPane.showMessageDialog(panel, "you are not permitted to do this");
                    }


                } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        getBillboardInfo = new JButton("Get Billboard Info");
        getBillboardInfo.setBounds(320, 75, 150, 25);
        panel.add(getBillboardInfo);

        editBillboard = new JButton("Edit Billboard");
        editBillboard.setBounds(320, 135, 150, 25);
        panel.add(editBillboard);

        scheduleBillboard = new JButton("Schedule Billboard");
        scheduleBillboard.setBounds(320, 165, 150, 25);
        panel.add(scheduleBillboard);
        scheduleBillboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Socket socket = null;
                try {
                    socket = new Socket("localhost", 4444);
                    OutputStream outputStream = socket.getOutputStream();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                    boolean permitted;

                    oos.writeUTF("clickedScheduleBillboard");
                    oos.flush();
                    permitted = ois.readBoolean();

                    oos.close();
                    ois.close();
                    socket.close();

                    if (permitted) {
                        scheduleBillboard scheduleBillboard = new scheduleBillboard();
                        frame.dispose();
                    }
                    else {
                        JOptionPane.showMessageDialog(panel, "you are not permitted to do this");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            }
        });

        removeBillboard = new JButton("Remove Billboard");
        removeBillboard.setBounds(320, 195, 150, 25);
        panel.add(removeBillboard);

        logOut = new JButton("Log Out");
        logOut.setBackground(Color.orange);
        logOut.setBounds(194, 275, 150, 25);
        panel.add(logOut);
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginPage loginPage = new loginPage();
                frame.dispose();
            }
        });



        frame.setVisible(true);
    }
}
