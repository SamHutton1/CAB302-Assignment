package Viewer;
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import javax.swing.text.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * This method will be run by the renderer if only picture data and information tags are present for the billboard
 */
public class PicInfo {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;

    Graphics g;

    public static int getMatchingFontSize(JComponent comp, String string) {
        int minSize = 10;
        int maxSize = 600;
        Dimension size = comp.getSize();

        if (comp == null || comp.getFont() == null || string.isEmpty()) {
            return -1;
        }
        //Init variables
        int width = size.width;
        int height = size.height;


        Font font = comp.getFont();

        int curSize = font.getSize();
        FontMetrics fm = comp.getFontMetrics(new Font(font.getName(), font.getStyle(), maxSize));
        //System.out.print("\nSize" + comp.getSize());
        while (fm.stringWidth(string) + 4 > width || fm.getHeight() > height) {
            maxSize--;
            fm = comp.getFontMetrics(new Font(font.getName(), font.getStyle(), maxSize));
            //System.out.print("\n" + curSize);
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
        //System.out.print(curSize);
        return curSize;
    }
    public static void PicInfo(Container pane, byte[] ImgB, String information, String informationColour, String backgroundColour) throws IOException, BadLocationException {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int WinHeight = screenSize.height;
        int WinWidth = screenSize.width;

        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();


        JTextArea TPane = new JTextArea(information);
        TPane.setSize((int) (WinWidth * 2), (int) (WinHeight - (WinHeight * 0.33 + WinHeight * .25)));
        TPane.setLineWrap(true);
        TPane.setEditable(false);
        TPane.setWrapStyleWord(true);
        TPane.setFont(new Font("Arial", Font.PLAIN, getMatchingFontSize(TPane, information)));
        TPane.setBackground(Color.decode(backgroundColour));
        TPane.setForeground(Color.decode(informationColour));
        pane.add(TPane);


            //Read Image from Data
        BufferedImage MyImage = ImageIO.read(new ByteArrayInputStream(ImgB));


        ImageIcon MyImageIcon = new ImageIcon(MyImage);
        int ImgWidth = MyImageIcon.getIconWidth();
        int ImgHeight = MyImageIcon.getIconHeight();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.ipady = 0;       //reset to default
        c.weighty = 0.5;   //request any extra vertical space

        c.anchor = GridBagConstraints.PAGE_END; //bottom of space

        c.gridx = 0;       //aligned with button 2

        c.gridy = 1;       //third row
        pane.setBackground(Color.white);
        if (ImgWidth / ImgHeight > WinWidth / WinHeight) {
            //means the window has a wider ratio than the image, and should scale img to Width


            c.ipady = (int) (((0.5 * WinWidth) / ImgWidth) * ImgHeight);
            Image newimg = MyImage.getScaledInstance((int) (0.5 * WinWidth), ((int) (((0.5 * WinWidth) / ImgWidth) * ImgHeight)), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way


            ImageIcon MyIcon = new ImageIcon(newimg);
            JLabel picLabel = new JLabel(MyIcon);
            picLabel.setBackground(Color.decode(backgroundColour));
            pane.add(picLabel, c);
        } else {


            Image newimg = MyImage.getScaledInstance((int) (((0.5 * WinHeight) / ImgHeight) * ImgWidth), (int) (0.5 * WinHeight), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way

            //c.ipady = (int) (1*WinHeight);
            int bottomTwoThirdsMiddle = (int) (WinHeight * 0.33);


            c.ipady = bottomTwoThirdsMiddle - (int) (0.5 * WinHeight * 0.5);

            ImageIcon MyIcon = new ImageIcon(newimg);
            JLabel picLabel = new JLabel(MyIcon);
            picLabel.setBackground(Color.decode(backgroundColour));
            pane.add(picLabel, c);
        }

    }
    public static void PicInfo(Container pane, URL ImgU, String information, String informationColour, String backgroundColour) throws IOException, BadLocationException {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int WinHeight = screenSize.height;
        int WinWidth = screenSize.width;

        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();


        JTextArea TPane = new JTextArea(information);
        TPane.setSize((int) (WinWidth * 2), (int) (WinHeight - (WinHeight * 0.33 + WinHeight * .25)));
        TPane.setLineWrap(true);
        TPane.setEditable(false);
        TPane.setWrapStyleWord(true);
        TPane.setFont(new Font("Arial", Font.PLAIN, getMatchingFontSize(TPane, information)));
        TPane.setBackground(Color.decode(backgroundColour));
        TPane.setForeground(Color.decode(informationColour));
        pane.add(TPane);


            //reads image from url
            Image MyImage = ImageIO.read(ImgU);



        ImageIcon MyImageIcon = new ImageIcon(MyImage);
        int ImgWidth = MyImageIcon.getIconWidth();
        int ImgHeight = MyImageIcon.getIconHeight();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.ipady = 0;       //reset to default
        c.weighty = 0.5;   //request any extra vertical space

        c.anchor = GridBagConstraints.PAGE_END; //bottom of space

        c.gridx = 0;       //aligned with button 2

        c.gridy = 1;       //third row
        pane.setBackground(Color.white);
        if (ImgWidth / ImgHeight > WinWidth / WinHeight) {
            //means the window has a wider ratio than the image, and should scale img to Width


            c.ipady = (int) (((0.5 * WinWidth) / ImgWidth) * ImgHeight);
            Image newimg = MyImage.getScaledInstance((int) (0.5 * WinWidth), ((int) (((0.5 * WinWidth) / ImgWidth) * ImgHeight)), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way


            ImageIcon MyIcon = new ImageIcon(newimg);
            JLabel picLabel = new JLabel(MyIcon);
            picLabel.setBackground(Color.decode(backgroundColour));
            pane.add(picLabel, c);
        } else {


            Image newimg = MyImage.getScaledInstance((int) (((0.5 * WinHeight) / ImgHeight) * ImgWidth), (int) (0.5 * WinHeight), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way

            //c.ipady = (int) (1*WinHeight);
            int bottomTwoThirdsMiddle = (int) (WinHeight * 0.33);


            c.ipady = bottomTwoThirdsMiddle - (int) (0.5 * WinHeight * 0.5);

            ImageIcon MyIcon = new ImageIcon(newimg);
            JLabel picLabel = new JLabel(MyIcon);
            picLabel.setBackground(Color.decode(backgroundColour));
            pane.add(picLabel, c);
        }

    }
}