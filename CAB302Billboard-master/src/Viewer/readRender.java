/* will recieve an XML file. Render it */

package Viewer;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;

//import addComponentsToPane;
//import ResizeLabelFont;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

//import javax.swing.text.Document;
//import javax.swing.text.Document.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.zip.GZIPInputStream;

import Viewer.allFields;

import static Viewer.PicInfo.getMatchingFontSize;
import static Viewer.allFields.allFields;

/*figure out how file will be recieved, then read it. maybe convert to a string?
then REGEX for each element and implement.
how it should be implemented is on the ass sheet
Does a new Main need to be implemented? see what goes in main vs here, class vs java
also we have multiple srcs, the ass sheet says to not.
wont need to open the file, as it will be pushed from server?
find how to access, but basically assume have the xml file.
coord with the control panel for layout managers and their heirachys. doesnt matter, just render how it is
write reading from the file to set the variables.
messSolo
picSolo
infoSolo
messPic
messInfo
picInfo
allFields
Img
 */






public class readRender {

    public static boolean messSolo = false;
    public  static boolean picSolo =false;
    public  static boolean infoSolo = false;
    public    static boolean messPic = false;
    public    static boolean messInfo= false;
    public   static  boolean picInfo= false;
    public   static boolean allFields= false;
    public  static boolean UseImgAdd=false;
    public  static  boolean UseImgData=false;
    public  static  String messageColour="#000000";
    public  static String informationColour="#000000";
    public  static  String backgroundColour="#ffffff" ;
    public  static URL imageAddress;
    public static String message = "";
    public  static String information = "";
    public  static byte[] pictureData;
    public static String imageAddString = "";

    /*
    Search through provided XML file and save billboard specific information for use in the renderer
     */
    public  static int getMatchingFontSize(JComponent comp, String string) {
        int minSize = 10;
        int maxSize = 600;
        Dimension size = comp.getSize();
        System.out.print(size);
        if (comp == null || comp.getFont() == null || string.isEmpty()) {

            return -1;
        }

        int width = size.width;
        int height = size.height;


        Font font = comp.getFont();

        int curSize = font.getSize();
        FontMetrics fm = comp.getFontMetrics(new Font(font.getName(), font.getStyle(), maxSize));

        while (fm.stringWidth(string) + 4 > width || fm.getHeight() > height) {
            maxSize--;
            fm = comp.getFontMetrics(new Font(font.getName(), font.getStyle(), maxSize));

            curSize = maxSize;
        }
        while (fm.stringWidth(string) + 4 < width || fm.getHeight() < height) {
            minSize++;
            fm = comp.getFontMetrics(new Font(font.getName(), font.getStyle(), minSize));
            curSize = minSize;
        }
        if (curSize < minSize) {
            curSize = minSize;
        }
        if (curSize > maxSize) {
            curSize = maxSize;
        }

        return curSize;
    }

