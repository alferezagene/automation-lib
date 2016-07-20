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


    //pass a list of colon-delimited values
    public static DataTable createDataTableWithHeaders(List<String> listValues) {
        List<List<String>> dataArray = new ArrayList<>();

        int size = listValues.get(0).split(":").length;

        //this is the header row which is intentionally blank.
        List<String> cd0 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            cd0.add("");
        }
        dataArray.add(cd0);

        for (String values : listValues) {
            List<String> row = new ArrayList<>();
            String[] splitValues = values.split(":");
            for (int i = 0; i < size; i++) {
                row.add(splitValues[i]);
            }
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
