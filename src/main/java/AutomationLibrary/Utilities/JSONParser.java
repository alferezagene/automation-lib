package AutomationLibrary.Utilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.util.HashMap;

public class JSONParser {
    private JSONObject jsonObj;

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
                type =  valuesCollection[i].getClass().toString();
            } catch (Exception e) {
                type = "null";
            }

            if ((!type.equals("class org.json.simple.JSONObject"))||(type.equals("null"))) {
                try {
                    flatList.put(keysCollection[i].toString().trim(),valuesCollection[i].toString().trim());
                } catch (Exception e) {
                }
            } else {
                flatList.putAll(flattenValues((JSONObject) valuesCollection[i]));
            }
        }
        return flatList;

    }

    public void addNode(String parent, JSONObject node) {
        JSONArray jsonHistoryParser = (JSONArray) jsonObj.get(parent);
        jsonHistoryParser.add(0, node);
        jsonObj.remove(parent);
        jsonObj.put(parent,jsonHistoryParser);
    }

    public void writeOutJSONFile(String path){
        try {
            FileHandler.writeOutFile(jsonObj.toJSONString(), path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
