package com.example.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsHelper {
    private static final String PREFS_NAME = "weather_prefs";
    private static final String KEY_CITY = "last_city";

    public static void saveCity(Context context, String city) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_CITY, city).apply();
    }

    public static String getSavedCity(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_CITY, null);
    }
}
