package R.helper;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by duynk on 1/5/16.
 */
public class BaseEntity implements IExportable {

    protected List<String> fields;
    protected HashMap<String, Object> data = new HashMap<>();

    public BaseEntity(@NonNull List<String> fieldsList) {
        fields = fieldsList;
    }

    public HashMap exportToHashMap() {
        return data;
    }

    public JSONObject exportToJsonObject() {
        return new JSONObject(data);
    }

    public void importFromHashMap(@NonNull HashMap m) {
        for (String field:fields) {
            if (m.containsKey(field)) {
                data.put(field, m.get(field));
            }
        }
    }

    public void set(String key, Object value) {
        if (fields.contains(key)) {
            data.put(key, value);
        }
    }

    public Object get(@NonNull String key, Object defaultValue) {
        if (fields.contains(key)) {
            if (data.containsKey(key)) {
                Object obj = data.get(key);
                return obj == null ? defaultValue : obj;
            } else {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public String getString(@NonNull String key, String defaultValue) {
        return (String) get(key, defaultValue);
    }

    public int getInt(@NonNull String key, int defaultValue) {
        Object obj = get(key, null);
        if (obj == null) {
            return defaultValue;
        } else {
            try {
                return (Integer)obj;
            } catch (Exception E) {
                return defaultValue;
            }
        }
    }

    public long getLong(@NonNull String key, long defaultValue) {
        Object obj = get(key, null);
        if (obj == null) {
            return defaultValue;
        } else {
            try {
                return (Long)obj;
            } catch (Exception E) {
                return defaultValue;
            }
        }
    }

    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        Object obj = get(key, null);
        if (obj == null) {
            return defaultValue;
        } else {
            try {
                return (Boolean)obj;
            } catch (Exception E) {
                return defaultValue;
            }
        }
    }

    public <T> ArrayList<T> getArray(@NonNull String key) {
        try {
            return (ArrayList<T>) get(key, null);
        }catch (Exception E) {
            return new ArrayList<>();
        }
    }

    public <T extends IExportable> ArrayList<T> convertArrayList(Object obj, Class<T> type) {
        if (obj instanceof ArrayList) {
            try {
                ArrayList a = (ArrayList)obj;
                ArrayList<T> b = new ArrayList<>();
                for (int i = 0; i < a.size(); i++) {
                    Object item = a.get(i);
                    if (type.isInstance(item)) {
                        b.add((T) item);
                    } else if (item instanceof HashMap) {
                        T w = type.newInstance();
                        w.importFromHashMap((HashMap) item);
                    }
                }
                return b;
            } catch (Exception E) {
                return null;
            }
        } else {
            return null;
        }
    }
}
