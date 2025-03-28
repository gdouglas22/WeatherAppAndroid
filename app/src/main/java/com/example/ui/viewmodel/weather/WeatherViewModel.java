package com.example.ui.viewmodel.weather;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.data.api.WeatherResponse;
import com.example.data.repository.WeatherRepository;
import com.example.ui.viewmodel.common.UiState;
import com.example.ui.viewmodel.weather.WeatherData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {

    private final MutableLiveData<String> cityLiveData = new MutableLiveData<>();
    private final MutableLiveData<WeatherData> weatherLiveData = new MutableLiveData<>();
    private final MutableLiveData<UiState> uiStateLiveData = new MutableLiveData<>();

    private final WeatherRepository repository = new WeatherRepository();

    public LiveData<String> getCityLiveData() {
        return cityLiveData;
    }

    public LiveData<WeatherData> getWeatherLiveData() {
        return weatherLiveData;
    }

    public LiveData<UiState> getUiStateLiveData() {
        return uiStateLiveData;
    }

    public void setCity(String city) {
        cityLiveData.setValue(city);
        fetchWeatherData(city);
    }

    private void fetchWeatherData(String city) {
        uiStateLiveData.setValue(UiState.LOADING);

        repository.getWeather(city, new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    weatherLiveData.setValue(WeatherData.fromResponse(response.body()));
                    uiStateLiveData.setValue(UiState.SUCCESS);
                } else {
                    uiStateLiveData.setValue(UiState.ERROR);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                uiStateLiveData.setValue(UiState.ERROR);
            }
        });
    }
}



