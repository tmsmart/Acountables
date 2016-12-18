package group.g203.countables.model;

import java.util.ArrayList;
import java.util.Date;

public class Countable {

    public String name;
    public int id;
    public int index;
    public ArrayList<Date> loggedDates;
    public int timesCompleted;
    public Date lastModified;
    public boolean isAccountable;
    public ArrayList<Date>  accountableDates;
    public boolean isReminderEnabled;
    public ArrayList<Date>  anchorDates;
    public int dayRepeater;

}
