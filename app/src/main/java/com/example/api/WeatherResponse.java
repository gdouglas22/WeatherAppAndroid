package com.example.api;

public class WeatherResponse {

    private Main main;
    private Wind wind;

    private String name;

    // Геттеры для доступа к данным
    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public String getName() {
        return name;
    }

    // Статический внутренний класс для модели "main", содержащей температуру
    public static class Main {
        private float temp; // Температура

        public float getTemp() {
            return temp;
        }
    }

    // Статический внутренний класс для модели "wind", содержащей скорость ветра
    public static class Wind {
        private float speed; // Скорость ветра

        public float getSpeed() {
            return speed;
        }
    }
}

