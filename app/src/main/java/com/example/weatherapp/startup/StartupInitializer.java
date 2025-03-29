package com.example.weatherapp.startup;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;

import com.example.conf.WeatherConfig;
import com.example.ui.viewmodel.weather.WeatherViewModel;
import com.example.util.PrefsHelper;

public class StartupInitializer {

    private final WeatherViewModel viewModel;
    private final Application app;

    public StartupInitializer(WeatherViewModel viewModel, Application app) {
        this.viewModel = viewModel;
        this.app = app;
    }

    public void prefetchData() {
        viewModel.prefetchWeatherData();

        String savedCity = PrefsHelper.getSavedCity(app);
        if (savedCity != null) {
            WeatherConfig.city = savedCity;
            viewModel.setCity(savedCity);
        }
    }
}

