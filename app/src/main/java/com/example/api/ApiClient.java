package com.example.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.openweathermap.org/"; // Правильный базовый URL

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        // Проверяем, инициализирован ли уже Retrofit
        if (retrofit == null) {
            // Инициализируем Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)  // Устанавливаем базовый URL
                    .addConverterFactory(GsonConverterFactory.create())  // Устанавливаем конвертер для JSON
                    .build();
        }
        return retrofit;  // Возвращаем объект Retrofit
    }

    // Получаем интерфейс WeatherApiService для выполнения API-запросов
    public static WeatherApiService getApiService() {
        return getClient().create(WeatherApiService.class);
    }
}
