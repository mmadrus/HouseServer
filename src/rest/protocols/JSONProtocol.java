package rest.protocols;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JSONProtocol {

    public JSONObject toJson (String jsonString) throws JSONException {

        return new JSONObject(new JSONTokener(jsonString));
    }

    public String fromJson (JSONObject jsonObject) {

        return jsonObject.toString();
    }
}
