package za.co.steff.goals.common.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";

    private static final String[] dayNames = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };

    public static String getTimeOfDayString() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            return "morning";
        } else if(timeOfDay >= 12 && timeOfDay < 17){
            return "afternoon";
        } else if(timeOfDay >= 17 && timeOfDay < 21){
            return "evening";
        } else {
            return "night";
        }
    }

    public static boolean isToday(DateTime inputDate) {
        inputDate = inputDate.withTimeAtStartOfDay();
        DateTime today = new DateTime().withTimeAtStartOfDay();
        return today.equals(inputDate);
    }

    public static boolean isTomorrow(DateTime inputDate) {
        inputDate = inputDate.withTimeAtStartOfDay();
        DateTime tomorrow = new DateTime().withTimeAtStartOfDay().plusDays(1);
        return tomorrow.equals(inputDate);
    }

    public static boolean isInThisWeek(DateTime inputDate) {
        DateTime sunday = DateTime.now();
        sunday = sunday.plusDays(7 - DateTime.now().getDayOfWeek());
        sunday = sunday.withTime(23, 59, 59, 59);

        // Check if today is sunday
        return inputDate.isBefore(sunday);
    }

    public static boolean isInNextWeek(DateTime inputDate) {
        if(isInThisWeek(inputDate)) {
            return false;
        }

        DateTime nextSunday = DateTime.now();
        nextSunday = nextSunday.plusDays(7 - DateTime.now().getDayOfWeek());
        nextSunday = nextSunday.plusWeeks(1);
        nextSunday = nextSunday.withTime(23, 59, 59, 59);

        // Check if today is sunday
        return inputDate.isBefore(nextSunday);
    }

    public static String getHumanReadableDate(DateTime inputDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEEE, dd MMMM");
        return formatter.print(inputDate);
    }

    public static String getHumanReadableTime(DateTime inputDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("h:mm a");
        return formatter.print(inputDate);
    }

    public static String getDayName(DateTime inputDate) {
        return dayNames[inputDate.getDayOfWeek() - 1];
    }

    public static String getNicePhraseFromDate(DateTime date, boolean includeTime) {
        String phrase = "";
        if(date == null)
            return phrase;

        // Format date nicely
        if(isToday(date)) {
            phrase += "today";
        } else if(isTomorrow(date)) {
            phrase += "tomorrow";
        } else if(isInThisWeek(date)) {
            phrase += "this " + getDayName(date);
        } else if(isInNextWeek(date)) {
            phrase += "next " + getDayName(date);
        } else {
            phrase += getHumanReadableDate(date);
        }

        // Format time
        if(includeTime) {
            phrase += " at ";
            phrase += getHumanReadableTime(date);
        }

        return phrase;
    }

}
