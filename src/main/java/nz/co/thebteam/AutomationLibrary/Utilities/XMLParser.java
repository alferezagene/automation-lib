package nz.co.thebteam.AutomationLibrary.Utilities;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.*;

public class XMLParser {
    public String xmlToParse = null;
    public Document doc;


    public XMLParser(String xmlToParse) {
        this.xmlToParse = xmlToParse;
        try {
            createXMLParser();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public XMLParser(String xmlToParse, Boolean hasNamespace) {
        this.xmlToParse = xmlToParse;
        try {
            createXMLParser();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void createXMLParser() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(xmlToParse)));
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //finds a single element (any namespace) or the first of multiple with the same name
    public String findElementValue(String elementToFind) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            return (String) xpath.evaluate("//*[local-name()='"+elementToFind+"']/text()", doc, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            System.out.println("The element " + elementToFind + " does not exist");
            return null;
        }


    }

    public String findElementAttribute(String xpath, String attributeName) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile("//" + xpath).evaluate(doc, XPathConstants.NODESET);

            //when it's a unique node name.
            //TODO write a new one for parsing nodes
            Node nNode = nodeList.item(0);
            Element eElement = (Element) nNode;
            return eElement.getAttribute(attributeName);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setElementValue(String xpath, String value) {

        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile("//" + xpath).evaluate(doc, XPathConstants.NODESET);

            //when it's a unique node name.
            //TODO write a new one for parsing nodes
            Node nNode = nodeList.item(0);
            Element eElement = (Element) nNode;
            eElement.setTextContent(value);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

    }

    public String toXMLString() {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }


    public void printAllElements() {
        NodeList nl = doc.getElementsByTagName("*");
        Element nsElement;
        String prefix;
        String localName;
        String nsName;

        System.out.println("The elements are: ");
        for (int i = 0; i < nl.getLength(); i++) {
            nsElement = (Element) nl.item(i);

            prefix = nsElement.getPrefix();
            System.out.println("  ELEMENT Prefix Name :" + prefix);

            localName = nsElement.getLocalName();
            System.out.println("  ELEMENT Local Name    :" + localName);

            nsName = nsElement.getNamespaceURI();
            System.out.println("  ELEMENT Namespace     :" + nsName);
        }
        System.out.println();
    }


}
