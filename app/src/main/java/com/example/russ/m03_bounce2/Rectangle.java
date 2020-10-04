package com.example.russ.m03_bounce2;

import android.graphics.Canvas;
import android.util.Log;

public class Rectangle extends Ball {

//    private static int points = 0;

    public Rectangle(float x, float y, float vx, float vy){
        super(x, y, vx, vy);
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawRect(
            x - radius * 1.2f,
            y - radius * 0.8f,
            x + radius * 1.2f,
            y + radius * 0.8f,
            paint
        );
    }

    @Override
    public void collision(){
        BouncingBallView.points++;
        Log.i("MOBI3002", "Points: " + BouncingBallView.points);
    }
//
//    /** Get the number of points accumulated*/
//    public static int getPoints(){
//        return points;
//    }
}
