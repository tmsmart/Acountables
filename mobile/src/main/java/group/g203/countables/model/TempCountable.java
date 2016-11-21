package group.g203.countables.model;

import java.util.ArrayList;
import java.util.Date;

import group.g203.countables.base.utils.CollectionUtils;
import io.realm.RealmList;

public class TempCountable {

    public String name;
    public int index;
    public ArrayList<Date> loggedDates;
    public int timesCompleted;
    public Date lastModified;
    public boolean isAccountable;
    public ArrayList<Date> accountableDates;
    public boolean isReminderEnabled;
    public ArrayList<Date> anchorDates;
    public int dayRepeater;

    public TempCountable(String name, int index, ArrayList<Date> loggedDates, int timesCompleted,
                         Date lastModified, boolean isAccountable, ArrayList<Date> accountableDates,
                         boolean isReminderEnabled, ArrayList<Date> anchorDates, int dayRepeater) {
        this.name = name;
        this.index = index;
        this.loggedDates = loggedDates;
        this.timesCompleted = timesCompleted;
        this.lastModified = lastModified;
        this.isAccountable = isAccountable;
        this.accountableDates = accountableDates;
        this.isReminderEnabled = isReminderEnabled;
        this.anchorDates = anchorDates;
        this.dayRepeater = dayRepeater;
    }

    public static TempCountable createTempCountable(Countable countable) {
        ArrayList<Date> dates = dateRealmListToArrayList(countable.loggedDates);
        ArrayList<Date> accountableDates = dateRealmListToArrayList(countable.accountableDates);
        ArrayList<Date> anchorDates = dateRealmListToArrayList(countable.anchorDates);

        return new TempCountable(countable.name, countable.index, dates, countable.timesCompleted,
                countable.lastModified, countable.isAccountable, accountableDates,
                countable.isReminderEnabled, anchorDates, countable.dayRepeater);
    }

    static ArrayList<Date> dateRealmListToArrayList(RealmList<DateField> list) {
        if (!CollectionUtils.isEmpty(list, true)) {
            ArrayList<Date> arrayList = new ArrayList<>(list.size());

            for (DateField dateField : list) {
                arrayList.add(dateField.date);
            }

            return arrayList;
        } else {
            return null;
        }
    }
}
