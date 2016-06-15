package AutomationLibrary.Utilities;


import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.*;

public class XMLParser {
    public String xmlToParse = null;
    public Document doc;

    public XMLParser(String xmlToParser) {
        this.xmlToParse = xmlToParser;
        try {
            createXMLParser();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String findElementValue(String xpath) throws XPathExpressionException {

        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xPath.compile(xpath).evaluate(doc, XPathConstants.NODESET);

        //when it's a unique node name.
        //TODO write a new one for parsing nodes
        Node nNode = nodeList.item(0);
        Element eElement = (Element) nNode;
        return eElement.getTextContent();

    }


    public void createXMLParser() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(new InputSource(new ByteArrayInputStream(xmlToParse.getBytes("utf-8"))));
        doc.getDocumentElement().normalize();

    }

}
