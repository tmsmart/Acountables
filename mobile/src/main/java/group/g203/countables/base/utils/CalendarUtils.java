package group.g203.countables.base.utils;

import android.util.Pair;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

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

    public static Date englishToDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EE, MMM dd, yyyy  -  hh:mm a");
        return formatter.parseDateTime(stringDate).toDate();
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

    public static String getMonthDateYearDashedString(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.getMonthOfYear() + Constants.DASH + dateTime.getDayOfMonth() + Constants.DASH +
                dateTime.getYear();
    }

    public static HashSet<String> getMonthDateYearDashedSet(RealmList<DateField> dates) {
        HashSet<String> dateSet = new HashSet<>(dates.size());

        for (DateField dateField : dates) {
            dateSet.add(getMonthDateYearDashedString(dateField.date));
        }

        return dateSet;
    }

    public static ArrayList<Date> realmListToDateArrayList(RealmList<DateField> dateFields) {
        ArrayList<Date> dates = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dateFields, true)) {
            for (DateField dateField : dateFields) {
                dates.add(dateField.date);
            }
        }
        return dates;
    }

    public static ArrayList<Date> returnLoggedAndAccountableDates(RealmList<DateField> loggedDates,
                                                             RealmList<DateField> accountableDates) {

        HashMap<String, Date> accountableMap = new HashMap<>();
        HashSet<String> removeSet = new HashSet<>();
        ArrayList<Date> dates = new ArrayList<>();
        Date date;

        if (!CollectionUtils.isEmpty(accountableDates, true)) {
            for (DateField dateField : accountableDates) {
                date = dateField.date;
                accountableMap.put(getMonthDateYearDashedString(date), date);
            }

            if (!CollectionUtils.isEmpty(loggedDates, true)) {
                for (DateField dateField : loggedDates) {
                    if (accountableMap.keySet().contains(getMonthDateYearDashedString(dateField.date))) {
                        removeSet.add(getMonthDateYearDashedString(dateField.date));
                    }
                }
            }

            Iterator<Map.Entry<String, Date>> mapIt = accountableMap.entrySet().iterator();
            while (mapIt.hasNext()) {
                Map.Entry<String, Date> entry = mapIt.next();
                if (removeSet.contains(entry.getKey())) {
                    mapIt.remove();
                }
            }
        }

        if (!CollectionUtils.isEmpty(loggedDates, true)) {
            for (DateField dateField : loggedDates) {
                dates.add(dateField.date);
            }
        }

        if (!CollectionUtils.isEmpty(accountableMap.values(), true)) {
            for (Date dateVal : accountableMap.values()) {
                if (isPriorToToday(dateVal)) {
                    dates.add(dateVal);
                }
            }
        }

        Collections.sort(dates);
        return dates;
    }

    public static boolean isToday(DateTime dateTime) {
        if (dateTime != null) {
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);

            DateTime startOfToday = today.toDateTimeAtStartOfDay();
            DateTime startOfTomorrow = tomorrow.toDateTimeAtStartOfDay();

            Interval todayInterval = new Interval(startOfToday, startOfTomorrow);
            return todayInterval.contains(dateTime);
        } else {
            return false;
        }
    }

    public static boolean isPriorToToday(Date date) {
        DateTime dateTime = new DateTime(date);
        if (dateTime != null && !isToday(dateTime)) {
            return dateTime.isBefore(new DateTime());
        } else {
            return false;
        }
    }

    public static int getDayOfWeekInt(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.getDayOfWeek();
    }

}
