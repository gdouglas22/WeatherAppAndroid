package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ui.adapter.ForecastPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ForecastFragment extends Fragment {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_forecast_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        List<String> forecastData = new ArrayList<>();
        forecastData.add("Today");
        forecastData.add("Tomorrow");
        forecastData.add("Next 4 Days");



        viewPager2 = requireView().findViewById(R.id.forecastViewPager);
         tabLayout = requireView().findViewById(R.id.tabLayout);


        ForecastPagerAdapter adapter = new ForecastPagerAdapter(getActivity(), forecastData);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Today");
                    break;
                case 1:
                    tab.setText("Tomorrow");
                    break;
                case 2:
                    tab.setText("Next 4 Days");
                    break;
            }
        }).attach();
    }
}

