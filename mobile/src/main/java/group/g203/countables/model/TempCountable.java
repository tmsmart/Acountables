package group.g203.countables.model;

import java.util.ArrayList;
import java.util.Date;

public class TempCountable {

    public String name;
    public int index;
    public ArrayList<Date> loggedDates;
    public int timesCompleted;
    public Date lastModified;
    public Date accountableStartDate;
    public boolean isAccountable;
    public boolean isReminderEnabled;

    public TempCountable(String name, int index, ArrayList<Date> loggedDates,
                         int timesCompleted, Date lastModified, Date accountableStartDate,
                         boolean isAccountable, boolean isReminderEnabled) {
        this.name = name;
        this.index = index;
        this.loggedDates = loggedDates;
        this.timesCompleted = timesCompleted;
        this.lastModified = lastModified;
        this.accountableStartDate = accountableStartDate;
        this.isAccountable = isAccountable;
        this.isReminderEnabled = isReminderEnabled;
    }

    public static TempCountable createTempCountable(Countable countable) {
        ArrayList<Date> dates = new ArrayList<>(countable.loggedDates.size());

        for (DateField dateField : countable.loggedDates) {
            dates.add(dateField.date);
        }

        return new TempCountable(countable.name, countable.index, dates, countable.timesCompleted,
                countable.lastModified, countable.accountableStartDate, countable.isAccountable,
                countable.isReminderEnabled);
    }
}
