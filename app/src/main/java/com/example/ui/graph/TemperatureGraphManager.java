package com.example.ui.graph;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.ui.graph.CustomGraphView;
import com.example.weatherapp.R;
import com.example.ui.viewmodel.forecast.ForecastData;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

public class TemperatureGraphManager {

    private final CustomGraphView graphView;
    private final Context context;
    private PopupWindow currentTooltip;

    public TemperatureGraphManager(CustomGraphView graphView, Context context) {
        this.graphView = graphView;
        this.context = context;
    }

    public void drawGraph(List<ForecastData> forecastList) {
        graphView.removeAllSeries();
        if (forecastList == null || forecastList.isEmpty()) return;

        DataPoint[] points = new DataPoint[forecastList.size()];
        for (int i = 0; i < forecastList.size(); i++) {
            double temp = parseTemperature(forecastList.get(i).getTemperature());
            points[i] = new DataPoint(i, temp);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        series.setColor(Color.parseColor("#222222"));
        series.setThickness(5);
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.parseColor("#11CCCCCC"));
        series.setDrawDataPoints(false);

        series.setOnDataPointTapListener((s, dataPoint) -> {
            int index = (int) dataPoint.getX();
            if (index >= 0 && index < forecastList.size()) {
                float x = graphView.getLastTouchX();
                float y = graphView.getLastTouchY();
                showTooltip(x, y, (DataPoint) dataPoint, forecastList.get(index));
            }
        });

        graphView.addSeries(series);

        GridLabelRenderer grid = graphView.getGridLabelRenderer();
        grid.setHorizontalLabelsVisible(false);
        grid.setVerticalLabelsVisible(false);
        grid.setGridStyle(GridLabelRenderer.GridStyle.NONE);
        grid.setPadding(0);

        graphView.getViewport().setScalable(false);
        graphView.getViewport().setScrollable(false);
        graphView.getViewport().setYAxisBoundsManual(false);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(points.length - 1);
        graphView.setPadding(0, 0, 0, 0);
        graphView.setBackgroundColor(Color.TRANSPARENT);
    }

    private void showTooltip(float rawX, float rawY, DataPoint point, ForecastData data) {
        if (currentTooltip != null && currentTooltip.isShowing()) {
            currentTooltip.dismiss();
            currentTooltip = null;
        }

        View popupView = LayoutInflater.from(context).inflate(R.layout.tooltip_view, null);
        TextView timeText = popupView.findViewById(R.id.tooltip_time);
        TextView tempText = popupView.findViewById(R.id.tooltip_temp);
        timeText.setText(data.getTime());
        tempText.setText(data.getTemperature());

        List<com.jjoe64.graphview.series.Series<?>> toRemove = new ArrayList<>();
        for (com.jjoe64.graphview.series.Series<?> s : graphView.getSeries()) {
            if ("selected_point".equals(s.getTitle())) {
                toRemove.add(s);
            }
        }

        LineGraphSeries<DataPoint> dot = new LineGraphSeries<>(new DataPoint[]{point});
        dot.setTitle("selected_point");
        dot.setDrawDataPoints(true);
        dot.setDataPointsRadius(20f);
        dot.setThickness(5);
        dot.setColor(Color.BLACK);

        graphView.post(() -> {
            for (com.jjoe64.graphview.series.Series<?> s : toRemove) {
                graphView.removeSeries(s);
            }
            graphView.addSeries(dot);
        });

        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        PopupWindow popup = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                false);
        popup.setOutsideTouchable(true);
        popup.setFocusable(false);
        currentTooltip = popup;

        int popupX = (int) rawX - (popupView.getMeasuredWidth() / 2);
        int popupY = (int) rawY - popupView.getMeasuredHeight() - 30;

        popup.showAtLocation(graphView, Gravity.NO_GRAVITY, popupX, popupY);
        animateIn(popupView);

        popupView.postDelayed(() -> animateOut(popup, popupView), 3000);
    }

    private void animateIn(View popupView) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(popupView, "alpha", 0f, 1f);
        ObjectAnimator translate = ObjectAnimator.ofFloat(popupView, "translationY", 50f, 0f);
        alpha.setDuration(300);
        translate.setDuration(300);
        alpha.start();
        translate.start();
    }

    private void animateOut(PopupWindow popup, View popupView) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(popupView, "alpha", 1f, 0f);
        ObjectAnimator translate = ObjectAnimator.ofFloat(popupView, "translationY", 0f, -50f);
        alpha.setDuration(300);
        translate.setDuration(300);
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (popup != null && popup.isShowing()) popup.dismiss();
                if (popup == currentTooltip) currentTooltip = null;
            }
        });
        alpha.start();
        translate.start();
    }

    private double parseTemperature(String tempStr) {
        try {
            return Double.parseDouble(tempStr.replace("°", "").trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

