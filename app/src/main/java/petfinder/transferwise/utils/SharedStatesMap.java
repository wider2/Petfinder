package petfinder.transferwise.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SharedStatesMap {

    private static SharedStatesMap instance;
    private LinkedHashMap<String, Object> map;
    private LinkedHashMap<String, Long> mapLong;


    private SharedStatesMap() {
        map = new LinkedHashMap<String, Object>();
        mapLong = new LinkedHashMap<String, Long>();
    }

    public static SharedStatesMap getInstance() {
        if (instance == null) {
            instance = new SharedStatesMap();
        }
        return instance;
    }


    public void setKey(String key, Object value) {
        map.put(key, value);
    }
    public String getKey(String key) {
        String name = "";
        if (map.get(key) != null) {
            if (!map.isEmpty()) {
                name = map.get(key).toString();
            }
        }
        return name;
    }


    public void setKeyLong(String key, Long value) {
        mapLong.put(key, value);
    }
    public Long getKeyLong(String key) {
        Long name=0L;
        if (mapLong.get(key) != null) {
            if (!mapLong.isEmpty()) {
                name = mapLong.get(key);
            }
        }
        return name;
    }

}