package R.helper;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by duynk on 1/5/16.
 */
public abstract class BaseEntity {

    static class EntityType {
        Class entityType;
        Class<?> collectionType;
        boolean isArray = false;
        boolean isDerivedFromEntity;
        EntityType(Class entityType, Class collectionType) {
            this.entityType = entityType;
            this.collectionType = collectionType;
            isArray = true;
            isDerivedFromEntity = BaseEntity.class.isAssignableFrom(entityType);
        }
        EntityType(Class entityType) {
            this.entityType = entityType;
            this.collectionType = void.class;
        }
        public Class getEntityType() {
            return entityType;
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

                    fields.put(field_name, new EntityType(annotation.type()));
                }

                final EntityArrayField arrayFieldAno = field.getAnnotation(EntityArrayField.class);
                if (arrayFieldAno != null) {
                    String field_name = arrayFieldAno.value();
                    field.setAccessible(true);
                    field.set(clzz, field_name);
                    fields.put(field_name, new EntityType(arrayFieldAno.type(), List.class));
                }
            }
        }catch (Exception E) {
            E.printStackTrace();
        }
    }
    protected HashMap<String, EntityType>fields;
    protected HashMap<String, Object> data = new HashMap<>();

    protected BaseEntity() {
        bind(this);
    }

    public JSONObject exportToJsonObject() throws Exception{
        JSONObject jsonObject = new JSONObject();
        for (String field : fields.keySet()) {
            EntityType t = fields.get(field);
            if (t.isArray) {
                List a = (List)data.get(field);
                if (a != null) {
                    JSONArray array = new JSONArray();
                    if (t.isDerivedFromEntity) {
                        for (Object obj : a) {
                            BaseEntity entity = (BaseEntity) obj;
                            array.put(entity.exportToJsonObject());
                        }
                    } else {
                        for (Object obj : a) {
                            array.put(obj);
                        }
                    }
                    jsonObject.put(field, array);
                }

            } else {
                if (t.isDerivedFromEntity) {
                    BaseEntity entity = (BaseEntity) data.get(field);
                    jsonObject.put(field, entity.exportToJsonObject());
                } else {
                    Object d = data.get(field);
                    jsonObject.put(field, d);
                }
            }
        }
        return jsonObject;
    }

    public void importData(@NonNull JSONObject m) throws Exception{
        data.clear();
        for (String field : fields.keySet()) {
            EntityType t = fields.get(field);
            if (t.isArray) {
                ArrayList a = new ArrayList();
                JSONArray array = new JSONArray();
                if (!m.isNull(field)) {
                    try {
                        array = m.getJSONArray(field);
                    } catch (Exception E) {
                        E.printStackTrace();
                    }
                    if (t.isDerivedFromEntity) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObj = array.getJSONObject(i);
                            if (jsonObj != null) {
                                a.add(castObject(jsonObj, t.getEntityType()));
                            }
                        }
                    } else {
                        for (int i = 0; i < array.length(); i++) {
                            a.add(array.get(i));
                        }
                    }
                    data.put(field, a);
                }
            } else {
                if (t.isDerivedFromEntity) {
                    JSONObject jsonObj = m.getJSONObject(field);
                    if (jsonObj != null) {
                        data.put(field, castObject(jsonObj, t.getEntityType()));
                    }
                } else {
                    data.put(field, m.get(field));
                }
            }
        }
    }

    public void set(String key, Object value) {
        if (fields.containsKey(key)) {
            data.put(key, value);
        }
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

    public <T> List<T> getArray(@NonNull String key) {
        try {
            return (List<T>) get(key, null);
        }catch (Exception E) {
            return new ArrayList<>();
        }
    }

    private <T extends BaseEntity> T castObject(JSONObject obj, Class<T> type) throws Exception {
        T w = type.newInstance();
        w.importData(obj);
        return w;
    }

    @Override
    public String toString() {
        try {
            return this.exportToJsonObject().toString();
        } catch (Exception E) {
            E.printStackTrace();
            return E.toString();
        }
    }
}
