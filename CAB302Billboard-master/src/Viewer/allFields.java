package Viewer;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;


public class allFields {
    public  static Dimension getTextSize(JLabel l, Font f) {
        Dimension size=new Dimension();

        FontMetrics fm=l.getFontMetrics(f);
        size.width=fm.stringWidth(l.getText());
        size.height=fm.getHeight();

        return size;
    }
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
     * This method will run by the renderer if all information for the billboard was provided
     * @param pane
     * @param message
     * @param imageP
     * @param information
     * @param colourMess
     * @param colourInfo
     * @param colourWall
     * @throws IOException
     */
    public  static void allFields(Container pane, String message, byte[] imageP, String information, String colourMess, String colourInfo, String colourWall) throws IOException {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int WinHeight= screenSize.height;
        int WinWidth= screenSize.width;
        pane.setLayout(new GridLayout(3,1));
        pane.setBackground(Color.decode(colourWall));



        //MESSAGE
        JLabel messageLabel= new JLabel(message);
        messageLabel.setBounds(0,0,WinWidth, (int) (WinHeight*0.33));

        Rectangle r=messageLabel.getBounds();
        int MIN_FONT_SIZE=3;
        int MAX_FONT_SIZE=240;
        int fontSize=MIN_FONT_SIZE;
        Font f=messageLabel.getFont();
        Rectangle r1=new Rectangle();
        Rectangle r2=new Rectangle();
        while (fontSize<MAX_FONT_SIZE) {
            r1.setSize(getTextSize(messageLabel, f.deriveFont(f.getStyle(), fontSize)));
            r2.setSize(getTextSize(messageLabel, f.deriveFont(f.getStyle(),fontSize+1)));
            if (r.contains(r1) && ! r.contains(r2)) {
                break;
            }
            fontSize++;
        }

        messageLabel.setFont(f.deriveFont(f.getStyle(),fontSize));
        messageLabel.setForeground(Color.decode(colourMess));
        messageLabel.setBackground(Color.decode(colourWall));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pane.add(messageLabel);



        //IMAGE



            //Read Image from Data
            BufferedImage MyImage = ImageIO.read(new ByteArrayInputStream(imageP));


        ImageIcon MyImageIcon = new ImageIcon(MyImage);
        int ImgWidth= MyImageIcon.getIconWidth();
        int ImgHeight= MyImageIcon.getIconHeight();
        Image newimg;
        if (ImgWidth / ImgHeight > WinWidth / WinHeight ){
            newimg = MyImage.getScaledInstance((int) (0.5 * WinWidth), ((int) (((0.5 * WinWidth) / ImgWidth) * ImgHeight)), Image.SCALE_SMOOTH);

        }
        else{
            newimg = MyImage.getScaledInstance((int) (((0.5 * WinHeight) / ImgHeight) * ImgWidth), (int) (0.5 * WinHeight), Image.SCALE_SMOOTH);

        }

        ImageIcon MyIcon= new ImageIcon(newimg);
        JLabel picLabel = new JLabel(MyIcon);

        pane.add(picLabel);




        //INFORMATION
        JTextArea TPane = new JTextArea(information);
        TPane.setLineWrap(true);
        TPane.setEditable(false);
        TPane.setWrapStyleWord(true);
        TPane.setSize(WinWidth*2,(int)(WinHeight*1/3));
        TPane.setFont(new Font("Arial", Font.PLAIN, getMatchingFontSize(TPane,information)));
        TPane.setForeground(Color.decode(colourInfo));
        TPane.setBackground(Color.decode(colourWall));
        pane.add(TPane);

    }

    public  static void allFields(Container pane, String message, URL imageP, String information, String colourMess, String colourInfo, String colourWall) throws IOException {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int WinHeight= screenSize.height;
        int WinWidth= screenSize.width;
        pane.setLayout(new GridLayout(3,1));
        pane.setBackground(Color.decode(colourWall));



        //MESSAGE
        JLabel messageLabel= new JLabel(message);
        messageLabel.setBounds(0,0,WinWidth, (int) (WinHeight*0.33));

        Rectangle r=messageLabel.getBounds();
        int MIN_FONT_SIZE=3;
        int MAX_FONT_SIZE=240;
        int fontSize=MIN_FONT_SIZE;
        Font f=messageLabel.getFont();
        Rectangle r1=new Rectangle();
        Rectangle r2=new Rectangle();
        while (fontSize<MAX_FONT_SIZE) {
            r1.setSize(getTextSize(messageLabel, f.deriveFont(f.getStyle(), fontSize)));
            r2.setSize(getTextSize(messageLabel, f.deriveFont(f.getStyle(),fontSize+1)));
            if (r.contains(r1) && ! r.contains(r2)) {
                break;
            }
            fontSize++;
        }

        messageLabel.setFont(f.deriveFont(f.getStyle(),fontSize));
        messageLabel.setForeground(Color.decode(colourMess));
        messageLabel.setBackground(Color.decode(colourWall));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        pane.add(messageLabel);



        //IMAGE



            Image MyImage = ImageIO.read(imageP);



        ImageIcon MyImageIcon = new ImageIcon(MyImage);
        int ImgWidth= MyImageIcon.getIconWidth();
        int ImgHeight= MyImageIcon.getIconHeight();
        Image newimg;
        if (ImgWidth / ImgHeight > WinWidth / WinHeight ){
            newimg = MyImage.getScaledInstance((int) (0.5 * WinWidth), ((int) (((0.5 * WinWidth) / ImgWidth) * ImgHeight)), Image.SCALE_SMOOTH);

        }
        else{
            newimg = MyImage.getScaledInstance((int) (((0.5 * WinHeight) / ImgHeight) * ImgWidth), (int) (0.5 * WinHeight), Image.SCALE_SMOOTH);

        }

        ImageIcon MyIcon= new ImageIcon(newimg);
        JLabel picLabel = new JLabel(MyIcon);

        pane.add(picLabel);




        //INFORMATION
        JTextArea TPane = new JTextArea(information);
        TPane.setLineWrap(true);
        TPane.setEditable(false);
        TPane.setWrapStyleWord(true);
        TPane.setSize(WinWidth*2,(int)(WinHeight*1/3));
        TPane.setFont(new Font("Arial", Font.PLAIN, getMatchingFontSize(TPane,information)));
        TPane.setForeground(Color.decode(colourInfo));
        TPane.setBackground(Color.decode(colourWall));
        TPane.setAlignmentX(Component.CENTER_ALIGNMENT);


        pane.add(TPane);

    }

}