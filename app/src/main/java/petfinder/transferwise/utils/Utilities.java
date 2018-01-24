package petfinder.transferwise.utils;

import org.json.JSONObject;

public class Utilities {

    public static boolean returnBooleanBasedOnJsonObjectName(JSONObject jsonVal, String jsonName) {
        boolean h = false;
        try {
            JSONObject jBreed = jsonVal.getJSONObject(jsonName);
            h = true;
        } catch (Exception e) {
            //e.printStackTrace();
            if (e.toString().contains("org.json.simple.JSONArray cannot be cast to     org.json.simple.JSONObject")) {
                h = false;
            }
        }
        return h;
    }


}