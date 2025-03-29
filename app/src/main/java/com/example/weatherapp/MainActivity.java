package com.example.weatherapp;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ui.viewmodel.weather.WeatherViewModel;
import com.example.util.UserInfoUtils;
import com.example.weatherapp.databinding.ActivityMainBinding;
import com.example.weatherapp.fragments.ForecastFragment;
import com.example.weatherapp.fragments.WeatherFragment;
import com.example.weatherapp.startup.LocationController;
import com.example.weatherapp.startup.PermissionManager;
import com.example.weatherapp.startup.StartupInitializer;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private LocationController locationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.weatherapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WeatherViewModel viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        StartupInitializer initializer = new StartupInitializer(viewModel, getApplication());
        locationController = new LocationController(this, viewModel);

        initializer.prefetchData();

        if (!PermissionManager.hasLocationPermission(this)) {
            PermissionManager.requestLocationPermission(this, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            locationController.resolveCityFromLocation();
        }

        binding.setUserName(UserInfoUtils.getUserAccountName(this));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weatherFragmentContainer, new WeatherFragment())
                    .replace(R.id.forecastFragmentContainer, new ForecastFragment())
                    .commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locationController.resolveCityFromLocation();
        }
    }
}


