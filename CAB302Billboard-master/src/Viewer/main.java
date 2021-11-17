package Viewer;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

//import static Viewer.main.setup;

public class main {

    public static void main(String[] args) throws IllegalAccessException, InterruptedException, BadLocationException, InstantiationException, ClassNotFoundException, UnsupportedLookAndFeelException, IOException, ParserConfigurationException, SAXException {
        main.setup();
    }
    //old function to receive file no longer needed

    /**
     * Attempts to make a connection to a server and receive a file.
     * Outdated by the setup() function that works much better and more efficiently.
     * @param args
     * @return
     * @throws IOException
     */
    static Document receive(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);


        Document xmlFile = null;
        try (
                Socket kkSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
                BufferedReader inputReader = new BufferedReader(
                        new InputStreamReader(kkSocket.getInputStream()));
        ) {
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;
            StringBuilder sb = new StringBuilder();
            String inline = "";
            while ((inline = inputReader.readLine()) != null) {
                sb.append(inline);
            }

            //System.out.println(sb.toString());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            xmlFile = (Document) builder.parse(String.valueOf(inputReader));


            while ((fromServer = inputReader.readLine()) != null) {
                //System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye."))
                    break;

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    //System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
            return xmlFile;
        } catch (UnknownHostException e) {
            //System.err.println("Don't know about host " + hostName);
            errorRender();
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            errorRender();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            errorRender();
        } catch (SAXException e) {
            e.printStackTrace();
            errorRender();
        }
        finally {
            return xmlFile;
        }
    }

    /**
     * This method should be called in the case that a connection to the billboard server cannot be found, it will display a placeholder error billboard
     */
    static void errorRender(){
        JFrame frame = new JFrame("Billboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ResizeLabelFont label=new ResizeLabelFont("There are no currently scheduled billboards or there was an error rendering. Reattempting every 15 seconds");

        frame.getContentPane().add(label);
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
    };

    /**
     * This is a method that makes a connection to a server every 15 seconds based on a provided properties file and attempts to receive a billboard file.
     * @throws InterruptedException
     * @throws IllegalAccessException
     * @throws BadLocationException
     * @throws InstantiationException
     * @throws IOException
     * @throws UnsupportedLookAndFeelException
     * @throws ClassNotFoundException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    static void setup() throws InterruptedException, IllegalAccessException, BadLocationException, InstantiationException, IOException, UnsupportedLookAndFeelException, ClassNotFoundException, ParserConfigurationException, SAXException {

        while(1 == 1){


        String billboardString = "";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = null;
            try {
                builder = factory.newDocumentBuilder();
            }
            catch (javax.xml.parsers.ParserConfigurationException ex) {
            }





        //get data from props.db - hostname etc to use in errors
        //<----- IMPLEMENT LOGIC FOR CONNECTION TO SERVER TO RECEIVE XML
        //make server details variables from props.db instead of hard coded
            try {
                Socket socket = new Socket("localhost", 4444);
                OutputStream outputStream = socket.getOutputStream();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                oos.writeUTF("currentBillboard");
                //i think we probably send the current time and check what that corresponds with in the database
                oos.flush();
                //billboardXML is either a billboard XML as a string or a message saying there are currently no scheduled billboards

                billboardString = ois.readObject().toString();
                //System.out.println(billboardXML);
                System.out.print(billboardString);
                //try{frame.dispose;}
                if (billboardString.equals("noBillboardsScheduled")){
                    System.out.print("no BB recognised");
                    errorRender();
                }
                else {
                    builder = factory.newDocumentBuilder();
                    org.w3c.dom.Document doc = builder.parse( new InputSource( new StringReader( billboardString ) ) );

                    readRender.xmlParser(doc);
                }
                //convert data to xml using receive function
                ois.close();
                oos.close();
                socket.close();
            }
            catch (UnknownHostException e) {
                System.err.println("Don't know about that host ");

                errorRender();
            } catch (IOException e) {
                System.err.println(e);
                e.printStackTrace();
                System.err.println(e.getStackTrace());
                errorRender();
            }

            if (billboardString.equals("noBillboardsScheduled")){errorRender();
                System.out.print("the seconds BB string check failed");}
            else {
                //we either need to chande read render to deal with string or figure out how to convert the XML string to an XML
                //render XML
//                readRender.xmlParser((org.w3c.dom.Document) XML);
//                readRender.detectInfo();
//                readRender.Render();
            }

            //wait 15 seconds for next request
            Thread.sleep(15000);
        }
    }



}
