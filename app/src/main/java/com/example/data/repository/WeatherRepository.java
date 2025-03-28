package com.example.data.repository;

import com.example.conf.WeatherConfig;
import com.example.data.api.ApiClient;
import com.example.data.api.ForecastResponse;
import com.example.data.api.WeatherApiService;
import com.example.data.api.WeatherResponse;

import retrofit2.Callback;

public class WeatherRepository {

    private final WeatherApiService api;


    public WeatherRepository() {
        this.api = ApiClient.getApiService();
    }

    public void getWeather(String city, Callback<WeatherResponse> callback) {
        api.getWeather(city, WeatherConfig.API_KEY, WeatherConfig.UNITS).enqueue(callback);
    }

    public void getForecast(String city, Callback<ForecastResponse> callback) {
        api.getForecast(city, WeatherConfig.API_KEY, WeatherConfig.UNITS).enqueue(callback);
    }
}
