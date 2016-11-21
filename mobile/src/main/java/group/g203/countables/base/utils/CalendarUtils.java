package group.g203.countables.base.utils;

import android.util.Pair;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import group.g203.countables.base.Constants;
import group.g203.countables.model.DateField;
import io.realm.RealmList;

public class CalendarUtils {

    public final static int WEEK_LENGTH = 7;

    public static String return_MMDDYYYY_Format(Date date) {
        DateTime dateTime = new DateTime(date);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yy");
        return formatter.print(dateTime);
    }

    public static String returnEnglishFormat(Date date) {
        DateTime dateTime = new DateTime(date);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EE, MMM dd, yyyy  -  hh:mm a");
        return formatter.print(dateTime);
    }

    public static String returnEnglishMonth(Date date) {
        DateTime dateTime = new DateTime(date);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM");
        return formatter.print(dateTime);
    }

    public static String returnEnglishCalendarDay(Date date) {
        DateTime dateTime = new DateTime(date);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EE");
        return formatter.print(dateTime);
    }

    public static String returnDayNumberString(Date date) {
        DateTime dateTime = new DateTime(date);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("d");
        return formatter.print(dateTime);
    }

    public static ArrayList<Pair<Date, Pair<String, String>>> dateRepeatAspectList(Date date) {
        ArrayList<Pair<Date, Pair<String, String>>> dateList = new ArrayList<>(WEEK_LENGTH);
        DateTime dateTime = new DateTime(date);

        for (int i = 0; i < WEEK_LENGTH; i++) {
            dateList.add(new Pair<>(dateTime.toDate(), new Pair<>(returnEnglishCalendarDay(dateTime.toDate()),
                    returnDayNumberString(dateTime.toDate()))));
            dateTime = dateTime.plusDays(1);
        }
        return dateList;
    }

    public static ArrayList<Date> weekDates(Date date) {
        ArrayList<Date> dates = new ArrayList<>(WEEK_LENGTH);
        DateTime dateTime = new DateTime(date);

        for (int i = 0; i < WEEK_LENGTH; i++) {
            dates.add(dateTime.toDate());
            dateTime = dateTime.plusDays(1);
        }

        return dates;
    }

    public static Date setNotificationDate(Date date, int hour, int min) {
        DateTime dateTime = new DateTime(date).withTime(hour, min, Constants.ZERO, Constants.ZERO);
        return dateTime.toDate();
    }

    public static Pair<Integer, Integer> getHoursAndMins(DateField dateField) {
        DateTime dateTime = new DateTime(dateField.date);
        return new Pair<>(dateTime.getHourOfDay(), dateTime.getMinuteOfHour());
    }

    public static HashSet<Pair<String, String>> anchorDatePairs(RealmList<DateField> dateFields) {
        HashSet<Pair<String, String>> dateSet = new HashSet(dateFields.size());
        for (DateField dateField : dateFields) {
            DateTime dateTime = new DateTime(dateField.date);
            dateSet.add(new Pair<>(returnEnglishCalendarDay(dateTime.toDate()),
                    returnDayNumberString(dateTime.toDate())));
        }
        return dateSet;
    }
}
