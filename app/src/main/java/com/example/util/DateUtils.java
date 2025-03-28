package com.example.weatherapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String formatDate(long timestampMillis) {
        Date date = new Date(timestampMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE d, HH:mm", Locale.ENGLISH);
        return sdf.format(date);
    }

    public static String getCurrentFormattedDate() {
        return formatDate(System.currentTimeMillis());
    }
}
