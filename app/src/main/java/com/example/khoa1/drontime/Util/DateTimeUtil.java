package com.example.khoa1.drontime.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by khoa1 on 12/16/2017.
 */

public class DateTimeUtil {
    public static SimpleDateFormat dfDay = new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat dfHour = new SimpleDateFormat("HH:mm");
    public static boolean isToday(Date time) {
        Calendar now = Calendar.getInstance();
        Calendar timeToCheck = Calendar.getInstance();
        timeToCheck.setTime(time);
        return now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)
                && now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR);
    }
}
