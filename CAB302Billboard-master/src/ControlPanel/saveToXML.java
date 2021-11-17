package ControlPanel;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;

/**
 * This class creates an xml file everytime a new billboard is created. As the SQLite database does not have recognise
 * an xml type, the xml is converted to a string before it is sent to the database.
 * The server connection is not established in the class - the string is sent back to createBillboard class and
 * sent to the server from there.
 */
public class saveToXML {

    public static final String xmlFilePath = createBillboard.getTitle.getText() + ".xml"; //to package folder

    //store colour values for use in conversions between Color --> hex types
    public static Color bgColour = stringToColor(createBillboard.selectBGC.getText());
    public static Color messColour = stringToColor(createBillboard.selectMessageColour.getText());
    public static Color infoColour = stringToColor(createBillboard.selectInfoColour.getText());

    public static void main(String[] args){
    }

    public static String returnXMLString () {

        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            // root element --> billboard
            Element billboard = document.createElement("billboard");
            Attr billboardAttr = document.createAttribute("Colour");
            billboardAttr.setValue("#" + Integer.toHexString(bgColour.getRGB()).substring(2));
            billboard.setAttributeNode(billboardAttr);
            document.appendChild(billboard);

            // message element
            if (!createBillboard.inputMessage.getText().isEmpty()) {
                Element message = document.createElement("message");
                billboard.appendChild(message);
                Attr messageAttr = document.createAttribute("Colour");
                messageAttr.setValue("#" + Integer.toHexString(messColour.getRGB()).substring(2));
                message.setAttributeNode(messageAttr);
                message.appendChild(document.createTextNode((String) createBillboard.inputMessage.getText()));
            }

            // picture element
            if (!createBillboard.inputMessage.getText().isEmpty()) {
                Element picture = document.createElement("picture");
                if (!createBillboard.imageURL.getText().isEmpty()) { //if image is in URL format
                    Attr pictureAttr = document.createAttribute("url");
                    pictureAttr.setValue(createBillboard.imageURL.getText());
                    picture.setAttributeNode(pictureAttr);
                    billboard.appendChild(picture);
                } else if (!createBillboard.imagePath.getText().isEmpty()) {
                    Attr pictureAttr = document.createAttribute("data");
                    pictureAttr.setValue(createBillboard.imageBase64);
                    picture.setAttributeNode(pictureAttr);
                    billboard.appendChild(picture);
                }
            }

            // information element
            if (!createBillboard.inputInformation.getText().isEmpty()) {
                Element information = document.createElement("information");
                Attr infoAttr = document.createAttribute("Colour");
                infoAttr.setValue("#" + Integer.toHexString(infoColour.getRGB()).substring(2));
                information.setAttributeNode(infoAttr);
                information.appendChild(document.createTextNode((String) createBillboard.inputInformation.getText()));
                billboard.appendChild(information);
            }
            
            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

            transformer.transform(domSource, streamResult);

            //SQLite does not have an xml data type, so the file is converted to a string for storing
            BufferedReader br = new BufferedReader(new FileReader(new File(xmlFilePath)));
            String line;
            StringBuilder xml = new StringBuilder();

            while ((line = br.readLine()) != null) {
                xml.append(line.trim());
            }

            String xmlString = xml.toString();

            System.out.println("xml string:" + xml);
            System.out.println("Done creating XML File");
            return xmlString;

        } catch (ParserConfigurationException | TransformerException | IOException pce) {
            pce.printStackTrace();
            return "";

        }

    }

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
}
