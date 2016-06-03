package AutomationLibrary.Utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HTMLParser {
    private Document doc;

    public HTMLParser(String html) {
        this.doc = Jsoup.parse(html);
    }

    public String getElementValueByID(String id) {
        Element content = doc.getElementById(id);
        return content.val();
    }

    public String getElementIDByValue(String value) {
        Elements content = doc.getElementsByAttribute("value");
        for (int i = 0; i < content.size(); i++) {
            if (content.get(i).val().equals(value)) {
                return content.get(i).id();
            }
        }

        return null;
    }

    public String getElementIDByClassName(String text) {
        Elements content = doc.getElementsByAttributeValue("class", text);
        return content.get(0).id();

    }

    public List<String> getListOfElementsIdsByAttribute(String attribute) {
        Elements content = doc.getElementsByAttribute(attribute);
        List<String> ids = new ArrayList<String>();
        for (int i = 0; i < content.size(); i++) {
            ids.add(content.get(i).id());
        }
        return ids;
    }

    public void writeOutHTML(String path) {
        try {
            FileHandler.writeOutFileAsHTML(doc, path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Document getDoc() {
        return this.doc;
    }


    public void updateHTMLElement(String id, String attribute, String updatedAttribute) {
        doc.getElementById(id).attr(attribute, updatedAttribute);
    }

}
