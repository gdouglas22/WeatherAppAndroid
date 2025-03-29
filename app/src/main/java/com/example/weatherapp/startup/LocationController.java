package com.example.weatherapp.startup;

import android.app.Activity;
import android.util.Log;

import com.example.conf.WeatherConfig;
import com.example.ui.viewmodel.weather.WeatherViewModel;
import com.example.util.LocationHelper;
import com.example.util.PrefsHelper;

public class LocationController {

    private final WeatherViewModel viewModel;
    private final Activity activity;

    public LocationController(Activity activity, WeatherViewModel viewModel) {
        this.activity = activity;
        this.viewModel = viewModel;
    }

    public void resolveCityFromLocation() {
        LocationHelper.resolveCityFromLocation(activity, activity, this::handleResolvedCity);
    }

    private void handleResolvedCity(String cityName) {
        if (cityName != null && !cityName.isEmpty()) {
            WeatherConfig.city = cityName;
            String lastSavedCity = PrefsHelper.getSavedCity(activity);

            Log.d("LocationController", "LocationHelper вернул город: " + cityName);
            Log.d("LocationController", "Последний сохранённый город: " + lastSavedCity);

            if (!cityName.equalsIgnoreCase(lastSavedCity)) {
                PrefsHelper.saveCity(activity, cityName);
                viewModel.setCity(cityName);
            }
        } else {
            Log.w("LocationController", "Город не удалось определить");
        }
    }
}

