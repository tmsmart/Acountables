package group.g203.countables.base.manager;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import group.g203.countables.model.Countable;
import group.g203.countables.model.DateField;
import group.g203.countables.model.serializer.CountableSerializer;
import group.g203.countables.model.serializer.DateFieldSerializer;
import group.g203.countables.model.serializer.DateTimeSerializer;
import io.realm.RealmObject;

public class GsonManager {

    static Gson getGson() {
        try {
            return new GsonBuilder()
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getDeclaringClass().equals(RealmObject.class);
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    })
                    .registerTypeAdapter(Class.forName("io.realm.CountableRealmProxy"), new CountableSerializer())
                    .registerTypeAdapter(Class.forName("io.realm.DateFieldRealmProxy"), new DateFieldSerializer())
                    .registerTypeAdapter(DateTime.class, new DateTimeSerializer())
                    .registerTypeAdapter(Countable.class, new CountableSerializer())
                    .registerTypeAdapter(DateField.class, new DateFieldSerializer())
                    .create();
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static String toJson(Countable countable) {
        return getGson().toJson(countable);
    }

    public static Countable fromJson(String json) {
        return getGson().fromJson(json, Countable.class);
    }
}
