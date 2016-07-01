package nz.co.thebteam.AutomationLibrary;

import cucumber.api.DataTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HelperMethods {

    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static DataTable createDataTableWithHeaders(List<String> values) {
        List<List<String>> dataArray = new ArrayList<>();
        //this is the header row which is intentionally blank.
        List<String> cd0 = new ArrayList<>();
        cd0.add("");
        cd0.add("");
        dataArray.add(cd0);

        for (String pair : values) {
            List<String> row = new ArrayList<>();
            String[] pairValues = pair.split(":");
            row.add(pairValues[0]);
            row.add(pairValues[1]);
            dataArray.add(row);
        }
        return DataTable.create(dataArray);

    }

    public static Map<String, String> createMap(List<String> values) {
        Map<String, String> dataArray = new HashMap<>();
        for (String pair : values) {
            String[] pairValues = pair.split(":");
            dataArray.put(pairValues[0], pairValues[1]);
        }
        return dataArray;
    }
}
