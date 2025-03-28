package com.example.weatherapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.conf.WeatherConfig;
import com.example.ui.viewmodel.weather.WeatherViewModel;
import com.example.util.LocationHelper;
import com.example.util.PrefsHelper;
import com.example.util.UserInfoUtils;
import com.example.weatherapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private WeatherViewModel viewModel;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.weatherapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.GET_ACCOUNTS}, 2001);
            return;
        }

        // Устанавливаем имя пользователя
        String userName = UserInfoUtils.getUserAccountName(this);
        binding.setUserName(userName);

        // Загружаем UI сразу
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weatherFragmentContainer, new WeatherFragment())
                    .replace(R.id.forecastFragmentContainer, new ForecastFragment())
                    .commit();
        }

        // Загружаем сохранённый город для начальной отрисовки
        String savedCity = PrefsHelper.getSavedCity(this);
        if (savedCity != null) {
            WeatherConfig.city = savedCity;
            viewModel.setCity(savedCity);
        }

        // Проверяем разрешение и вызываем LocationHelper при наличии
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationHelper.resolveCityFromLocation(this, this, this::handleResolvedCity);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void handleResolvedCity(String cityName) {
        if (cityName != null && !cityName.isEmpty()) {
            WeatherConfig.city = cityName;
            String lastSavedCity = PrefsHelper.getSavedCity(this);

            Log.d("MainActivity", "LocationHelper вернул город: " + cityName);
            Log.d("MainActivity", "Последний сохранённый город: " + lastSavedCity);

            if (!cityName.equalsIgnoreCase(lastSavedCity)) {
                Log.d("MainActivity", "Новый город: сохраняем и обновляем UI");
                PrefsHelper.saveCity(this, cityName);
                viewModel.setCity(cityName);
            } else {
                Log.d("MainActivity", "Город совпадает с сохранённым — UI не обновляется");
            }
        } else {
            Log.w("MainActivity", "Город не удалось определить");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity", "Пользователь дал разрешение — пробуем снова получить город");
                LocationHelper.resolveCityFromLocation(this, this, this::handleResolvedCity);
            } else {
                Log.w("MainActivity", "Пользователь отказал в доступе к геолокации");
            }
        }
    }
}

