package AutomationLibrary.Utilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.HashMap;

public class JSONArrayParser {

    private JSONArray jsonArray;

    public JSONArrayParser(String JSONToParse) {
        jsonArray = (JSONArray) JSONValue.parse(JSONToParse);
    }

    public JSONArrayParser(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public JSONArrayParser getAtIndex(int index) {
        return new JSONArrayParser((JSONArray) jsonArray.get(index));
    }

    public JSONParser getObjectAtIndex(int index) {
        return new JSONParser((JSONObject) jsonArray.get(index));
    }

    public int size() {
        try {
            return jsonArray.size();
        } catch (Exception e) {
            return 0;
        }
    }
    public String findValueAnyLevel(String nodeName) {

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObj = (JSONObject) jsonArray.get(i);
            HashMap<String, String> flattenedJSON;

            flattenedJSON = JSONParser.flattenValues(jsonObj);
            if (flattenedJSON.containsKey(nodeName)) {
                return flattenedJSON.get(nodeName);
            }
            return null;
        }
        return null;
    }
}
