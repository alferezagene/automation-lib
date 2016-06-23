package AutomationLibTests;


import nz.co.thebteam.AutomationLibrary.Utilities.JSONArrayParser;
import nz.co.thebteam.AutomationLibrary.Utilities.JSONParser;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class JSONTest {


    String jsonExample = "{\"widget\": {\n" +
            "    \"debug\": \"on\",\n" +
            "    \"window\": {\n" +
            "        \"title\": \"Sample Konfabulator Widget\",\n" +
            "        \"name\": \"main_window\",\n" +
            "        \"width\": 500,\n" +
            "        \"height\": 500\n" +
            "    },\n" +
            "    \"image\": { \n" +
            "        \"src\": \"Images/Sun.png\",\n" +
            "        \"name\": \"sun1\",\n" +
            "        \"hOffset\": 250,\n" +
            "        \"vOffset\": 250,\n" +
            "        \"alignment\": \"center\"\n" +
            "    },\n" +
            "    \"text\": {\n" +
            "        \"data\": \"Click Here\",\n" +
            "        \"size\": 36,\n" +
            "        \"style\": \"bold\",\n" +
            "        \"name\": \"text1\",\n" +
            "        \"hOffset\": 250,\n" +
            "        \"vOffset\": 100,\n" +
            "        \"alignment\": \"center\",\n" +
            "        \"onMouseUp\": \"sun1.opacity = (sun1.opacity / 100) * 90;\"\n" +
            "    }\n" +
            "}}  ";

    String jsonExample2 = "[\n" +
            " {\n" +
            "  \"id\"   : 1,\n" +
            "  \"name\" : {\n" +
            "    \"first\" : \"Yong\",\n" +
            "    \"last\" : \"Foobar\" \n" +
            "  },\n" +
            "  \"contact\" : [\n" +
            "    { \"type\" : \"phone/home\", \"ref\" : \"111-111-1234\"},\n" +
            "    { \"type\" : \"phone/work\", \"ref\" : \"222-222-2222\"}\n" +
            "  ]\n" +
            "},\n" +
            "{\n" +
            "  \"id\"   : 2,\n" +
            "  \"name\" : {\n" +
            "    \"first\" : \"Yong\",\n" +
            "    \"last\" : \"Wibble\" \n" +
            "  },\n" +
            "  \"contact\" : [\n" +
            "    { \"type\" : \"phone/home\", \"ref\" : \"333-333-1234\"},\n" +
            "    { \"type\" : \"phone/work\", \"ref\" : \"444-444-4444\"}\n" +
            "  ]\n" +
            "}\n" +
            "]";

    @Test
    public void simpleJsonTest() {
        JSONParser jp = new JSONParser(jsonExample);
        assertEquals(jp.getChildNode("widget").getChildNode("text").getValue("data"), "Click Here");
    }


    @Test
    public void jsonTestWithNodes() {
        JSONArrayParser jp = new JSONArrayParser(jsonExample2);
        assertEquals("Foobar", jp.findNodes("name").get(0).get("last"));
        assertEquals("Wibble", jp.findNodes("name").get(1).get("last"));
    }


}
