package rest.protocols;

import org.json.JSONObject;
import org.json.JSONTokener;

public class JSONProtocol {

    private static JSONProtocol jsonProtocol = null;

    private JSONProtocol () {}

    public static JSONProtocol getInstance () {

        if (jsonProtocol == null) {

            jsonProtocol = new JSONProtocol();
        }

        return jsonProtocol;
    }

    public JSONObject toJson (String jsonString) {

        return new JSONObject(new JSONTokener(jsonString));
    }

    public String fromJson (JSONObject jsonObject) {

        return jsonObject.toString();
    }
}
