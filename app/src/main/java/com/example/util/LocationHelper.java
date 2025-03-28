package com.example.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.conf.WeatherConfig;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;

public class LocationHelper {

    public interface CityCallback {
        void onCityResolved(String cityName);
    }

    public static void resolveCityFromLocation(Context context, Activity activity, CityCallback callback) {
        FusedLocationProviderClient fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (!addresses.isEmpty()) {
                        String cityName = addresses.get(0).getLocality();
                        if (cityName != null && !cityName.isEmpty()) {
                            WeatherConfig.city = cityName;
                            Log.d("LocationHelper", "City resolved: " + cityName);
                            callback.onCityResolved(cityName); // 🔔 Уведомляем
                        }
                    }
                } catch (IOException e) {
                    Log.e("LocationHelper", "Geocoder failed", e);
                }
            }
        });
    }
}


