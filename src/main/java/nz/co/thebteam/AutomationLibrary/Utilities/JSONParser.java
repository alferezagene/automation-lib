package nz.co.thebteam.AutomationLibrary.Utilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSONParser {
    public JSONObject jsonObj;

    public JSONParser(String JSONToParse) {
        jsonObj = (JSONObject) JSONValue.parse(JSONToParse);
    }

    public JSONParser(JSONObject jsonObject) {
        jsonObj = jsonObject;
    }

    public String getValue(String value) {
        return jsonObj.get(value).toString();
    }

    public JSONParser getChildNode(String childName) {
        return new JSONParser((JSONObject) jsonObj.get(childName));
    }

    public JSONArrayParser getChildJSONArray(String childName) {
        return new JSONArrayParser((JSONArray) jsonObj.get(childName));
    }

    public String findValueAnyLevel(String nodeName) {
        HashMap<String, String> flattenedJSON;

        flattenedJSON = flattenValues(jsonObj);
        if (flattenedJSON.containsKey(nodeName)) {
            return flattenedJSON.get(nodeName);
        }
        return null;
    }


    public static HashMap<String, String> flattenValues(JSONObject values) {
        HashMap<String, String> flatList = new HashMap<String, String>();

        Object[] valuesCollection = values.values().toArray();
        Object[] keysCollection = values.keySet().toArray();
        for (int i = 0; i < valuesCollection.length; i++) {
            String type;
            try {
                type = valuesCollection[i].getClass().toString();
            } catch (Exception e) {
                type = "null";
            }
            if (type.equals("class org.json.simple.JSONArray")) { //array
                JSONArrayParser jsonArray = new JSONArrayParser((JSONArray) valuesCollection[i]);
                for (int q = 0; q < jsonArray.size(); q++) {
                    flatList.putAll(flattenValues(jsonArray.getObjectAtIndex(q)));
                }

            } else if (type.equals("class org.json.simple.JSONObject")) { //object
                flatList.putAll(flattenValues((JSONObject) valuesCollection[i]));
            } else { //flat value
                flatList.put(keysCollection[i].toString().trim(), valuesCollection[i].toString().trim());
            }
        }
        return flatList;

    }

    public static List<HashMap<String, Object>> flattenObjects(JSONObject values) {
        List<HashMap<String, Object>> flatlist = new ArrayList<>();
        Object[] valuesCollection = values.values().toArray();
        Object[] keysCollection = values.keySet().toArray();
        for (int i = 0; i < valuesCollection.length; i++) {
            String type;
            try {
                type = valuesCollection[i].getClass().toString();
            } catch (Exception e) {
                type = "null";
            }
            if (type.equals("class org.json.simple.JSONArray")) {  //if it's an array - recursion baby!
                JSONArray jsonArray = (JSONArray) valuesCollection[i];
                flatlist.addAll(flattenObjects((JSONObject) jsonArray.get(0)));
            }
            if (type.equals("class org.json.simple.JSONObject")) { //if it's an object
                flatlist.addAll(flattenObjects((JSONObject) valuesCollection[i]));
            } else { //everything else, with original type preserved
                HashMap<String, Object> jsonObjectData = new HashMap<>();
                jsonObjectData.put(keysCollection[i].toString(), valuesCollection[i]);
                flatlist.add(jsonObjectData);
            }
        }
        return flatlist;

    }

    public void addNode(String parent, JSONObject node) {
        JSONArray jsonHistoryParser = (JSONArray) jsonObj.get(parent);
        jsonHistoryParser.add(0, node);
        jsonObj.remove(parent);
        jsonObj.put(parent, jsonHistoryParser);
    }

    public void writeOutJSONFile(String path) {
        try {
            FileHandler.writeOutFile(jsonObj.toJSONString(), path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
