package com.example.ui.viewmodel.forecast;

public final class ForecastData {

    private final String day;
    private final String temperature;
    private final String humidity;
    private final String wind;
    private final String rain;
    private final String time;
    private final String description;

    public ForecastData(String day, String temperature, String humidity, String wind, String rain, String time, String description) {
        this.day = day;
        this.temperature = temperature;
        this.humidity = humidity;
        this.wind = wind;
        this.rain = rain;
        this.time = time;
        this.description = description;
    }

    public String getDay() {
        return day;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWind() {
        return wind;
    }

    public String getRain() {
        return rain;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }
}

