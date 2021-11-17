package ControlPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This classes is called through the createBillboard class and is used to show a graphical preview of a billboard
 * as it is being designed.
 */
public class graphicalViewBB {

    /**
     * This function was expanded on from work found at:
     * http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Convertsagivenstringintoacolor.htm
     * The function takes a string that represents a colour, either as a word or in hex form, and produces a
     * Color type to match that of the input string.
     * @param value
     * @return Color type
     */
    public static Color stringToColor(final String value) {
        if (value == null) {
            return Color.white;
        }
        try {
            //find colour through hex value
            return Color.decode(value);
        } catch (NumberFormatException nfe) {
            // if no hex value was provided, try finding the colour by name through reflection
            try {
                final Field f = Color.class.getField(value);
                return (Color) f.get(null);
            } catch (Exception ce) {
                // if given colour doesn't exist, use white as default
                return Color.white;
            }
        }
    }

    /**
     * This function is a constructor of the class and is used to provide a rough graphical view of a Billboard as
     * it is being created. The layouts for the billboard elements are not all implemented correctly, though all of them
     * are able to be shown on the display. The intent of this graphical view is to give a rough idea of colour schemes
     * and whether or not a specified image is valid (in either url or as a path)
     * @throws IOException
     */
    public graphicalViewBB() throws IOException {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.pack();
        frame.setTitle(createBillboard.getTitle.getText());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //make fullscreen
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize(); //get frame dimensions

        frame.add(panel);
        panel.setBackground(stringToColor((String)createBillboard.selectBGC.getText())); //set background color

        //get message and set colour
        String messageText = createBillboard.inputMessage.getText();
        JTextArea message = new JTextArea(messageText);
        message.setEditable(false);
        message.setBackground(null);
        message.setForeground(stringToColor((String)createBillboard.selectMessageColour.getText()));
        message.setFont(new Font("Arial",Font.BOLD,24));
        int text1_x = (int) ((dimension.getWidth() - frame.getWidth()) / 2 - 40);
        int text1_y = (int) ((dimension.getHeight() - frame.getHeight()) / 2 - 12);
        message.setBounds(text1_x, text1_y, (int) dimension.getWidth()-frame.getWidth(), 26);
        panel.add(message);

        //get information and set colour
        String information = createBillboard.inputInformation.getText();
        JTextArea info = new JTextArea(information);
        info.setEditable(false);
        info.setBackground(null);
        info.setForeground(stringToColor((String)createBillboard.selectInfoColour.getText()));
        int info_x = (int) Math.round(0.5*(dimension.getWidth()-frame.getWidth()));
        int info_y = (int) Math.round((2/3)*(dimension.getHeight()-frame.getHeight()));
        info.setBounds(info_x, info_y,(int)dimension.getWidth()-frame.getWidth(), (int)dimension.getHeight()-frame.getHeight());
        info.setCursor(null);
        info.setFocusable(true);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        panel.add(info);

        //image from path
        String imPath = createBillboard.imagePath.getText();
        panel.setLayout(new FlowLayout());
        ImageIcon image1;
        JLabel label1;
        image1 = new ImageIcon(getClass().getResource(imPath));
        label1 = new JLabel(image1);
        panel.add(label1, BorderLayout.CENTER);

        //add image through url
        BufferedImage image = ImageIO.read(new URL(createBillboard.imageURL.getText()));
        ImageIcon icon = new ImageIcon(image);
        frame.setLayout(new FlowLayout());
        System.out.println("Load image into frame...");
        JLabel label = new JLabel();
        label.setIcon(icon);
        frame.getContentPane().add(label);
        frame.pack();

        frame.setResizable(true);
        frame.setVisible(true);
    }
}
