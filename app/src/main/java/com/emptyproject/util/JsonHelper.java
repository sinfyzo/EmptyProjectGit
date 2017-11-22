package com.emptyproject.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by INFYZO\shailesh.pateliya on 23/8/16.
 */
public class JsonHelper {
    public static Object toJSON(Object object) throws Exception {
        if (object instanceof Map) {
            JSONObject json = new JSONObject();
            Map map = (Map) object;
            for (Object key : map.keySet()) {
                json.put(key.toString(), toJSON(map.get(key)));
            }
            return json;
        } else if (object instanceof Iterable) {
            JSONArray json = new JSONArray();
            for (Object value : ((Iterable)object)) {
                json.put(value);
            }
            return json;
        } else {
            return object;
        }
    }

    public static boolean isEmptyObject(JSONObject object) {
        return object.names() == null;
    }

    public static Map<String, Object> getMap(JSONObject object, String key) throws Exception {
        return toMap(object.getJSONObject(key));
    }

    public static Map<String, Object> toMap(JSONObject object) throws Exception {
        Map<String, Object> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(object.get(key)));
        }
        return map;
    }

    public static List toList(JSONArray array) throws Exception {
        List list = new ArrayList();
        for (int i = 0; i < array.length(); i++) {
            list.add(fromJson(array.get(i)));
        }
        return list;
    }

    private static Object fromJson(Object json) throws Exception {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return toMap((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        } else {
            return json;
        }
    }
}
