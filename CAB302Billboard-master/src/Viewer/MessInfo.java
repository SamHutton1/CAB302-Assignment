package Viewer;
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import javax.swing.text.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * This method will be run by the renderer if only text and information tags are present for the billboard
 */
public class MessInfo {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;

    Graphics g;
    public static int getMatchingFontSize(JComponent comp, String string) {
        int minSize = 10;
        int maxSize = 200;
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
        System.out.print("\nSize"+comp.getSize());
        while (fm.stringWidth(string) + 4 > width || fm.getHeight() > height) {
            maxSize--;
            fm = comp.getFontMetrics(new Font(font.getName(), font.getStyle(), maxSize));
            System.out.print("\n"+curSize);
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
        System.out.print(curSize);
        return curSize;
    }
    public static void MessInfo(Container pane, String message,  String information, String informationColour, String messageColour, String backgroundColour ) throws IOException, BadLocationException {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int WinHeight= screenSize.height;
        int WinWidth= screenSize.width;

        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        ResizeLabelFont messageLabel=new ResizeLabelFont(message);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy=0;
        c.weighty=0.5;
        messageLabel.setForeground(Color.decode(messageColour));

        messageLabel.setBackground(Color.decode(backgroundColour));
        pane.add(messageLabel, c);



        JTextArea TPane = new JTextArea(information);
        TPane.setSize((int)(WinWidth*2),(int)(WinHeight-(WinHeight*0.33+WinHeight*.25)));
        TPane.setLineWrap(true);
        TPane.setEditable(false);
        TPane.setWrapStyleWord(true);
        c.gridy=1;
        TPane.setFont(new Font("Arial", Font.PLAIN, getMatchingFontSize(TPane,information)));
        TPane.setForeground(Color.decode(informationColour));
        TPane.setBackground(Color.decode(backgroundColour));

        pane.add(TPane);



    }
}