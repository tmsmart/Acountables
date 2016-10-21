package group.g203.countables.model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Countable extends RealmObject {

    public String name;
    public RealmList<DateField> loggedDates;
    public DateField accountableStartDate;
    public boolean isAccountable;
    public boolean isReminderEnabled;

}
