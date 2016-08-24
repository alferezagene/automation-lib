package nz.co.thebteam.AutomationLibrary.Utilities;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipHelper {

    InputStream zippedFile;

    public ZipHelper(InputStream zippedFile) {
        this.zippedFile = zippedFile;
    }

    public List<String> getFilesAsStrings() {
        List<String> files = new ArrayList<>();
        ZipInputStream zis = new ZipInputStream(zippedFile);

        ZipEntry entry;
        try {
            while ((entry = zis.getNextEntry()) != null) {
                String file = "";
                System.out.println("entry: " + entry.getName() + ", " + entry.getSize());
                // consume all the data from this entry

                while (zis.available() > 0) {
                    file += IOUtils.toString(zis);
                    zis.read();
                    file += IOUtils.toString(zis);
                    files.add(file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

}
