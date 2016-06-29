package nz.co.thebteam.AutomationLibrary.Utilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSONArrayParser {

    public JSONArray jsonArray;

    public JSONArrayParser(String JSONToParse) {
        jsonArray = (JSONArray) JSONValue.parse(JSONToParse);
    }

    public JSONArrayParser(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public JSONArrayParser getAtIndex(int index) {
        return new JSONArrayParser((JSONArray) jsonArray.get(index));
    }

    public JSONObject getObjectAtIndex(int index) {
        return ((JSONObject) jsonArray.get(index));
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

    public List<String> findAllValues(String nodeName) {
        List<String> values = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObj = (JSONObject) jsonArray.get(i);
            HashMap<String, String> flattenedJSON;

            flattenedJSON = JSONParser.flattenValues(jsonObj);
            if (flattenedJSON.containsKey(nodeName)) {
                values.add(flattenedJSON.get(nodeName));
            }

        }
        return values;
    }
    public List<Object> findAllObjects(String nodeName) {
        List<Object> values = new ArrayList<>();
        List<HashMap<String, Object>> flattenedJSON = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            Object jsonObj =jsonArray.get(i);
            flattenedJSON.addAll(JSONParser.flattenObjects((JSONObject) jsonObj));
        }

        for (HashMap<String, Object> value : flattenedJSON) {
            if (value.containsKey(nodeName)) {
                values.add(value.get(nodeName));
            }
        }
        return values;
    }


    public List<JSONObject> findNodes(String nodeName) {

        List<JSONObject> nodes = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            //if it's an object
            if (jsonArray.get(i).getClass().toString().contains("JSONObject")) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                Object[] valuesCollection = obj.values().toArray();
                Object[] keysCollection = obj.keySet().toArray();
                //for each element (hashmap) of the object
                for (int q = 0; q < keysCollection.length; q++) {
                    //if it's the right node, add it to the list
                    if (keysCollection[q].equals(nodeName)) {
                        //sometimes the nodes have an object, sometimes an array with single object
                        if (valuesCollection[q].getClass().toString().contains("JSONArray")) {
                            JSONArray valueArray = (JSONArray) valuesCollection[q];
                            for (int p = 0; p < valueArray.size(); p++) {
                                nodes.add((JSONObject) valueArray.get(p));
                            }
                        } else {
                            nodes.add((JSONObject) valuesCollection[q]);
                        }
                    } else if (valuesCollection[q].getClass().toString().contains("JSONArray")) {
                        JSONArrayParser j2 = new JSONArrayParser((JSONArray) valuesCollection[q]);
                        //recursively go down the jsonarrays and return nodes
                        nodes.addAll(j2.findNodes(nodeName));
                    }
                }
            }
        }
        return nodes;
    }

}
