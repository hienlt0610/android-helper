package R.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snappydb.DB;
import com.snappydb.DBFactory;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by duynk on 1/4/16.
 */
public class LocalStorage {
    static LocalStorage sInstance = new LocalStorage();
    Context ctx;
    DB db;

    public static void init(Context ctx) {
        try {
            sInstance.db = DBFactory.open(ctx);
            sInstance.ctx = ctx;
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public static void close() {
        try {
            sInstance.db.close();
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public static void set(@NonNull String key, int value) {
        try {
            sInstance.db.putInt(key, value);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public static void set(@NonNull String key, long value) {
        try {
            sInstance.db.putLong(key, value);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public static void set(@NonNull String key, String value) {
        try {
            sInstance.db.put(key, value);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public static void set(@NonNull String key, boolean b) {
        try {
            sInstance.db.putBoolean(key, b);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public static void set(@NonNull String key, JSONObject json) {
        try {
            if (json != null) {
                sInstance.db.put(key, json.toString());
            } else {
                sInstance.db.del(key);
            }
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public static void set(@NonNull String key, HashMap map) {
        try {
            if (map != null) {
                JSONObject obj = new JSONObject(map);
                set(key, obj);
            } else {
                sInstance.db.del(key);
            }
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public static void set(@NonNull String key, EntityX obj) throws Exception{
        if (obj != null) {
            JSONObject json = obj.exportToJson();
            set(key, json);
        } else {
            sInstance.db.del(key);
        }
    }

    public static void set(int key, boolean b) {
        set(sInstance.ctx.getString(key), b);
    }

    public static void set(int key, int value) {
        set(sInstance.ctx.getString(key), value);
    }

    public static void set(int key, long value) {
        set(sInstance.ctx.getString(key), value);
    }

    public static void set(int key, String value) {
        set(sInstance.ctx.getString(key), value);
    }

    public static void set(int key, JSONObject value) {
        set(sInstance.ctx.getString(key), value);
    }

    public static void set(int key, HashMap value) {
        set(sInstance.ctx.getString(key), value);
    }

    public static void set(int key, EntityX obj) throws Exception{
        set(sInstance.ctx.getString(key), obj.exportToJson());
    }

    public static int getInt(@NonNull String key, int defaultValue) {
        try {
            return sInstance.db.getInt(key);
        } catch (Exception E) {
            return defaultValue;
        }
    }

    public static long getLong(@NonNull String key, long defaultValue) {
        try {
            return sInstance.db.getLong(key);
        } catch (Exception E) {
            return defaultValue;
        }
    }

    public static String getString(@NonNull String key, String defaultValue) {
        try {
            return sInstance.db.get(key);
        } catch (Exception E) {
            return defaultValue;
        }
    }

    public static boolean getBool(@NonNull String key, boolean defaultValue) {
        try {
            return sInstance.db.getBoolean(key);
        } catch (Exception E) {
            return defaultValue;
        }
    }

    @Nullable
    public static JSONObject getJSON(@NonNull String key) {
        try {
            String str = sInstance.db.get(key);
            return new JSONObject(str);
        } catch (Exception E) {
            return null;
        }
    }

    @Nullable
    public static HashMap getHashMap(@NonNull String key) {
        String s = getString(key, null);
        if (s != null) {
            try {
                return new ObjectMapper().readValue(s, HashMap.class);
            } catch (Exception E) {
                return null;
            }
        } else {
            return null;
        }
    };

    public static boolean loadEntityX(@NonNull String key, @NonNull EntityX obj) throws Exception {
        JSONObject m = getJSON(key);
        if (m != null) {
            obj.importFromJson(m);
            return true;
        } else {
            return false;
        }
    }

    public static int getInt(int key, int defaultValue) {
        return getInt(sInstance.ctx.getString(key), defaultValue);
    }

    public static long getLong(int key, long defaultValue) {
        return getLong(sInstance.ctx.getString(key), defaultValue);
    }

    public static String getString(int key, String defaultValue) {
        return getString(sInstance.ctx.getString(key), defaultValue);
    }

    public static boolean getBool(int key, boolean defaultValue) {
        return getBool(sInstance.ctx.getString(key), defaultValue);
    }

    public static JSONObject getJSON(int key) {
        return getJSON(sInstance.ctx.getString(key));
    }

    public static HashMap getHashMap(int key) {
        return getHashMap(sInstance.ctx.getString(key));
    }

    public static boolean loadEntityX(int key, @NonNull EntityX obj) throws Exception{
        return loadEntityX(sInstance.ctx.getString(key), obj);
    }

    public static void remove(String key) {
        try {
            sInstance.db.del(key);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public static void remove(int key) {
        remove(sInstance.ctx.getString(key));
    }

    @Nullable
    public static String[] findKeys(String filter) {
        try {
            return sInstance.db.findKeys(filter);
        } catch (Exception E) {
            return null;
        }
    }

    @Nullable
    public static String[] findKeys(String from, String to) {
        try {
            return sInstance.db.findKeysBetween(from, to);
        } catch (Exception E) {
            return null;
        }
    }
}
