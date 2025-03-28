package com.example.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ui.viewmodel.forecast.ForecastData;
import com.example.weatherapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.util.Log;

public class ForecastViewPagerAdapter extends RecyclerView.Adapter<ForecastViewPagerAdapter.ForecastViewHolder> {

    private Context ctx;
    private List<String> data = new ArrayList<>(Arrays.asList("Today", "Tomorrow", "Next 4 Days"));

    private List<ForecastData> fullForecastData = new ArrayList<>();

    public void setForecastData(List<ForecastData> forecastData) {
        this.fullForecastData = forecastData;
        notifyDataSetChanged();
    }


    public ForecastViewPagerAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Используем разметку для каждого слайда
        View view = LayoutInflater.from(ctx).inflate(R.layout.forecast_slider_item, parent, false);
        return new ForecastViewHolder(view);
    }

    private List<ForecastData> getForecastCards(int position) {
        List<ForecastData> today = new ArrayList<>();
        List<ForecastData> tomorrow = new ArrayList<>();
        List<ForecastData> nextFourDays = new ArrayList<>();

        if (fullForecastData.isEmpty()) return today;

        SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat compareFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        try {
            Date firstDate = apiFormat.parse(fullForecastData.get(0).getTime());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(firstDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            String todayDate = compareFormat.format(calendar.getTime());

            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String tomorrowDate = compareFormat.format(calendar.getTime());

            for (ForecastData item : fullForecastData) {
                if (today.size() >= 4 && tomorrow.size() >= 4 && nextFourDays.size() >= 4) break;

                try {
                    Date parsed = apiFormat.parse(item.getTime());
                    assert parsed != null;
                    String date = compareFormat.format(parsed);

                    Log.d("ForecastAdapter", "Parsed=" + date + " | Today=" + todayDate + " | Tomorrow=" + tomorrowDate);

                    if (date.equals(todayDate) && today.size() < 4) {
                        today.add(item);
                    } else if (date.equals(tomorrowDate) && tomorrow.size() < 4) {
                        tomorrow.add(item);
                    } else if (nextFourDays.size() < 4) {
                        nextFourDays.add(item);
                    }

                } catch (Exception e) {
                    Log.e("ForecastAdapter", "Parse error", e);
                }
            }

        } catch (Exception e) {
            Log.e("ForecastAdapter", "Critical parse error", e);
        }

        switch (position) {
            case 0:
                return today.subList(0, Math.min(today.size(), 4));
            case 1:
                return tomorrow.subList(0, Math.min(tomorrow.size(), 4));
            case 2:
                // Собираем только по одному элементу для каждого дня, начиная с "послезавтра"
                List<ForecastData> result = new ArrayList<>();
                Set<String> addedDates = new HashSet<>();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                // "Сегодня"
                String todayDate = dayFormat.format(calendar.getTime());

                for (ForecastData item : fullForecastData) {
                    try {
                        Date parsed = apiFormat.parse(item.getTime());
                        String dateStr = dayFormat.format(parsed);
                        String timeStr = item.getTime().split(" ")[1];

                        // Пропускаем "сегодня" и ищем только 00:00
                        if (!dateStr.equals(todayDate) && timeStr.equals("00:00:00") && !addedDates.contains(dateStr)) {
                            result.add(item);
                            addedDates.add(dateStr);
                        }

                        if (result.size() == 4) break;

                    } catch (Exception e) {
                        Log.e("ForecastAdapter", "Next 4 Days parse error", e);
                    }
                }

                return result;

            default:
                return new ArrayList<>();
        }
    }




    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(new ForecastCardAdapter(getForecastCards(position), position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }
}




