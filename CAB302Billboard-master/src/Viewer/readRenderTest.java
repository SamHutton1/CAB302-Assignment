package Viewer;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import org.w3c.dom.*;

import static org.junit.jupiter.api.Assertions.*;

class readRenderTest {


    @Test
    void normalCase() throws IllegalAccessException, BadLocationException, InstantiationException, IOException, UnsupportedLookAndFeelException, ClassNotFoundException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        File fileX= new File("9.xml");
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(fileX);








        readRender.xmlParser(doc);

    }


    @Test
    void xmlParser() {
        //test1
        //run xml with real xml file
        //test2
        //run parser with file that isnt xml
    }

    @Test
    void detectInfo() {
    }

    @Test
    void render() throws IllegalAccessException, BadLocationException, InstantiationException, IOException, UnsupportedLookAndFeelException, ClassNotFoundException {
        readRender.messSolo=true;
        readRender.message= "mess age 123";
        readRender.Render();
    }
}