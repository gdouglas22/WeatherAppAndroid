package com.example.data.api;

import java.util.List;
import java.util.Map;

public class WeatherResponse {

    private Main main;
    private Wind wind;
    private String name;
    private long dt;
    private List<Weather> weather;
    private Map<String, Float> rain;

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public String getName() {
        return name;
    }

    public long getDt() {
        return dt;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Map<String, Float> getRain() {
        return rain;
    }

    public static class Main {
        private float temp;
        private int humidity;

        public float getTemp() {
            return temp;
        }

        public int getHumidity() {
            return humidity;
        }
    }

    public static class Wind {
        private float speed;

        public float getSpeed() {
            return speed;
        }
    }

    public static class Weather {
        private String main;
        private String description;
        private String icon;

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }
}


