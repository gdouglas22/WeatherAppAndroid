package com.example.ui.viewmodel.weather;

import com.example.data.api.WeatherResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherData {

    private final String city;
    private final String temperature;
    private final String windSpeed;
    private final String humidity;
    private final String rain;
    private final String description;
    private final String date;

    public WeatherData(String city, String temperature, String windSpeed, String humidity, String rain, String description, String date) {
        this.city = city;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.rain = rain;
        this.description = description;
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getRain() {
        return rain;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public static WeatherData fromResponse(WeatherResponse response) {
        String city = response.getName();
        String temperature = Math.round(response.getMain().getTemp()) + "°C";
        String windSpeed = Math.round(response.getWind().getSpeed()) + " m/s";
        String humidity = response.getMain().getHumidity() + "%";
        String rain = response.getRain() != null && response.getRain().get("1h") != null
                ? response.getRain().get("1h") + " mm"
                : "0 mm";
        String description = (response.getWeather() != null && !response.getWeather().isEmpty())
                ? response.getWeather().get(0).getMain()
                : "—";

        String formattedDate = formatDate(System.currentTimeMillis());

        return new WeatherData(city, temperature, windSpeed, humidity, rain, description, formattedDate);
    }

    private static String formatDate(long timestampMillis) {
        Date date = new Date(timestampMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE d, HH:mm", Locale.ENGLISH);
        return sdf.format(date);
    }
}



