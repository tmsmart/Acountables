package group.g203.countables.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class Countable implements Parcelable {

    public String name;
    public int id;
    public int index;
    public ArrayList<Date> loggedDates;
    public int timesCompleted;
    public Date lastModified;
    public boolean isAccountable;
    public ArrayList<Date>  accountableDates;
    public boolean isReminderEnabled;
    public ArrayList<Date> anchorDates;
    public int dayRepeater;

    public Countable() {
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
}