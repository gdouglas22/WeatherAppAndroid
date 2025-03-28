package com.example.data.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeather(
            @Query("q") String city, // Город
            @Query("appid") String apiKey, // API ключ
            @Query("units") String units // Единицы измерения (например, Celsius)
    );

    @GET("data/2.5/forecast")
    Call<ForecastResponse> getForecast(
            @Query("q") String city,
            @Query("appid") String apiKey,
            @Query("units") String units
    );
}