    /**
     * Converts a provided document to an XML and extracts parts from it.
     * Only works for files formatted to be billboards.
     * Will extract info such as billboard background, colour, message and picture URL or bytedata.
     * @param document
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws UnsupportedLookAndFeelException
     * @throws ClassNotFoundException
     * @throws BadLocationException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
     public static void xmlParser(Document document) throws IOException, ParserConfigurationException, SAXException, UnsupportedLookAndFeelException, ClassNotFoundException, BadLocationException, InstantiationException, IllegalAccessException {
        //Normalise structure
        document.getDocumentElement().normalize();
        //Root node
        Element root = document.getDocumentElement();

        NodeList nList = document.getElementsByTagName("billboard");

        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) node;
                //String error= eElement.getElementsByTagName("information").item(0).getAttributes().getNamedItem("colour").getTextContent();
                try {
                    if(eElement.getElementsByTagName("message").item(0).getAttributes().getNamedItem("colour").getTextContent() != null){
                        messageColour = eElement.getElementsByTagName("message").item(0).getAttributes().getNamedItem("colour").getTextContent();
                    }
                } catch (NullPointerException e) { }
                //if((eElement.getElementsByTagName("message").item(0).getAttributes().getNamedItem("colour").getTextContent() != null || eElement.getElementsByTagName("message").item(0).getAttributes().getNamedItem("colour").getTextContent()!= "")){

                try {
                    if ((eElement.getElementsByTagName("information").item(0).getAttributes().getNamedItem("colour").getTextContent() != null || eElement.getElementsByTagName("information").item(0).getAttributes().getNamedItem("colour").getTextContent() != "")) {
                        informationColour = eElement.getElementsByTagName("information").item(0).getAttributes().getNamedItem("colour").getTextContent();
                    }
                }
                catch (NullPointerException e){}

                try {
                    if (eElement.getAttribute("background") != null|| eElement.getElementsByTagName("message").item(0).getTextContent() != "") {
                        backgroundColour = eElement.getAttribute("background");
                        if (backgroundColour == ""){
                             backgroundColour="#ffffff";}

                    }
                }catch(NullPointerException e){}
                try {
                    if (eElement.getElementsByTagName("message").item(0).getTextContent() != null || eElement.getElementsByTagName("message").item(0).getTextContent() != "") {
                        message = eElement.getElementsByTagName("message").item(0).getTextContent();
                    }
                }catch(NullPointerException e){}

                try {
                    if (eElement.getElementsByTagName("information").item(0).getTextContent() != null || eElement.getElementsByTagName("information").item(0).getTextContent() != "") {
                        information = eElement.getElementsByTagName("information").item(0).getTextContent();
                    }
                }catch (NullPointerException e){}

                try {
                    if (eElement.getElementsByTagName("picture").item(0).getAttributes().getNamedItem("url").getNodeValue() != null || eElement.getElementsByTagName("picture").item(0).getAttributes().getNamedItem("url").getNodeValue() != "") {

                        imageAddString = eElement.getElementsByTagName("picture").item(0).getAttributes().getNamedItem("url").getNodeValue();
                        imageAddress = new URL(imageAddString); //error cuz empty

                        UseImgAdd = true;
                    }
                }catch (NullPointerException e){}
                try {
                    if (eElement.getElementsByTagName("picture").item(0).getAttributes().getNamedItem("data").getNodeValue() != null || eElement.getElementsByTagName("picture").item(0).getAttributes().getNamedItem("data").getNodeValue() != "") {
                        File file2 = new File("picData.xml");
                        file2.createNewFile();
                        FileWriter writer = new FileWriter(file2);
                        writer.write(eElement.getAttribute("picture data"));
                        UseImgData = true;
                    }
                } catch (NullPointerException e){}
            }
        }
        detectInfo();
    }

    /**
     * This method checks billboard data variables and checks if they are empty after the XMLparser has been called.
     *
     * @throws IllegalAccessException
     * @throws BadLocationException
     * @throws InstantiationException
     * @throws IOException
     * @throws UnsupportedLookAndFeelException
     * @throws ClassNotFoundException
     */
    static void detectInfo() throws IllegalAccessException, BadLocationException, InstantiationException, IOException, UnsupportedLookAndFeelException, ClassNotFoundException {

        if((UseImgData | UseImgAdd)&& information != "" && message != ""){
            allFields = true;
        } else if ((UseImgData| UseImgAdd)&& information != ""){
            picInfo = true;
        } else if (information != "" && message != ""){
            messInfo = true;
        } else if ((UseImgData | UseImgAdd)&& message != ""){
            messPic = true;
        } else if (message != ""){
            messSolo = true;
        } else if (information != ""){
            infoSolo = true;
        } else if ((UseImgData | UseImgAdd)){
            picSolo = true;
        }
        Render();
    }

