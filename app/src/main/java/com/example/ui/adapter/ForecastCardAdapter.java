package com.example.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ui.viewmodel.forecast.ForecastData;
import com.example.weatherapp.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Date;

public class ForecastCardAdapter extends RecyclerView.Adapter<ForecastCardAdapter.ForecastCardViewHolder> {

    private List<ForecastData> data;

    private int tabPosition;

    public ForecastCardAdapter(List<ForecastData> data, int tabPosition) {
        this.data = data;
        this.tabPosition = tabPosition;
    }


    @NonNull
    @Override
    public ForecastCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_card_in_recyclerview, parent, false);
        return new ForecastCardViewHolder(view);
    }

    private int getIconResForDescription(String description) {
        Log.d("ForecastAdapter", "Description=" + description);
        if (description == null) return R.drawable.ic_sun_cloud;


        if (description.contains("rain") || description.contains("shower")) {
            return R.drawable.ic_cloud_angled_rain_zap;
        } else if (description.contains("storm") || description.contains("thunder")) {
            return R.drawable.cloud_angled_zap;
        } else if (description.contains("wind")) {
            return R.drawable.ic_sun_cloud_wind;
        } else if (description.contains("clear")) {
            return R.drawable.ic_sun_cloud;
        } else if (description.contains("cloud")) {
            return R.drawable.ic_sun_cloud;
        }

        return R.drawable.ic_sun_cloud;
    }


    @Override
    public void onBindViewHolder(@NonNull ForecastCardViewHolder holder, int position) {
        ForecastData item = data.get(position);

        String displayTime;
        String temperature = item.getTemperature() + "°C";

        try {
            SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

            if (tabPosition == 2) { // "Next 4 Days"
                SimpleDateFormat toFormat = new SimpleDateFormat("EEE", Locale.US); // например: Mon, Tue...
                Date parsed = fromFormat.parse(item.getTime());
                displayTime = toFormat.format(parsed);
            } else {
                SimpleDateFormat toFormat = new SimpleDateFormat("HH:mm", Locale.US);
                Date parsed = fromFormat.parse(item.getTime());
                displayTime = toFormat.format(parsed);
            }

        } catch (Exception e) {
            displayTime = "N/A";
        }

        holder.textView1.setText(displayTime);      // либо HH:mm, либо день недели
        holder.textView2.setText(temperature);       // температура
        holder.imageView.setImageResource(getIconResForDescription(item.getDescription()));
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ForecastCardViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;
        ImageView imageView;

        public ForecastCardViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}


