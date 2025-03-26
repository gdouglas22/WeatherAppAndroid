package com.example.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;

import java.util.Arrays;
import java.util.List;

public class ForecastPagerAdapter extends RecyclerView.Adapter<ForecastPagerAdapter.ForecastViewHolder> {

    private List<String> data; // Данные будут передаваться извне
    private Context ctx;

    // Конструктор адаптера
    public ForecastPagerAdapter(Context ctx, List<String> data) {
        this.ctx = ctx;
        this.data = data;
    }

    // This method returns our layout
    @NonNull
    @Override
    public ForecastPagerAdapter.ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.forecast_card_in_slider, parent, false);
        return new ForecastPagerAdapter.ForecastViewHolder(view);
    }

    // This method binds the screen with the view
    @Override
    public void onBindViewHolder(@NonNull ForecastPagerAdapter.ForecastViewHolder holder, int position) {
        // This will set the images in imageview
        holder.textView.setText(data.get(position));
        holder.emptyText1.setText("empty");
        holder.emptyText2.setText("empty");
        holder.textView2.setText(data.get(position));
        holder.emptyText11.setText("empty");
        holder.emptyText22.setText("empty");
    }

    // This Method returns the size of the Array
    @Override
    public int getItemCount() {
        return data.size();
    }

    // The ViewHolder class holds the view
    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView emptyText1, emptyText2, emptyText3;
        TextView textView2;

        TextView emptyText11, emptyText22, emptyText33;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.dayText);
            emptyText1 = itemView.findViewById(R.id.emptyText1);
            emptyText2 = itemView.findViewById(R.id.emptyText2);
            textView2 = itemView.findViewById(R.id.dayText2);
            emptyText11 = itemView.findViewById(R.id.emptyText12);
            emptyText22 = itemView.findViewById(R.id.emptyText22);
        }
    }
}


