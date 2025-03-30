package com.example.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String formatDate(long timestampMillis) {
        Date date = new Date(timestampMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE d, HH:mm", Locale.ENGLISH);
        return sdf.format(date);
    }

    public static String formatDate(String timeString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
            SimpleDateFormat outputFormat = new SimpleDateFormat("d MMM HH:mm", Locale.ENGLISH);
            Date date = inputFormat.parse(timeString);
            assert date != null;
            return outputFormat.format(date);
        } catch (Exception e) {
            return timeString;
        }
    }


    public static String getCurrentFormattedDate() {
        return formatDate(System.currentTimeMillis());
    }

    public static String formatTemperature(String tempString) {
        String clean = tempString.replace("°", "").replace("C", "").trim();
        return clean + "°C";
    }

}
