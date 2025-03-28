package com.example.ui.viewmodel.forecast;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.data.api.ForecastResponse;
import com.example.data.repository.WeatherRepository;
import com.example.ui.viewmodel.common.UiState;
import com.example.ui.viewmodel.forecast.ForecastData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastViewModel extends ViewModel {

    private final MutableLiveData<List<ForecastData>> forecastLiveData = new MutableLiveData<>();
    private final MutableLiveData<UiState> uiStateLiveData = new MutableLiveData<>();
    private final WeatherRepository repository = new WeatherRepository();

    public LiveData<List<ForecastData>> getForecastLiveData() {
        return forecastLiveData;
    }

    public LiveData<UiState> getUiStateLiveData() {
        return uiStateLiveData;
    }

    public void loadForecast(String city) {
        uiStateLiveData.setValue(UiState.LOADING);

        repository.getForecast(city, new Callback<>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    forecastLiveData.setValue(response.body().toForecastDataList());
                    uiStateLiveData.setValue(UiState.SUCCESS);
                } else {
                    Log.w("ForecastViewModel", "Ответ от API пустой или неуспешный");
                    uiStateLiveData.setValue(UiState.ERROR);
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                Log.e("ForecastViewModel", "Ошибка загрузки прогноза", t);
                uiStateLiveData.setValue(UiState.ERROR);
            }
        });
    }

    // Пригодится позже
    public List<ForecastData> getCurrentForecast() {
        return forecastLiveData.getValue();
    }
}


