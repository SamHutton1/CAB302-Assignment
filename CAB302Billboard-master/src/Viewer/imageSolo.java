package Viewer;
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * This method will be run by the renderer if there is only an image present as the billboard information
 */
public class imageSolo {
    public static void imageSolo(Container pane, URL ImgU, String backgroundColour) throws IOException {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int WinHeight = screenSize.height;
        int WinWidth = screenSize.width;

        BufferedImage MyImage;

            //reads image from url
        MyImage = ImageIO.read(ImgU);


        //get image dimensions for scaling
        ImageIcon MyImageIcon = new ImageIcon(MyImage);
        int ImgWidth = MyImageIcon.getIconWidth();
        int ImgHeight = MyImageIcon.getIconHeight();


        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;


        if (ImgWidth / ImgHeight > WinWidth / WinHeight) {
            //If the window has a wider ratio than the image, then should scale img to 50% Width

            c.ipadx = (int) (0.5 * WinWidth);
            c.ipady = (int) (((0.5 * WinWidth) / ImgWidth) * ImgHeight);
            Image newimg = MyImage.getScaledInstance((int) (0.5 * WinWidth), ((int) (((0.5 * WinWidth) / ImgWidth) * ImgHeight)), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way


            ImageIcon MyIcon = new ImageIcon(newimg);
            JLabel picLabel = new JLabel(MyIcon);
            picLabel.setBackground(Color.decode(backgroundColour));
            pane.add(picLabel, c);
        } else {
            //If the window has a taller ratio than the image, then should scale img to 50% height

            c.ipady = (int) (0.5 * WinHeight);
            c.ipadx = (int) (((0.5 * WinHeight) / ImgHeight) * ImgWidth);
            Image newimg = MyImage.getScaledInstance((int) (((0.5 * WinHeight) / ImgHeight) * ImgWidth), (int) (0.5 * WinHeight), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way

            ImageIcon MyIcon = new ImageIcon(newimg);
            JLabel picLabel = new JLabel(MyIcon);
            picLabel.setBackground(Color.decode(backgroundColour));
            pane.add(picLabel, c);
        }

    }
    public static void imageSolo(Container pane, byte[] ImgB , String backgroundColour) throws IOException {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int WinHeight = screenSize.height;
        int WinWidth = screenSize.width;

        BufferedImage MyImage;

        //reads image from Data

            //Read Image from Data
            MyImage = ImageIO.read(new ByteArrayInputStream(ImgB));


        //get image dimensions for scaling
        ImageIcon MyImageIcon = new ImageIcon(MyImage);
        int ImgWidth = MyImageIcon.getIconWidth();
        int ImgHeight = MyImageIcon.getIconHeight();


        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;


        if (ImgWidth / ImgHeight > WinWidth / WinHeight) {
            //If the window has a wider ratio than the image, then should scale img to 50% Width

            c.ipadx = (int) (0.5 * WinWidth);
            c.ipady = (int) (((0.5 * WinWidth) / ImgWidth) * ImgHeight);
            Image newimg = MyImage.getScaledInstance((int) (0.5 * WinWidth), ((int) (((0.5 * WinWidth) / ImgWidth) * ImgHeight)), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way


            ImageIcon MyIcon = new ImageIcon(newimg);
            JLabel picLabel = new JLabel(MyIcon);
            picLabel.setBackground(Color.decode(backgroundColour));
            pane.add(picLabel, c);
        } else {
            //If the window has a taller ratio than the image, then should scale img to 50% height

            c.ipady = (int) (0.5 * WinHeight);
            c.ipadx = (int) (((0.5 * WinHeight) / ImgHeight) * ImgWidth);
            Image newimg = MyImage.getScaledInstance((int) (((0.5 * WinHeight) / ImgHeight) * ImgWidth), (int) (0.5 * WinHeight), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way

            ImageIcon MyIcon = new ImageIcon(newimg);
            JLabel picLabel = new JLabel(MyIcon);
            picLabel.setBackground(Color.decode(backgroundColour));
            pane.add(picLabel, c);
        }

    }
}