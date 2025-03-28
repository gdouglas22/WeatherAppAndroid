package com.example.data.api;



import com.example.ui.viewmodel.forecast.ForecastData;

import java.util.List;
import java.util.ArrayList;

public class ForecastResponse {

    // Исправлено: теперь list содержит ForecastDataFromResponse, а не ForecastData
    private List<ForecastDataFromResponse> list;

    public List<ForecastDataFromResponse> getList() {
        return list;
    }

    public void setList(List<ForecastDataFromResponse> list) {
        this.list = list;
    }

    // Конвертация в ForecastData
    public List<com.example.ui.viewmodel.forecast.ForecastData> toForecastDataList() {
        List<ForecastData> forecastDataList = new ArrayList<>();
        if (list != null) {
            for (ForecastDataFromResponse forecast : list) {
                String time = forecast.getDt_txt();
                String temperature = String.valueOf(forecast.getMain().getTemp());
                String description = forecast.getWeather()[0].getDescription();
                forecastDataList.add(new ForecastData("empty", temperature, "empty", "empty", "empty", time, description));
            }
        }
        return forecastDataList;
    }

    public static class ForecastDataFromResponse {
        private String dt_txt;
        private Main main;
        private Weather[] weather;

        public String getDt_txt() {
            return dt_txt;
        }

        public Main getMain() {
            return main;
        }

        public Weather[] getWeather() {
            return weather;
        }

        public static class Main {
            private float temp;

            public float getTemp() {
                return temp;
            }
        }

        public static class Weather {
            private String description;

            public String getDescription() {
                return description;
            }
        }
    }
}






