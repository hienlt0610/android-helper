package R.helper;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by duynk on 1/5/16.
 */
public class BaseEntity implements IEntityIO {

    public interface EntityField {
        public boolean equals(String otherName);
        public String toString();
    }

    protected List<String> fields;
    protected HashMap<String, Object> data = new HashMap<>();

    public BaseEntity(@NonNull List<String> fieldsList) {
        fields = fieldsList;
    }

    public BaseEntity(EntityField... fieldsList) {
        fields = new ArrayList<>();
        for (EntityField e: fieldsList) {
            fields.add(e.toString());
        }
    }

    public HashMap exportToHashMap() {
        return data;
    }

    public JSONObject exportToJsonObject() {
        return new JSONObject(data);
    }

    public void importData(@NonNull HashMap m) {
        for (String field:fields) {
            if (m.containsKey(field)) {
                data.put(field, m.get(field));
            }
        }
    }

    public void importData(@NonNull JSONObject m) {
        try {
            for (String field : fields) {
                if (m.has(field)) {
                    data.put(field, m.get(field));
                }
            }
        }catch (Exception E) {
            E.printStackTrace();
        }
    }

    public void set(String key, Object value) {
        if (fields.contains(key)) {
            data.put(key, value);
        }
    }

    public void set(EntityField e, Object value) {
        String key = e.toString();
        set(key, value);
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

    public String getString(EntityField e, String defaultValue) {
        return getString(e.toString(), defaultValue);
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

    public int getInt(EntityField e, int defaultValue) {
        return getInt(e.toString(), defaultValue);
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

    public long getLong(EntityField e, long defaultValue) {
        return getLong(e.toString(), defaultValue);
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

    public boolean getBoolean(EntityField e, boolean defaultValue) {
        return getBoolean(e.toString(), defaultValue);
    }

    public <T> ArrayList<T> getArray(@NonNull String key) {
        try {
            return (ArrayList<T>) get(key, null);
        }catch (Exception E) {
            return new ArrayList<>();
        }
    }

    public <T> ArrayList<T> getArray(EntityField e) {
        return getArray(e.toString());
    }

    public <T extends IEntityIO> ArrayList<T> convertArrayList(Object obj, Class<T> type) {
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
                        w.importData((HashMap) item);
                        b.add(w);
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
