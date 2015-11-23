package ca.brocku.cosc3p97.cs97aa.assignment2;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class holds some common date formatting functions
 * that is consumed by the application.
 */
public class DateHelper {
    public static final String DATE_LONG_FORMAT = "MMMM d, yyyy";
    public static final String DATE_SHORT_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "kk:mm";
    public static final int YEAR_INDEX = 0;
    public static final int MONTH_INDEX = 1;
    public static final int DAY_INDEX = 2;
    public static final int HOUR_INDEX = 0;
    public static final int MINUTE_INDEX = 1;


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
     * @return The string representation of the date in the desired format
     */
    private static String formatLongDate(Calendar date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_LONG_FORMAT);
        return dateFormat.format(date.getTime());
    }


    /**
     * Format a date provided as a parameter into the short date format
     * (ex. 2016-01-01).
     * @param date The date object that contains the date to be formatted
     * @return The string representation of the date in the desired format
     */
    public static String formatShortDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_SHORT_FORMAT);
        return dateFormat.format(date);
    }


    /**
     * Format a time provided as a parameter into the time format (ex. 10:15)
     * @param time The date object that contains the time to be formatted
     * @return The string representation of the time in the desired format
     */
    public static String formatTime(Date time) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        return timeFormat.format(time);
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


    /**
     * Split the time passed as a string argument into 2 integers which represent the hours and
     * minutes of the time value
     * @param timeString The time value to be split
     * @return The integer array which contains to the 2 integers
     */
    public static int[] splitTime(String timeString) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        int[] ints = new int[2];

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(timeFormat.parse(timeString));
            ints[HOUR_INDEX] = calendar.get(Calendar.HOUR_OF_DAY);
            ints[MINUTE_INDEX] = calendar.get(Calendar.MINUTE);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ints;
    }


    /**
     * Split the date passed as a string argument into 3 integers which represent the year,
     * month and day of the date value
     * @param dateString The date value to be split
     * @return The integer array which contains to the 3 integers
     */
    public static int[] splitDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_SHORT_FORMAT);
        int[] ints = new int[3];

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(dateString));
            ints[YEAR_INDEX] = calendar.get(Calendar.YEAR);
            ints[MONTH_INDEX] = calendar.get(Calendar.MONTH) + 1;
            ints[DAY_INDEX] = calendar.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ints;

    }


    /**
     * Convert a set of integers passed as calendar date arguments into a Date object
     * @param year The year value of the date
     * @param month The month value of the date
     * @param day The day value of the date
     * @return The Date object representation of the integers provided
     */
    public static Date dateFromInts(int year, int month, int day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
        String dateString = String.format("%d-%d-%d", year, month+1, day);
        Date date = null;

        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }


    /**
     * Convert a set of integers passed as time arguments into a Date object
     * @param hour The hour associated with the time
     * @param minute The minute associated with the time
     * @return The Date object which is a representation of the time associated with the
     * integers passed as arguments
     */
    public static Date timeFromInts(int hour, int minute) {
        String timeString = String.format("%d:%d", hour, minute);
        SimpleDateFormat timeFormat = new SimpleDateFormat("k:m");
        Date time = null;

        try {
            time = timeFormat.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }
}
