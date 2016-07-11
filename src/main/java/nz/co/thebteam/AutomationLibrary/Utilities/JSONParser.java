package nz.co.thebteam.AutomationLibrary.Utilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONParser {
    public JSONObject jsonObj = null;
    public JSONArray jsonArray = null;

    public JSONParser(String JSONToParse) {
        try {
            jsonObj = (JSONObject) JSONValue.parse(JSONToParse); //it's an object
        } catch (Exception e) {
            jsonArray = (JSONArray) JSONValue.parse(JSONToParse); //it's an array
        }
    }

    public JSONParser(JSONObject jsonObject) {
        this.jsonObj = jsonObject;
    }

    public JSONParser(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String getValue(String value) {
        if (isObject()) {
            return jsonObj.get(value).toString();
        } else {
            return null;
        }
    }

    public JSONParser getChildJSONObject(String childName) {
        if (isObject()) {
            return new JSONParser((JSONObject) jsonObj.get(childName));
        } else {
            return null;
        }
    }

    public JSONParser getChildJSONArray(String childName) {
        if (isObject()) {
            return new JSONParser((JSONArray) jsonObj.get(childName));
        } else {
            return null;
        }
    }

    private String findValueAnyLevelUniversal(String nodeName) {
        List<HashMap<String, String>> flattenedJSON;
        flattenedJSON = flattenValues(jsonObj);
        for (Map<String, String> keyValuePair : flattenedJSON) {
            if (keyValuePair.containsKey(nodeName)) {
                return keyValuePair.get(nodeName);
            }
        }
        return null;
    }

    public String findValueAnyLevel(String nodeName) {
        if (this.jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                this.jsonObj = (JSONObject) jsonArray.get(i);
                return findValueAnyLevelUniversal(nodeName);
            }
        } else {
            return findValueAnyLevelUniversal(nodeName);
        }
        return null;
    }


    public static List<HashMap<String, String>> flattenValues(JSONObject values) {
        List<HashMap<String, String>> flatList = new ArrayList<>();

        Object[] valuesCollection = values.values().toArray();
        Object[] keysCollection = values.keySet().toArray();
        for (int i = 0; i < valuesCollection.length; i++) {
            HashMap<String, String> valuePair = new HashMap<>();
            String type;
            try {
                type = valuesCollection[i].getClass().toString();
            } catch (Exception e) {
                type = "null";
            }
            if (type.equals("class org.json.simple.JSONArray")) { //array
                JSONArray ja = (JSONArray) valuesCollection[i];
                if (ja.size() > 0) { //sometimes there are empty arrays
                    if (ja.get(0).getClass().toString().equals("class java.lang.String")) {
                        //it's an array of strings, badly formattted JSON
                        valuePair.put(keysCollection[i].toString().trim(), valuesCollection[i].toString().trim());
                        flatList.add(valuePair);
                    } else {
                        flatList.addAll(flattenValues((JSONArray) valuesCollection[i]));
                    }
                }
            } else if (type.equals("class org.json.simple.JSONObject")) { //object
                flatList.addAll(flattenValues((JSONObject) valuesCollection[i]));
            } else { //flat value
                valuePair.put(keysCollection[i].toString().trim(), valuesCollection[i].toString().trim());
                flatList.add(valuePair);
            }
        }
        return flatList;
    }

    public static List<HashMap<String, String>> flattenValues(JSONArray jsonArray) {
        List<HashMap<String, String>> flatList = new ArrayList<>();
        for (int q = 0; q < jsonArray.size(); q++) {
            flatList.addAll(flattenValues((JSONObject) jsonArray.get(q)));
        }
        return flatList;
    }


    public List<JSONObject> findNodes(String nodeName) {
        List<JSONObject> nodes = new ArrayList<>();

        if (jsonArray != null) { //if we start with an array
            for (int i = 0; i < jsonArray.size(); i++) {
                //if it's an object
                if (jsonArray.get(i).getClass().toString().contains("JSONObject")) {
                    JSONParser jObj = new JSONParser((JSONObject) jsonArray.get(i));
                    nodes.addAll(jObj.findNodesObject(nodeName));
                }
            }
        } else {//we start with an object
            nodes.addAll(findNodesObject(nodeName));
        }
        return nodes;
    }

    private List<JSONObject> findNodesObject(String nodeName) {
        List<JSONObject> nodes = new ArrayList<>();

        Object[] valuesCollection = jsonObj.values().toArray();
        Object[] keysCollection = jsonObj.keySet().toArray();
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

                } else if (valuesCollection[q].getClass().toString().contains("JSONObject")) {
                    nodes.add((JSONObject) valuesCollection[q]);
                }
            } else if (valuesCollection[q].getClass().toString().contains("JSONArray")) {
                JSONParser j2 = new JSONParser((JSONArray) valuesCollection[q]);
                //recursively go down the jsonarrays and return nodes
                nodes.addAll(j2.findNodes(nodeName));
            }
        }
        return nodes;

    }

    private static List<HashMap<String, Object>> flattenObjects(JSONObject values) {
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


    public void writeOutJSONFile(String path) {
        try {
            FileHandler.writeOutFile(jsonObj.toJSONString(), path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public JSONParser getAtIndex(int index) {
        if (!isObject()) {
            return new JSONParser((JSONObject) jsonArray.get(index));
        } else {
            return null;
        }
    }


    public JSONObject getObjectAtIndex(int index) {
        if (!isObject()) {
            return ((JSONObject) jsonArray.get(index));
        } else {
            return null;
        }
    }

    public int size() {
        try {
            return jsonArray.size();
        } catch (Exception e) {
            return 0;
        }
    }

    //Retrieves a list of the values matching the nodename
//BROKEN
    public List<String> findAllValues(String nodeName) {
        List<String> values = new ArrayList<>();
        List<HashMap<String, String>> flattenedJSON;

        if (jsonArray != null) { //if we're starting wtih an array
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                flattenedJSON = JSONParser.flattenValues(jsonObj);
                for (Map<String, String> keyValuePair : flattenedJSON)
                    if (keyValuePair.containsKey(nodeName)) {
                        values.add(keyValuePair.get(nodeName));
                    }
            }
        } else { //starting with an object
            flattenedJSON = JSONParser.flattenValues(this.jsonObj);
            for (Map<String, String> keyValuePair : flattenedJSON)
                if (keyValuePair.containsKey(nodeName)) {
                    values.add(keyValuePair.get(nodeName));
                }
        }
        return values;
    }


    //Retrieves a list of the objects matching the nodename
    public List<Object> findAllObjects(String nodeName) {
        List<Object> values = new ArrayList<>();
        List<HashMap<String, Object>> flattenedJSON = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            Object jsonObj = jsonArray.get(i);
            flattenedJSON.addAll(flattenObjects((JSONObject) jsonObj));
        }

        for (HashMap<String, Object> value : flattenedJSON) {
            if (value.containsKey(nodeName)) {
                values.add(value.get(nodeName));
            }
        }
        return values;
    }

    public static boolean isObject(Object objectToTest) {
        try {
            JSONObject jsonObject = (JSONObject) objectToTest;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isObject() {
        if (this.jsonObj != null) {
            return true;
        } else {
            return false;
        }
    }


}
