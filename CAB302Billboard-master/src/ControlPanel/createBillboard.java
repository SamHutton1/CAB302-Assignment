package ControlPanel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;


/**
 * This class is called when a user chooses to create a new billboard. It only uses the createBillboard constructor function.
 */
public class createBillboard {

    /**
     * global fields called in the constructor function. These variables are also called in other classes like
     * saveToXML and graphicalViewBB
     */
    private static JButton goBack;
    static JLabel title;
    static JTextField getTitle;
    static JLabel BGC;
    static JTextField selectBGC;
    static JLabel messageColour;
    static JTextField selectMessageColour;
    static JLabel infoColour;
    static JTextField selectInfoColour;
    static JLabel whatMessage;
    static JTextField inputMessage;
    static JLabel whatInformation;
    static JTextField inputInformation;
    static JLabel whatImagePath;
    static JTextField imagePath;
    static JLabel or;
    static JLabel whatImageURL;
    static JTextField imageURL;
    static JButton viewBB;
    static JButton confirm;

    static String imageBase64;

    private Font heading = new Font("Serif", Font.PLAIN, 20);

    /**
     * This function produces a JFrame which extends the control panel and prompts the user to enter information about their new
     * billboard. Colour fields can be entered in either hex or just as colour words. The user can choose to view a rough graphical view
     * of their billboard as they are creating it. This view, however, is not representative of what the actual viewer would show as the
     * the placements of the billboard attributes are not correct. This tool is instead intended to be used to ensure a correct image file
     * path or url is provided (both cannot be entered into the same billboard), and to visualise the colour selections.
     */
    public createBillboard() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        frame.setTitle(" Create Billboards");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.add(panel);
        panel.setLayout(null);

        title = new JLabel("Billboard Title:");
        title.setBounds(10, 20, 120, 25);
        panel.add(title);

        BGC = new JLabel("Background:");
        BGC.setBounds(10, 50, 120, 25);
        panel.add(BGC);

        messageColour = new JLabel("Message Colour:");
        messageColour.setBounds(10, 80, 120, 25);
        panel.add(messageColour);

        infoColour = new JLabel("Information Colour:");
        infoColour.setBounds(10, 110, 120, 25);
        panel.add(infoColour);

        whatMessage = new JLabel("Message:");
        whatMessage.setBounds(10, 140, 120, 25);
        panel.add(whatMessage);

        whatInformation = new JLabel("Information:");
        whatInformation.setBounds(10, 170, 120, 25);
        panel.add(whatInformation);

        whatImagePath = new JLabel("Image Path:");
        whatImagePath.setBounds(10, 200, 120, 25);
        panel.add(whatImagePath);

        or = new JLabel ("OR");
        or.setBounds(75, 230, 120, 25);
        panel.add(or);

        whatImageURL = new JLabel("Image URL:");
        whatImageURL.setBounds(10, 260, 120, 25);
        panel.add(whatImageURL);

        getTitle = new JTextField();
        getTitle.setBounds(150, 20, 120, 25);
        panel.add(getTitle);

        selectBGC = new JTextField();
        selectBGC.setBounds(150, 50, 80,25);
        panel.add(selectBGC);

        selectMessageColour = new JTextField();
        selectMessageColour.setBounds(150,80,80,25);
        panel.add(selectMessageColour);

        selectInfoColour = new JTextField();
        selectInfoColour.setBounds(150, 110, 80, 25);
        panel.add(selectInfoColour);

        inputMessage = new JTextField();
        inputMessage.setBounds(150, 140, 200,25);
        panel.add(inputMessage);

        inputInformation = new JTextField();
        inputInformation.setBounds(150, 170, 200, 25);
        panel.add(inputInformation);

        imagePath = new JTextField();
        imagePath.setBounds(150, 200, 300, 25);
        panel.add(imagePath);

        imageURL = new JTextField();
        imageURL.setBounds(150, 260, 300, 25);
        panel.add(imageURL);

        viewBB = new JButton("View Billboard");
        viewBB.setBounds(360, 300, 150, 25);
        panel.add(viewBB);
        //if "see graphical view" button pressed, a new JFrame will be created which contains the fields
        //entered in the createBillboard frame. The new frame can be exited without closing the entire ControlPanel
        viewBB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("message:" + inputMessage.getText());
                System.out.println("info:" + inputInformation.getText());
                System.out.println("view graphical billboard");
                try {
                    graphicalViewBB graphicalViewBB = new graphicalViewBB();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        confirm = new JButton("Confirm Billboard");
        confirm.setBounds(360, 330, 150, 25);
        panel.add(confirm);
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("confirm billboard:" + getTitle.getText());

                //change image path format to base64 encoded (for format required by the XML)
                imageBase64 = Base64.getEncoder().encodeToString(imagePath.getText().getBytes());
                System.out.println("base64 " + imageBase64);

                //export BB to XML format --> done in separate class
                saveToXML saveToXML = new saveToXML();
                String[] xmlFilePath = {"filename.xml"};
                saveToXML.main(xmlFilePath);

                String billboardXML = ControlPanel.saveToXML.returnXMLString();

                //connects with the server to send the title of the created billboard for storing in the database
                try {
                    Socket socket = new Socket("localhost", 4444);
                    OutputStream outputStream = socket.getOutputStream();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                    System.out.println("writing submit billboard");
                    oos.writeUTF("submitBillboard");

                    oos.writeUTF(getTitle.getText());
                    oos.writeUTF(billboardXML);

                    oos.close();
                    ois.close();
                    socket.close();
                } catch (IOException ex) {
                    System.out.println("An error occurred.");
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
