package group.g203.countables.model.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import group.g203.countables.base.Constants;
import group.g203.countables.base.utils.CollectionUtils;
import group.g203.countables.model.Countable;

public class CountableSerializer implements JsonSerializer<Countable>, JsonDeserializer<Countable> {
    @Override
    public JsonElement serialize(Countable src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constants.COUNTABLE_NAME, src.name);
        jsonObject.addProperty(Constants.COUNTABLE_ID, src.id);
        jsonObject.addProperty(Constants.COUNTABLE_INDEX, src.index);
        jsonObject.addProperty(Constants.TIMES_COMPLETED, src.timesCompleted);
        jsonObject.addProperty(Constants.IS_ACCOUNTABLE, src.isAccountable);
        jsonObject.addProperty(Constants.IS_REMINDER, src.isReminderEnabled);
        jsonObject.addProperty(Constants.DAY_REPEATER, src.dayRepeater);
        jsonObject.add(Constants.ACCOUNTABLE_DATES, createDatesJsonArray(context, src.accountableDates));
        jsonObject.add(Constants.LOGGED_DATES, createDatesJsonArray(context, src.loggedDates));
        jsonObject.add(Constants.ANCHOR_DATES, createDatesJsonArray(context, src.anchorDates));
        jsonObject.add(Constants.LAST_MODIFIED, context.serialize(new DateTime(src.lastModified)));
        return jsonObject;
    }

    @Override
    public Countable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        Countable countable = new Countable();

        countable.name = jsonObject.get(Constants.COUNTABLE_NAME).getAsString();
        countable.id = jsonObject.get(Constants.COUNTABLE_ID).getAsInt();
        countable.index = jsonObject.get(Constants.COUNTABLE_INDEX).getAsInt();
        countable.timesCompleted = jsonObject.get(Constants.TIMES_COMPLETED).getAsInt();
        countable.isAccountable = jsonObject.get(Constants.IS_ACCOUNTABLE).getAsBoolean();
        countable.isReminderEnabled = jsonObject.get(Constants.IS_REMINDER).getAsBoolean();
        countable.dayRepeater = jsonObject.get(Constants.DAY_REPEATER).getAsInt();
        countable.accountableDates = createDateArrayListFromJsonArray(context, jsonObject.getAsJsonArray(Constants.ACCOUNTABLE_DATES));
        countable.loggedDates = createDateArrayListFromJsonArray(context, jsonObject.getAsJsonArray(Constants.LOGGED_DATES));
        countable.anchorDates = createDateArrayListFromJsonArray(context, jsonObject.getAsJsonArray(Constants.ANCHOR_DATES));
        countable.lastModified = ((DateTime)context.deserialize(jsonObject.get(Constants.LAST_MODIFIED), DateTime.class)).toDate();

        return countable;
    }

    JsonArray createDatesJsonArray(JsonSerializationContext context, ArrayList<Date> dates) {
        JsonArray jsonArray = new JsonArray();
        if (!CollectionUtils.isEmpty(dates)) {
            for (Date date : dates) {
                jsonArray.add(context.serialize(new DateTime(date)));
            }
        }
        return jsonArray;
    }

    ArrayList<Date> createDateArrayListFromJsonArray(JsonDeserializationContext context, JsonArray jsonArray) {
        ArrayList<Date> dates = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
          // todo finish this thru debugging
        }

        return dates;
    }
}
