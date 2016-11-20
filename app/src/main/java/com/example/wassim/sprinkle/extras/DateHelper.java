package com.example.wassim.sprinkle.extras;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by wassim on 11/20/16.
 */

public class DateHelper {

    public static long addDaysToDate(long startDate, int wateringFrequency)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(startDate));
        c.add(Calendar.DATE, wateringFrequency);

        return c.getTimeInMillis();
    }

    public static long substractTwoDates(long date1, long date2)
    {
        Date d1 = new Date(date1);
        Date d2 = new Date(date2);
        long diff = Math.abs(d1.getTime() - d2.getTime());

        return (diff + 12 * 60 * 60 * 1000) / (24 * 60 * 60 * 1000);
    }
}
