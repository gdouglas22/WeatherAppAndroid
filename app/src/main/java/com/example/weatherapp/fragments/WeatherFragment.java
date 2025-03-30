package com.example.weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ui.viewmodel.common.UiState;
import com.example.ui.viewmodel.weather.WeatherData;
import com.example.ui.viewmodel.weather.WeatherViewModel;
import com.example.util.PrefsHelper;
import com.example.weatherapp.databinding.FragmentWeatherFragmentBinding;

public class WeatherFragment extends Fragment {

    private FragmentWeatherFragmentBinding binding;
    private WeatherViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWeatherFragmentBinding.inflate(inflater, container, false);
        binding.loadingIndicator.setVisibility(View.VISIBLE);
        binding.contentLayout.setVisibility(View.GONE);

        viewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);

        observeViewModel();

        String savedCity = PrefsHelper.getSavedCity(requireContext());
        if (savedCity != null && !savedCity.isEmpty()) {
            viewModel.setCity(savedCity);
        }

        return binding.getRoot();
    }


    private void observeViewModel() {
        viewModel.getWeatherLiveData().observe(getViewLifecycleOwner(), this::updateUIWithWeather);
        viewModel.getUiStateLiveData().observe(getViewLifecycleOwner(), this::handleUiState);
    }

    private void updateUIWithWeather(WeatherData weatherData) {
        binding.setWeatherData(weatherData);
    }


    private void handleUiState(UiState state) {
        switch (state) {
            case LOADING:
                binding.loadingIndicator.setVisibility(View.VISIBLE);
                binding.contentLayout.setVisibility(View.GONE);
                break;
            case SUCCESS:
                binding.loadingIndicator.setVisibility(View.GONE);
                binding.contentLayout.setVisibility(View.VISIBLE);
                break;
            case ERROR:
                binding.loadingIndicator.setVisibility(View.GONE);
                binding.contentLayout.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

