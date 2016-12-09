package group.g203.countables.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Countable extends RealmObject {

    public String name;
    public int id;
    public int index;
    public RealmList<DateField> loggedDates;
    public int timesCompleted;
    public Date lastModified;
    public boolean isAccountable;
    public RealmList<DateField> accountableDates;
    public boolean isReminderEnabled;
    public RealmList<DateField> anchorDates;
    public int dayRepeater;

}
