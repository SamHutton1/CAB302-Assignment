package ControlPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;

/**
 * This class is called from the control panel and displays a list of titles of all of the billboards
 * in the database at any time.
 */
public class listBillboards {

    private JLabel title;
    private JButton goBack;
    static JList BBlist;
    private Font heading = new Font("Ariel", Font.PLAIN, 20);

    /**
     * Creates a string array which is used at various times throughout the listBillboards() constructor
     * @param arr
     * @return string array
     */
    public static String[] GetStringArray(ArrayList<String> arr){
        String str[] = new String[arr.size()];
        for(int j = 0; j < arr.size(); j++){
            str[j] = arr.get(j);
        }
        return str;
    }

    /**
     * establishes a new JFrame and displays a list of titles for all of the billboards in the database at any time.
     * @throws IOException
     */
    public listBillboards() throws IOException {

        ArrayList<String> listOfBillboards = new ArrayList<>();
        int size;
        //creates a connection to the server
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

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        frame.setTitle("Billboards");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize(); //get frame dimensions
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.add(panel);
        panel.setLayout(null);

        title = new JLabel("All Billboards:");
        title.setFont(heading);
        title.setBounds(1, 10, 300, 25);
        panel.add(title);

        BBlist = new JList(listOfBillboards.toArray());
        BBlist.setBackground(null);
        BBlist.setBounds(1,40,(int)dimension.getWidth()-frame.getWidth(), (int)dimension.getHeight()-frame.getHeight());
        panel.add(BBlist);

        //go back to the control panel main window
        goBack = new JButton("Back");
        goBack.setBounds(360, 10, 150, 25);
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
