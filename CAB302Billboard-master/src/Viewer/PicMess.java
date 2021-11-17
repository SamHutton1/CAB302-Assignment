package Viewer;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * This method will be run by the renderer if only picture data and message data are found for the billboard
 */
public class PicMess {

    public static Dimension getTextSize(JLabel l, Font f) {
        Dimension size=new Dimension();
        //g.setFont(f);
        FontMetrics fm=l.getFontMetrics(f);
        size.width=fm.stringWidth(l.getText());
        size.height=fm.getHeight();

        return size;
    }
    public static int getMatchingFontSize(JComponent comp, String string) {
        int minSize = 10;
        int maxSize = 200;
        Dimension size = comp.getSize();

        if (comp == null || comp.getFont() == null || string.isEmpty()) {
            System.out.print("woops");
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
    public static void PicMess(Container pane,  URL imageP, String message, String messageCol, String backgroundCol) throws IOException, BadLocationException {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int WinHeight= screenSize.height;
        int WinWidth= screenSize.width;


        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

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
        messageLabel.setForeground(Color.decode(messageCol));
        messageLabel.setBackground(Color.decode(backgroundCol));
        messageLabel.setFont(f.deriveFont(f.getStyle(),fontSize));





        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.weighty = 0.5;

        Dimension messDim= new Dimension(WinWidth, (int) (WinHeight*0.33));
        messageLabel.setMaximumSize(messDim);

        pane.add(messageLabel, c);



            //reads image from url
            Image MyImage = ImageIO.read(imageP);


        ImageIcon MyImageIcon = new ImageIcon(MyImage);
        int ImgWidth= MyImageIcon.getIconWidth();
        int ImgHeight= MyImageIcon.getIconHeight();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.ipady = 0;       //reset to default
        c.weighty = 0.5;   //request any extra vertical space

        c.anchor = GridBagConstraints.PAGE_END; //bottom of space

        c.gridx = 0;       //aligned with button 2

        c.gridy = 1;       //third row
        pane.setBackground(Color.white);
        if (ImgWidth / ImgHeight > WinWidth / WinHeight ){
            //means the window has a wider ratio than the image, and should scale img to Width


            c.ipady = (int) (((0.5*WinWidth)/ImgWidth)*ImgHeight);
            Image newimg = MyImage.getScaledInstance((int) (0.5*WinWidth), ((int) (((0.5*WinWidth)/ImgWidth)*ImgHeight)),  Image.SCALE_SMOOTH); // scale it the smooth way


            ImageIcon MyIcon= new ImageIcon(newimg);
            JLabel picLabel = new JLabel(MyIcon);

            pane.add(picLabel, c);
        }
        else{
            //opposite to above

            Image newimg = MyImage.getScaledInstance((int) (((0.5*WinHeight)/ImgHeight)*ImgWidth), (int) (0.5*WinHeight),  Image.SCALE_SMOOTH); // scale it the smooth way

            int bottomTwoThirdsMiddle= (int) (WinHeight*0.33);


            c.ipady=bottomTwoThirdsMiddle-(int) (0.5*WinHeight*0.5);

            ImageIcon MyIcon= new ImageIcon(newimg);
            JLabel picLabel = new JLabel(MyIcon);
            pane.add(picLabel, c);
        }

    }

    public static void PicMess(Container pane,  byte[] imageP, String message, String messageCol, String backgroundCol) throws IOException, BadLocationException {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int WinHeight= screenSize.height;
        int WinWidth= screenSize.width;


        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

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
        messageLabel.setForeground(Color.decode(messageCol));
        messageLabel.setBackground(Color.decode(backgroundCol));
        messageLabel.setFont(f.deriveFont(f.getStyle(),fontSize));





        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.weighty = 0.5;

        Dimension messDim= new Dimension(WinWidth, (int) (WinHeight*0.33));
        messageLabel.setMaximumSize(messDim);

        pane.add(messageLabel, c);


        BufferedImage MyImage = ImageIO.read(new ByteArrayInputStream(imageP));

        ImageIcon MyImageIcon = new ImageIcon(MyImage);
        int ImgWidth= MyImageIcon.getIconWidth();
        int ImgHeight= MyImageIcon.getIconHeight();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.ipady = 0;       //reset to default
        c.weighty = 0.5;   //request any extra vertical space

        c.anchor = GridBagConstraints.PAGE_END; //bottom of space

        c.gridx = 0;       //aligned with button 2

        c.gridy = 1;       //third row
        pane.setBackground(Color.white);
        if (ImgWidth / ImgHeight > WinWidth / WinHeight ){
            //means the window has a wider ratio than the image, and should scale img to Width


            c.ipady = (int) (((0.5*WinWidth)/ImgWidth)*ImgHeight);
            Image newimg = MyImage.getScaledInstance((int) (0.5*WinWidth), ((int) (((0.5*WinWidth)/ImgWidth)*ImgHeight)),  Image.SCALE_SMOOTH); // scale it the smooth way


            ImageIcon MyIcon= new ImageIcon(newimg);
            JLabel picLabel = new JLabel(MyIcon);

            pane.add(picLabel, c);
        }
        else{
            //opposite to above

            Image newimg = MyImage.getScaledInstance((int) (((0.5*WinHeight)/ImgHeight)*ImgWidth), (int) (0.5*WinHeight),  Image.SCALE_SMOOTH); // scale it the smooth way

            int bottomTwoThirdsMiddle= (int) (WinHeight*0.33);


            c.ipady=bottomTwoThirdsMiddle-(int) (0.5*WinHeight*0.5);

            ImageIcon MyIcon= new ImageIcon(newimg);
            JLabel picLabel = new JLabel(MyIcon);
            pane.add(picLabel, c);
        }

    }



}