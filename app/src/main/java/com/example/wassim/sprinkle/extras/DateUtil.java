package com.example.wassim.sprinkle.extras;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Classe utilitaire pour effectuer certaines operations sur les dates
 */
public class DateUtil {

    /**
     * Fonction permettant d'ajouter un nombre de jours à une date donnée.
     * @param startDate
     * @param wateringFrequency
     * @return long
     */
    public static long addDaysToDate(long startDate, int wateringFrequency)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(startDate));
        c.add(Calendar.DATE, wateringFrequency);

        return c.getTimeInMillis();
    }

    /**
     * Fonction permettant de comparer deux dates
     * @param date1
     * @param date2
     * @return int
     */
    public static int compareTwoDate(long date1, long date2)
    {
            long diff = date1 - date2;
            long seconds = diff / 1000l;
            long minutes = seconds / 60l;
            long hours = minutes / 60l;

            if(hours < 0)
                return -1;
            else if (hours > 0 && hours <= 24)
                return 0;
            else
                return 1;
    }

    /**
     * Fonction pour le formattage d'une date: dd-mm-yyyy - h:m
     * @param date
     * @return
     */
    public static String formatDate(long date)
    {
        return new SimpleDateFormat("dd-MM-yyyy - H:mm").format(date);
    }
}
