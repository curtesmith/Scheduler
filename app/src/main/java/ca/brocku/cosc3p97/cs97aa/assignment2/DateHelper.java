package ca.brocku.cosc3p97.cs97aa.assignment2;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
    private static final String DATE_LONG_FORMAT = "MMMM d, yyyy";
    private static final String DATE_SHORT_FORMAT = "yyyy-MM-dd";

    public static String getTodayLongDate() {
        Calendar calendar = Calendar.getInstance();
        return formatLongDate(calendar);
    }


    public static Date getToday() {
        return Calendar.getInstance().getTime();
    }


    public static Date getTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }


    public static String getTomorrowLongDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        return formatLongDate(calendar);
    }


    private static String formatLongDate(Calendar date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_LONG_FORMAT);
        return dateFormat.format(date.getTime());
    }


    public static String formatShortDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_SHORT_FORMAT);
        return dateFormat.format(date);
    }


    public  static Date getNextDate(Date fromDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);

        int daysToAdd;

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                daysToAdd = 6;
                break;
            case Calendar.FRIDAY:
                daysToAdd = 3;
                break;
            default:
                daysToAdd = 1;
        }

        calendar.add(Calendar.DATE, daysToAdd);
        return calendar.getTime();
    }

}
