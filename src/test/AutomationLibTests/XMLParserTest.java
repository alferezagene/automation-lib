package AutomationLibTests;

import AutomationLibrary.Utilities.XMLParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XMLParserTest {

    String xmlToParse = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "   <soap:Body>\n" +
            "      <soap:Fault>\n" +
            "         <faultcode>soap:Server</faultcode>\n" +
            "         <faultstring>Invalid Wibble</faultstring>\n" +
            "         <Error code=\"100\" message=\"Invalid Wibble\"></Error>\n" +
            "      </soap:Fault>\n" +
            "   </soap:Body>\n" +
            "</soap:Envelope>";


    String template = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:bkd=\"http://wibble\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <bkd:AccountBalances>\n" +
            "         <bkd:Header>\n" +
            "            <bkd:id>1</bkd:id>\n" +
            "            <bkd:idPrefix>0</bkd:idPrefix>\n" +
            "         </bkd:Header>\n" +
            "         <bkd:Request>\n" +
            "            <!--Optional:-->\n" +
            "            <bkd:WibbleNo>12345</bkd:WibbleNo>\n" +
            "            <!--Optional:-->\n" +
            "            <bkd:BibbleNo></bkd:BibbleNo>\n" +
            "         </bkd:Request>\n" +
            "      </bkd:AccountBalances>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";


    @Test
    public void XMLParserTestElement() {
        XMLParser xmlParser = new XMLParser(xmlToParse);
        assertEquals("soap:Server", xmlParser.findElementValue("faultcode"));
    }

    @Test
    public void XMLParserTestAttribute() {
        XMLParser xmlParser = new XMLParser(xmlToParse);
      assertEquals("100", xmlParser.findElementAttribute("Error", "code"));
        assertEquals("Invalid Wibble", xmlParser.findElementAttribute("Error", "message"));
    }

    @Test
    public void XMLParserSetValue() {
        XMLParser xmlParser = new XMLParser(xmlToParse);
        xmlParser.setElementValue("faultcode", "wibble");
        assertEquals("wibble", xmlParser.findElementValue("faultcode"));
    }

    @Test
    public void XMLParserToString() {
        XMLParser xmlParser = new XMLParser(xmlToParse);
        String wibble = xmlParser.toXMLString();
        assertEquals(wibble, xmlToParse);
    }

    @Test
    public void XMLParserTestElementNamespace() {
        XMLParser xmlParser = new XMLParser(template);
        assertEquals("1", xmlParser.findElementValue("bkd:id"));
        assertEquals("12345", xmlParser.findElementValue("bkd:WibbleNo"));
    }

    @Test
    public void XMLParserTestPrint() {
        XMLParser xmlParser = new XMLParser(template);
        xmlParser.printAllElements();
    }

}
