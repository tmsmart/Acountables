package group.g203.countables.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import group.g203.countables.base.Constants;

public class Countable implements Parcelable {

    private final static String OPEN_BRACE = "{";
    private final static String CLOSED_BRACE = "}";
    private final static String COLON = ":";
    private final static String QUOTE = "\"";
    private final static String COMMA = ",";
    private final static String OPEN_BRACKET = "[";
    private final static String CLOSED_BRACKET = "]";

    public String name;
    public int id;
    public int index;
    public ArrayList<Date> loggedDates;
    public int timesCompleted;
    public Date lastModified;
    public boolean isAccountable;
    public ArrayList<Date> accountableDates;
    public boolean isReminderEnabled;
    public ArrayList<Date> anchorDates;
    public int dayRepeater;
    public String jsonString;

    public Countable() {
    }

    public Countable(String jsonString) throws JSONException {
        JSONObject jsonObj = new JSONObject(jsonString);
        name = jsonObj.getString(Constants.COUNTABLE_NAME);
        id = jsonObj.getInt(Constants.COUNTABLE_ID);
        index = jsonObj.getInt(Constants.COUNTABLE_INDEX);
        loggedDates = returnDatesFromJson(jsonObj.getJSONArray(Constants.LOGGED_DATES));
        timesCompleted = jsonObj.getInt(Constants.TIMES_COMPLETED);
        lastModified = returnDate(jsonObj.getString(Constants.LAST_MODIFIED));
        isAccountable = jsonObj.getBoolean(Constants.IS_ACCOUNTABLE);
        accountableDates = returnDatesFromJson(jsonObj.getJSONArray(Constants.ACCOUNTABLE_DATES));
        isReminderEnabled = jsonObj.getBoolean(Constants.IS_REMINDER);
        anchorDates = returnDatesFromJson(jsonObj.getJSONArray(Constants.ANCHOR_DATES));
        dayRepeater = jsonObj.getInt(Constants.DAY_REPEATER);
        this.jsonString = jsonString;
    }

    protected Countable(Parcel in) {
        name = in.readString();
        id = in.readInt();
        index = in.readInt();
        if (in.readByte() == 0x01) {
            loggedDates = new ArrayList<Date>();
            in.readList(loggedDates, Date.class.getClassLoader());
        } else {
            loggedDates = null;
        }
        timesCompleted = in.readInt();
        long tmpLastModified = in.readLong();
        lastModified = tmpLastModified != -1 ? new Date(tmpLastModified) : null;
        isAccountable = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            accountableDates = new ArrayList<Date>();
            in.readList(accountableDates, Date.class.getClassLoader());
        } else {
            accountableDates = null;
        }
        isReminderEnabled = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            anchorDates = new ArrayList<Date>();
            in.readList(anchorDates, Date.class.getClassLoader());
        } else {
            anchorDates = null;
        }
        dayRepeater = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
        dest.writeInt(index);
        if (loggedDates == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(loggedDates);
        }
        dest.writeInt(timesCompleted);
        dest.writeLong(lastModified != null ? lastModified.getTime() : -1L);
        dest.writeByte((byte) (isAccountable ? 0x01 : 0x00));
        if (accountableDates == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(accountableDates);
        }
        dest.writeByte((byte) (isReminderEnabled ? 0x01 : 0x00));
        if (anchorDates == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(anchorDates);
        }
        dest.writeInt(dayRepeater);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Countable> CREATOR = new Parcelable.Creator<Countable>() {
        @Override
        public Countable createFromParcel(Parcel in) {
            return new Countable(in);
        }

        @Override
        public Countable[] newArray(int size) {
            return new Countable[size];
        }
    };

    private void setEmptyCountable() {
        name = null;
        id = 0;
        index = 0;
        loggedDates = null;
        timesCompleted = 0;
        lastModified = null;
        isAccountable = false;
        accountableDates = null;
        isReminderEnabled = false;
        anchorDates = null;
        dayRepeater = 0;
        jsonString = null;
    }

    private ArrayList<Date> returnDatesFromJson(JSONArray array) {
        if (array.length() == 0) {
            return null;
        } else {
            ArrayList<Date> dates = new ArrayList<>(array.length());
            for (int i = 0; i < array.length(); i++) {
                try {
                    dates.add(returnDate(((JSONObject) array.get(i)).getString(Constants.DATEFIELD_DATE)));
                } catch (JSONException e) {
                    return null;
                }
            }
            return dates;
        }
    }

    private Date returnDate(String dateString) {
        Date date;
        try {
            SimpleDateFormat format =
                    new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            date = format.parse(dateString);
        } catch (ParseException pe) {
            return null;
        }
        return date;
    }

    public void updateCount() throws JSONException {
        int updatedCount = timesCompleted++;

        JSONObject jsonObj = new JSONObject(jsonString);

        jsonObj.put(Constants.TIMES_COMPLETED, updatedCount);
        jsonObj.put(Constants.LAST_MODIFIED, new Date());

        jsonString = jsonObj.toString();
    }
}