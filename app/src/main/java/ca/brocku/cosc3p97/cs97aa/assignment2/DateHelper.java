package ca.brocku.cosc3p97.cs97aa.assignment2;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class holds some common date formatting functions
 * that is consumed by the application.
 */
public class DateHelper {
    private static final String DATE_LONG_FORMAT = "MMMM d, yyyy";
    private static final String DATE_SHORT_FORMAT = "yyyy-MM-dd";


    /**
     * Get a string representation of today's date formatted
     * in the long format (ex. January 1, 2016).
     * @return The string representation of the date
     */
    public static String getTodayLongDate() {
        Calendar calendar = Calendar.getInstance();
        return formatLongDate(calendar);
    }


    /**
     * Get today's date as a date object
     * @return The date object that is today's date
     */
    public static Date getToday() {
        return Calendar.getInstance().getTime();
    }


    /**
     * Get tomorrow's date as a date object
     * @return The date object that is tomorrow's date
     */
    public static Date getTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }


    /**
     * Get a string representation of tomorrow's date formatted
     * in the long date format (ex. January 1, 2016).
     * @return The string representation of the date
     */
    public static String getTomorrowLongDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        return formatLongDate(calendar);
    }


    /**
     * Format a date provided as a parameter into the long date format
     * (ex. January 1, 2016).
     * @param date The date to be formatted
     * @return The string representation of the date
     */
    private static String formatLongDate(Calendar date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_LONG_FORMAT);
        return dateFormat.format(date.getTime());
    }


    /**
     * Format a date provided as a parameter into the short date format
     * (ex. 2016-01-01).
     * @param date
     * @return
     */
    public static String formatShortDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_SHORT_FORMAT);
        return dateFormat.format(date);
    }


    /**
     * Given a date as a parameter find the next date of similar type. For example,
     * given a weekday as a parameter find the next weekday date or given a weekend day
     * as a parameter find the next weekend day date.
     * @param fromDate The date from which to calculate the next date
     * @return the next date as a date object
     */
    public  static Date getNextDate(Date fromDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);
        final int DAYS_UNTIL_NEXT_SATURDAY = 6;
        final int DAYS_UNTIL_NEXT_MONDAY = 3;
        final int DAYS_UNTIL_TOMORROW = 1;

        int daysToAdd;

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                daysToAdd = DAYS_UNTIL_NEXT_SATURDAY;
                break;
            case Calendar.FRIDAY:
                daysToAdd = DAYS_UNTIL_NEXT_MONDAY;
                break;
            default:
                daysToAdd = DAYS_UNTIL_TOMORROW;
        }

        calendar.add(Calendar.DATE, daysToAdd);
        return calendar.getTime();
    }

}
