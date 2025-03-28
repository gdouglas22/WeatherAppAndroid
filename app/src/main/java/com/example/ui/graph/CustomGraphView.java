package com.example.ui.graph;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.jjoe64.graphview.GraphView;

public class CustomGraphView extends GraphView {
    private float lastTouchX = 0;
    private float lastTouchY = 0;

    public CustomGraphView(Context context) {
        super(context);
    }

    public CustomGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN ||
                event.getAction() == MotionEvent.ACTION_MOVE ||
                event.getAction() == MotionEvent.ACTION_UP) {
            lastTouchX = event.getRawX();
            lastTouchY = event.getRawY();
        }
        return super.onTouchEvent(event);
    }

    public float getLastTouchX() {
        return lastTouchX;
    }

    public float getLastTouchY() {
        return lastTouchY;
    }
}

