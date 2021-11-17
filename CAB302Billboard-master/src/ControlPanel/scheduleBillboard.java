package ControlPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class scheduleBillboard {

    public static JLabel title;
    public static JLabel whatDay;
    public static JLabel whatTime;
    public static JLabel endTime;

    public JComboBox BBtitle;
    public JComboBox day;
    public JComboBox startTimeHours;
    public JComboBox strtTimeMinutes;
    public JComboBox endTimeHours;
    public JComboBox endTimeMinutes;

    public JButton seeSchedule;
    public JButton goBack;
    public JButton submit;

    public scheduleBillboard() throws IOException {

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        frame.setTitle("Schedule Billboards");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.add(panel);
        panel.setLayout(null);

        title = new JLabel("Billboard Title:");
        title.setBounds(10, 20, 120, 25);
        panel.add(title);

        whatDay = new JLabel("Day:");
        whatDay.setBounds(10, 50, 120, 25);
        panel.add(whatDay);

        whatTime = new JLabel("Start Time");
        whatTime.setBounds(10, 80, 200, 25);
        panel.add(whatTime);

        endTime = new JLabel("End Time");
        endTime.setBounds(10, 110, 120, 25);
        panel.add(endTime);

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        //get list of available billboards
        ArrayList<String> listOfBillboards = new ArrayList<>();
        int size;
        //creates a connection to the server I DUNNO IF THIS WILL WORK
        Socket socket = new Socket("localhost", 4444);
        OutputStream outputStream = socket.getOutputStream();
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        //tell the server that it wants to list users
        oos.writeUTF("listBillboard");
        oos.flush();
        size = ois.readInt();
        for (int i = 0; i < size; i++) {
            listOfBillboards.add(ois.readUTF());
        }
        ois.close();
        oos.close();
        socket.close();

        BBtitle = new JComboBox(listOfBillboards.toArray());
        BBtitle.setBounds(200,20,120,25);
        panel.add(BBtitle);

        day = new JComboBox(days);
        day.setBounds(200, 50, 120, 25);
        panel.add(day);

        String[] hours = new String[24];

        for (int i = 0; i < 24; i++) {

                hours[i] = Integer.toString(i);
        }

        String[] minutes = new String[60];

        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minutes[i] = "0" + Integer.toString(i);
            }
            else {
                minutes[i] = Integer.toString(i);
            }
        }

        startTimeHours = new JComboBox(hours);
        startTimeHours.setBounds(200, 80, 50, 25);
        panel.add(startTimeHours);


        strtTimeMinutes = new JComboBox(minutes);
        strtTimeMinutes.setBounds(260, 80, 50, 25);
        panel.add(strtTimeMinutes);

        endTimeHours = new JComboBox(hours);
        endTimeHours.setBounds(200, 110, 50, 25);
        panel.add(endTimeHours);

        endTimeMinutes = new JComboBox(minutes);
        endTimeMinutes.setBounds(260, 110, 50, 25);
        panel.add(endTimeMinutes);

        submit = new JButton("Submit");
        submit.setBounds(360, 50, 80, 25);
        panel.add(submit);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //send to the database day of week start time and end time
                try {
                    Socket socket = new Socket("localhost", 4444);
                    OutputStream outputStream = socket.getOutputStream();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                    oos.writeUTF("scheduleBillboard");


                    if (day.getSelectedIndex() == 0) {
                        System.out.println(day.getSelectedIndex());
                        oos.writeInt(day.getSelectedIndex());
                    }
                    else  if (day.getSelectedIndex() == 1) {
                        oos.writeInt(1);
                    }
                    else  if (day.getSelectedIndex() == 2) {
                        oos.writeInt(2);
                    }
                    else  if (day.getSelectedIndex() == 3) {
                        oos.writeInt(3);
                    }
                    else  if (day.getSelectedIndex() == 4) {
                        oos.writeInt(4);
                    }

                    oos.writeUTF(hours[startTimeHours.getSelectedIndex()] + ":" + minutes[strtTimeMinutes.getSelectedIndex()]);
                    oos.writeUTF(hours[endTimeHours.getSelectedIndex()] + ":" + minutes[endTimeMinutes.getSelectedIndex()]);
                    String title = (String)BBtitle.getSelectedItem();
                    oos.writeUTF(title);
                    oos.close();
                    ois.close();
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        goBack = new JButton("Back");
        goBack.setBounds(360, 10, 80, 25);
        panel.add(goBack);
        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("back");
                controlPanel controlPanel = new controlPanel();
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }
}