    /**
     * This is the billboard renderer. It will display a billboard based on information extracted from the XMLParser.
     * It will display something slightly different based on the information gathered.
     * @throws ClassNotFoundException
     * @throws UnsupportedLookAndFeelException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws BadLocationException
     */
    static void Render() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException, BadLocationException {




        UIManager.setLookAndFeel((UIManager.getSystemLookAndFeelClassName()));
        JFrame frame = new JFrame("Billboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //create the layout
        if (messSolo){
            //maximise font, only 1 line. may be coloured. pos not specified

            ResizeLabelFont label=new ResizeLabelFont(message);

            frame.getContentPane().add(label);
            label.setForeground(Color.decode(messageColour));
            label.setBackground(Color.decode(backgroundColour));


        }
        else if (picSolo){//scaled for each side to taek up a maximise of half the screen dimensions. keep aspect ratio. display in centre. write a check to use image data vs URL and import





            if (UseImgAdd) {
                    imageSolo.imageSolo(frame.getContentPane(),imageAddress , backgroundColour);
            }
            else if(UseImgData){
                 imageSolo.imageSolo(frame.getContentPane(),pictureData ,backgroundColour);
            }
        }
        else if (infoSolo){// centred, word wrapping and size should make it fill up no more than 75% wedth n 50% height

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int WinHeight = screenSize.height;
            int WinWidth = screenSize.width;
            Container pane =frame.getContentPane();
            pane.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            JTextArea TPane = new JTextArea(information);
            TPane.setSize((int) (WinWidth*1.25), (int) (WinHeight*1.5));
            TPane.setLineWrap(true);
            TPane.setEditable(false);
            TPane.setWrapStyleWord(true);
            TPane.setFont(new Font("Arial", Font.PLAIN, getMatchingFontSize(TPane, information)));
            TPane.setBackground(Color.decode(backgroundColour));
            TPane.setForeground(Color.decode(informationColour));
            //c.weightx=1;

            c.fill = GridBagConstraints.HORIZONTAL;
            //c.fill = GridBagConstraints.CENTER;
            //c.ipadx = (int) (0.75 * WinWidth);
            //c.ipady = (int) (0.5*WinHeight);
            pane.setBackground(Color.decode(backgroundColour));
            pane.add(TPane,c);
        }
        else if (messPic) {// pic scaled for each side to taek up a maximise of half the screen dimensions. keep aspect ratio. in the midle of the bottom 2/3. text sized to fit above and centred in that space

            if (UseImgAdd) {
                PicMess.PicMess(frame.getContentPane(),imageAddress, message, messageColour,backgroundColour);

            }
            //reads image from Data
            else if(UseImgData) {
                //read from data from Img n get width + height
                PicMess.PicMess(frame.getContentPane(),pictureData, message, messageColour,backgroundColour);
            }

        }
        else if (messInfo) {//mess top half, info bot half. should be simple - do like pic info for info and put mess like mess pic


                MessInfo.MessInfo(frame.getContentPane(), message, information, informationColour,messageColour,backgroundColour );




        }
        else if (picInfo) {//pic middle top 3rd, with above sizing. info then fills bottom, centred. shouldnt be more than 75% width } this is implemented in the PicInfo file, just gotta make it take inputs


            if (UseImgAdd) {
                PicInfo.PicInfo(frame.getContentPane(),imageAddress, information, informationColour, backgroundColour);

            }
            //reads image from Data
            else if(UseImgData) {
                //read from data from Img n get width + height
                PicInfo.PicInfo(frame.getContentPane(),pictureData, information, informationColour, backgroundColour);
            }


        }
        else if (allFields) {// pic centre, 1/3 max screen width or heigh (keep ratio.) mess on top, info on bottom}
            if (UseImgAdd) {
                allFields(frame.getContentPane(),message,imageAddress,information,messageColour,
                        informationColour, backgroundColour);

            }
            //reads image from Data
            else if(UseImgData) {
                //read from data from Img n get width + height
                allFields(frame.getContentPane(),message,pictureData,information,messageColour,
                        informationColour, backgroundColour);
            }
        }


        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose ();
                System.exit (0);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                frame.dispose ();
                System.exit (0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    System.out.println("The key has been pressed");
                    System.exit(0);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    System.out.println("The key has been pressed");
                    System.exit(0);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    System.out.println("The key has been pressed");
                    System.exit(0);
                }
            }
        });



    }
}