package com.example.russ.m03_bounce2;

import android.graphics.Canvas;

public class Square extends Ball {

    public Square(float x, float y, float vx, float vy){
        super(x, y, vx, vy);
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawRect(
            x - radius,
            y - radius,
            x + radius,
            y + radius,
            paint
        );
    }
}
