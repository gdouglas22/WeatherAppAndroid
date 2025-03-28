package com.example.weatherapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ui.adapter.ForecastViewPagerAdapter;
import com.example.ui.graph.CustomGraphView;
import com.example.ui.graph.TemperatureGraphManager;
import com.example.ui.viewmodel.common.UiState;
import com.example.ui.viewmodel.forecast.ForecastData;
import com.example.ui.viewmodel.forecast.ForecastViewModel;
import com.example.ui.viewmodel.weather.WeatherViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class ForecastFragment extends Fragment {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private ProgressBar progressBar;
    private ForecastViewPagerAdapter adapter;
    private ForecastViewModel forecastViewModel;
    private CustomGraphView graphView;
    private TemperatureGraphManager graphManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager2 = view.findViewById(R.id.forecastViewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        progressBar = view.findViewById(R.id.forecastLoadingIndicator);
        graphView = (CustomGraphView) view.findViewById(R.id.temperatureGraph);

        forecastViewModel = new ViewModelProvider(this).get(ForecastViewModel.class);
        graphManager = new TemperatureGraphManager(graphView, requireContext());

        observeViewModel();

        WeatherViewModel cityViewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
        cityViewModel.getCityLiveData().observe(getViewLifecycleOwner(), forecastViewModel::loadForecast);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateGraphForPage(position);
            }
        });
    }

    private void updateGraphForPage(int pageIndex) {
        List<ForecastData> allData = forecastViewModel.getCurrentForecast();
        if (allData == null || allData.isEmpty()) return;

        List<ForecastData> dataForGraph;

        switch (pageIndex) {
            case 0:
                dataForGraph = allData.subList(0, Math.min(8, allData.size()));
                break;
            case 1:
                dataForGraph = allData.size() >= 16 ? allData.subList(8, 16) : allData.subList(8, allData.size());
                break;
            case 2:
                dataForGraph = allData.size() > 16 ? allData.subList(16, allData.size()) : List.of();
                break;
            default:
                dataForGraph = List.of();
        }

        graphManager.drawGraph(dataForGraph);
    }

    private void observeViewModel() {
        forecastViewModel.getForecastLiveData().observe(getViewLifecycleOwner(), this::displayForecast);
        forecastViewModel.getUiStateLiveData().observe(getViewLifecycleOwner(), this::handleUiState);
    }

    private void displayForecast(List<ForecastData> forecastList) {
        if (adapter == null) {
            adapter = new ForecastViewPagerAdapter(requireContext());
            viewPager2.setAdapter(adapter);

            new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                switch (position) {
                    case 0: tab.setText("Today"); break;
                    case 1: tab.setText("Tomorrow"); break;
                    case 2: tab.setText("Next 4 Days"); break;
                }
            }).attach();
        }

        adapter.setForecastData(forecastList);
        updateGraphForPage(viewPager2.getCurrentItem());
    }

    private void handleUiState(UiState state) {
        switch (state) {
            case LOADING:
                progressBar.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
            case ERROR:
                progressBar.setVisibility(View.GONE);
                if (state == UiState.ERROR) {
                    Toast.makeText(getContext(), "Ошибка загрузки прогноза", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
