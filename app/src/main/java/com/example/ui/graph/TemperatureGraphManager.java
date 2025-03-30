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

    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }
    public void drawGraph(List<ForecastData> forecastList) {
        graphView.removeAllSeries();
        if (forecastList == null || forecastList.isEmpty()) return;

        List<DataPoint> smoothedPoints = new ArrayList<>();

        for (int i = 0; i < forecastList.size() - 1; i++) {
            double x1 = i;
            double y1 = parseTemperature(forecastList.get(i).getTemperature());
            double x2 = i + 1;
            double y2 = parseTemperature(forecastList.get(i + 1).getTemperature());

            smoothedPoints.add(new DataPoint(x1, y1));

            for (int j = 1; j <= 3; j++) {
                double t = j / 4.0;
                double x = x1 + t;
                double y = (1 - t) * y1 + t * y2;
                smoothedPoints.add(new DataPoint(x, y));
            }
        }

        double lastX = forecastList.size() - 1;
        double lastY = parseTemperature(forecastList.get(forecastList.size() - 1).getTemperature());
        smoothedPoints.add(new DataPoint(lastX, lastY));

        DataPoint[] points = smoothedPoints.toArray(new DataPoint[0]);

        int lineColor = context.getColor(R.color.text_primary);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        series.setColor(lineColor);
        series.setBackgroundColor(adjustAlpha(lineColor, 0.2f));
        series.setColor(Color.parseColor("#222222"));
        series.setThickness(5);
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.parseColor("#66CCCCCC"));
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
        graphView.getViewport().setMaxX(forecastList.size() - 1);
        graphView.setPadding(0, 0, 0, 0);
    }

    private void showTooltip(float rawX, float rawY, DataPoint point, ForecastData data) {
        if (currentTooltip != null && currentTooltip.isShowing()) {
            currentTooltip.dismiss();
            currentTooltip = null;
        }

        View popupView = LayoutInflater.from(context).inflate(R.layout.tooltip_view, null);
        TextView timeText = popupView.findViewById(R.id.tooltip_time);
        TextView tempText = popupView.findViewById(R.id.tooltip_temp);
        timeText.setText(com.example.weatherapp.util.DateUtils.formatDate(data.getTime()));
        tempText.setText(com.example.weatherapp.util.DateUtils.formatTemperature(data.getTemperature()));

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
        int lineColor = context.getColor(R.color.text_primary);
        dot.setColor(lineColor);

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

