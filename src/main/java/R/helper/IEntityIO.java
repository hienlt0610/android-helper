package R.helper;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by duynk on 1/5/16.
 */
public interface IEntityIO {
    HashMap exportToHashMap();
    void importData(HashMap m);
    void importData(JSONObject obj);
    JSONObject exportToJsonObject();
}
