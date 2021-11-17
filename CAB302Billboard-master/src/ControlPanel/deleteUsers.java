package ControlPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;


public class deleteUsers {

    private JList username;
    private JList permissions;
    private JButton goBack;
    private JScrollPane scrollPane;
    private JLabel usernameTitle;
    private JButton delete;

    private JComboBox testing;

    private Font heading = new Font("Serif", Font.PLAIN, 20);

    /**
     * The list users contructor is called if a user clicked list users and has the permissions to do so. It receives
     * an array of all existing users from the database through the server and displays that array as a JOoptionPane
     * @throws IOException
     * @throws SQLException
     */
    public deleteUsers () throws IOException, SQLException {


        ArrayList<String> listOfUsernames = new ArrayList<>();
        int size;
        //creates a connection to the server
        Socket socket = new Socket("localhost", 4444);
        OutputStream outputStream = socket.getOutputStream();
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        //tell the server that it wants to list users
        oos.writeUTF("listUsers");
        oos.flush();
        size = ois.readInt();
        for (int i = 0; i < size; i++) {
            listOfUsernames.add(ois.readUTF());
        }



        ois.close();
        oos.close();
        socket.close();

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        frame.setTitle("List Users");
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);


        frame.add(panel);
        panel.setLayout(null);

        usernameTitle = new JLabel("Usernames");
        usernameTitle.setFont(heading);
        usernameTitle.setBounds(0, 0, 100, 20);
        panel.add(usernameTitle);

        testing = new JComboBox(listOfUsernames.toArray());
        testing.setBounds(0, 20, 150, 25);
        panel.add(testing);

        delete = new JButton("Delete");
        delete.setBounds(200, 25, 75, 50);
        panel.add(delete);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Socket socket = new Socket("localhost", 4444);
                    OutputStream outputStream = socket.getOutputStream();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(outputStream);

                    //tell server we want to delete a user
                    oos.writeUTF("deleteUser");

                    //tell the server the username of the user we want to delete
                    oos.writeUTF(listOfUsernames.get(testing.getSelectedIndex()));
                    oos.flush();

                    if (ois.equals("bad")) {
                        JOptionPane.showMessageDialog(panel, "cannot delete yourself");
                    }
                    else {
                        controlPanel controlPanel = new controlPanel();
                        frame.dispose();
                    }

                    oos.close();
                    ois.close();
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

//        username = new JList(listOfUsernames.toArray());
//        username.setVisibleRowCount(listOfUsernames.size());
//        panel.add(username);
//        username.setBounds(0, 0, 150, 500);

//        scrollPane = new JScrollPane();
//        scrollPane.setViewportView(username);
//        scrollPane.setBounds(0, 20, 200, 280);
//        panel.add(scrollPane);

        goBack = new JButton("Back");
        panel.add(goBack);
        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("back");
                controlPanel controlPanel = new controlPanel();
                frame.dispose();
            }
        });
        goBack.setBounds(200, 200, 75, 50);

        frame.add(panel);
        frame.setVisible(true);

    }
}
