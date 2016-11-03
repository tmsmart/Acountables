package group.g203.countables.base.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class CalendarUtils {

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
}
