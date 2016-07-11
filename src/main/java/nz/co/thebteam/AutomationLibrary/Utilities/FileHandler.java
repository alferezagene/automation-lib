package nz.co.thebteam.AutomationLibrary.Utilities;


import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

public class FileHandler {
    public static void writeOutFileAsHTML(Document resource, String fileName) throws IOException {
        File file = new File(fileName);
        FileUtils.writeStringToFile(file, resource.outerHtml(), "UTF-8");
    }

    public static void writeOutFile(String resource, String fileName) throws IOException {
        File file = new File(fileName);
        FileUtils.writeStringToFile(file, resource, "UTF-8");
    }

    public static String readFileAsString(String fileName) {
        File file = new File(fileName);
        try {
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
