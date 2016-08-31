package nz.co.thebteam.AutomationLibrary.Utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateHelper {

    //Pass in a date format as a string and optionally pass in days you want to add or subtract (negative) from today's date
    public static String calculateDate(String df) {
        DateFormat dateFormat = new SimpleDateFormat(df);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String calculateDate(String df, int diff) {
        DateFormat dateFormat = new SimpleDateFormat(df);
        Date date = new Date();
        date = addDays(date, diff);
        return dateFormat.format(date);
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
}