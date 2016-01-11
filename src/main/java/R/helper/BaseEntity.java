package R.helper;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by duynk on 1/5/16.
 */
public class BaseEntity implements IEntityIO {

    class EntityType {
        Class entityType;
        Class<?> collectionType;
        boolean isArray = false;
        boolean isDerivedFromBaseEntity;
        EntityType(Class entityType, Class collectionType) {
            this.entityType = entityType;
            this.collectionType = collectionType;
            isArray = true;
            isDerivedFromBaseEntity = BaseEntity.class.isAssignableFrom(entityType);
        }
        EntityType(Class entityType) {
            this.entityType = entityType;
            this.collectionType = void.class;
        }
    }


    void bind(BaseEntity clzz) {
        try {
            fields = new HashMap<>();
            for (Field field : clzz.getClass().getDeclaredFields()) {
                Class type = field.getType();
                String name = field.getName();
                final EntityField annotation = field.getAnnotation(EntityField.class);
                if (annotation != null) {
                    String field_name = annotation.value();
                    field.setAccessible(true);
                    field.set(clzz, field_name);

                    if (annotation.collectionType() == void.class) {
                        fields.put(field_name, new EntityType(annotation.type()));
                    } else {
                        fields.put(field_name, new EntityType(annotation.type(), annotation.collectionType()));
                    }
                }
            }
        }catch (Exception E) {
            E.printStackTrace();
        }
    }
    protected HashMap<String, EntityType>fields;
    protected HashMap<String, Object> data = new HashMap<>();

    public BaseEntity() {
        bind(this);
    }

    public HashMap exportToHashMap() {
        return data;
    }

    public JSONObject exportToJsonObject() {
        JSONObject jsonObject = new JSONObject(data);

        try {
            for (String field : fields.keySet()) {
                EntityType t = fields.get(field);
                if (t.isArray) {
                    JSONArray array = new JSONArray();
                    ArrayList a = (ArrayList)data.get(field);
                    for (Object obj: a) {
                        JSONObject jsonObject2 = new JSONObject(data);
                        if (t.isDerivedFromBaseEntity) {
                            BaseEntity entity = (BaseEntity)obj;
                            jsonObject2.put(field, entity.exportToJsonObject());
                        } else {
                            jsonObject2.put(field, data.get(field));
                        }
                        array.put(jsonObject2);
                    }
                    jsonObject.put(field, array);
                } else {
                    if (t.isDerivedFromBaseEntity) {
                        BaseEntity entity = (BaseEntity) data.get(field);
                        jsonObject.put(field, entity.exportToJsonObject());
                    } else {
                        jsonObject.put(field, data.get(field));
                    }
                }
            }
        }catch (Exception E) {
            E.printStackTrace();
        }
        return jsonObject;
    }

    public void importData(@NonNull HashMap m) {
        for (String field:fields.keySet()) {
            if (m.containsKey(field)) {
                EntityType t = fields.get(field);
                if (t.isArray) {
                    if (t.isDerivedFromBaseEntity) {
                        data.put(field, castArrayList(m.get(field), t.entityType));
                    } else {
                        //TODO: handle array of string
                    }
                } else {
                    if (!t.isDerivedFromBaseEntity) {
                        data.put(field, m.get(field));
                    } else {
                        data.put(field, castObject(m.get(field), t.entityType));
                    }
                }
            }
        }
    }

    public void importData(@NonNull JSONObject m) {
        try {
            for (String field : fields.keySet()) {
                if (m.has(field)) {
                    data.put(field, m.get(field));
                }
            }
        }catch (Exception E) {
            E.printStackTrace();
        }
    }

    public void set(String key, Object value) {
        if (fields.containsKey(key)) {
            data.put(key, value);
        }
    }

    public void set(EntityField e, Object value) {
        String key = e.toString();
        set(key, value);
    }

    public Object get(@NonNull String key, Object defaultValue) {
        if (fields.containsKey(key)) {
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

    public <T extends IEntityIO> ArrayList<T> castArrayList(Object obj, Class<T> type) {
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

    public <T extends IEntityIO> T castObject(Object obj, Class<T> type) {
        try {
            if (type.isInstance(obj)) {
                return (T) obj;
            } else if (obj instanceof HashMap) {
                T w = type.newInstance();
                w.importData((HashMap) obj);
                return w;
            }
        } catch (Exception E) {
            return null;
        }
        return null;
    }
}
