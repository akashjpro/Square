package com.example.tmha.square.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by tmha on 6/14/2017.
 */

public class TimeUtils {

    /**
     * Get current time and format
     * @return: String
     */
    public static String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String time = dateFormat.format(calendar.getTime());
        return time;
    }
}
