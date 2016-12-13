package group.g203.countables.model.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;

import java.lang.reflect.Type;

import group.g203.countables.base.Constants;
import group.g203.countables.model.DateField;

public class DateFieldSerializer implements JsonSerializer<DateField>, JsonDeserializer<DateField> {

    @Override
    public JsonElement serialize(DateField src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.add(Constants.DATEFIELD_DATE, context.serialize(new DateTime(src.date)));
        return jsonObject;
    }

    @Override
    public DateField deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        return context.deserialize(jsonObject.get(Constants.DATEFIELD_DATE).getAsJsonObject(), DateField.class);
    }
}
