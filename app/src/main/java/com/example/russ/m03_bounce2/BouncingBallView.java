package com.example.russ.m03_bounce2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Random;

/**
 * Created by Russ on 08/04/2014.
 */
public class BouncingBallView extends View implements SensorEventListener {

    public static ArrayList<Ball> balls = new ArrayList<Ball>(); // list of Balls
    public static int points = 0;
    private Ball ball_1;  // use this to reference first ball in arraylist
    private Box box;

    // Status message to show Ball's (x,y) position and speed.
    private StringBuilder statusMsg = new StringBuilder();
    private Formatter formatter = new Formatter(statusMsg);
    private Paint paint;

    private int string_line = 1;  //
    private int string_x = 10;
    private int string_line_size = 40;  // pixels to move down one line
    private ArrayList<String> debug_dump1 = new ArrayList();
    private String[] debug_dump2 = new String[200];

    // For touch inputs - previous touch (x, y)
    private float previousX;
    private float previousY;

    // Used to calculate delta-time between frames
    private int target_fps = 45;
    private long last_frame = System.nanoTime();

    // Log details
    private Paint black_paint = new Paint();
    private TextPaint log_paint = new TextPaint();

    // Canvas color
    private int canvas_color = functions.randomColor();

    // Constructor
    public BouncingBallView(Context context) {
        super(context);

        // Init the array
        for (int i = 1; i < 200; i++) {
            debug_dump2[i] = "  ";
        }

        // create the box with random color
        box = new Box(canvas_color);  // ARGB

        balls.add(new Ball());
        ball_1 = balls.get(0);  // points ball_1 to the first; (zero-ith) element of list
        Log.w("BouncingBallLog", "Just added a bouncing ball");

        balls.add(new Ball());
        Log.w("BouncingBallLog", "Just added another bouncing ball");

        // Set up status message on paint object
        paint = new Paint();

        // Set the font face and size of drawing text
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(320);
        paint.setColor(Color.CYAN);

        // To enable keypad
        this.setFocusable(true);
        this.requestFocus();
        // To enable touch mode
        this.setFocusableInTouchMode(true);

        // Create log_paint
        black_paint.setColor(functions.color(0, 0, 0, 175));// Transparent black
        log_paint.setAntiAlias(true);
        log_paint.setTextSize(20);
        log_paint.setColor(functions.color(0, 255, 0, 175));// Transparent green
        log_paint.setTypeface(Typeface.MONOSPACE);

        Log.i("MOBI3002", "BouncingBallView()");
    }

    // Called back to draw the view. Also called after invalidate().
    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the components
        box.draw(canvas);
        canvas.drawColor(canvas_color);

        for (Ball b : balls) {
            b.draw(canvas);  //draw each ball in the list
            b.moveWithCollisionDetection(box);  // Update the position of the ball
        }

        // inc-rotate string_line
        if (string_line * string_line_size > box.yMax) {
            string_line = 1;  // first line is status
            debug_dump1.clear();
        } else {
            string_line++;
        }

        // inc-rotate string_x
        if (string_x > box.xMax) {
            string_x = 10;  // first line is status
        } else {
            string_x++;
        }

        // Calculate delta-time
        long now = System.nanoTime();
        int delta = (int)((now - last_frame) / 1000000.0);
        int target_ms = 1000 / target_fps;
        int sleep_time = target_ms - delta;

        last_frame = now;

        // Log results to canvas
        canvas.drawRect(0, 1, 220, 220, black_paint);

        canvas.drawText("target delta: " + target_ms, 10, 50, log_paint);
        canvas.drawText("delta       : " + delta, 10, 75, log_paint);
        canvas.drawText("sleep (ms)  : " + sleep_time, 10, 100, log_paint);
        canvas.drawText("target FPS  : " + target_fps, 10, 125, log_paint);
        canvas.drawText("FPS         : " + (int)(1000.0 / delta), 10, 150, log_paint);
        canvas.drawText("Points      : " + points, 10, 200, log_paint);

        // Sleep to ensure target_fps is achieved
        if(sleep_time > 0)
            try {
                Thread.sleep(sleep_time);
            } catch (Exception ignore){}

        this.invalidate();
    }

    // Called back when the view is first created or its size changes.
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Set the movement bounds for the ball
        box.set(0, 0, w, h);
        Log.w("BouncingBallLog", "onSizeChanged w=" + w + " h=" + h);
    }


    // Touch-input handler
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        float deltaX, deltaY;
        float scalingFactor = 5.0f / ((box.xMax > box.yMax) ? box.yMax : box.xMax);

        if(event.getAction() == MotionEvent.ACTION_MOVE){
            // Modify rotational angles according to movement
            deltaX = currentX - previousX;
            deltaY = currentY - previousY;
            ball_1.speedX += deltaX * scalingFactor;
            ball_1.speedY += deltaY * scalingFactor;
            //Log.w("BouncingBallLog", " Xspeed=" + ball_1.speedX + " Yspeed=" + ball_1.speedY);
            Log.w("MOBI3002", "x,y= " + previousX + " ," + previousY + "  Xdiff=" + deltaX + " Ydiff=" + deltaY);

            // Add squares when delta > 30 circles otherwise
            Ball ball;

            if(Math.abs(deltaX) > 30 || Math.abs(deltaY) > 30)
                ball = new Square(previousX, previousY, deltaX, deltaY);
            else
                ball = new Rectangle(previousX, previousY, deltaX, deltaY);

            balls.add(ball);  // add ball at every touch event

            // A way to clear list when too many balls
            if (balls.size() > 20) {
                // leave first ball, remove the rest
                Log.v("BouncingBallLog", "too many balls, clear back to 1");
                balls.clear();
                balls.add(new Ball());
                ball_1 = balls.get(0);  // points ball_1 to the first (zero-ith) element of list
            }

        }else{
            canvas_color = functions.randomColor();
        }


        // Save current x, y
        previousX = currentX;
        previousY = currentY;

        // Try this (remove other invalidate(); )
        //this.invalidate();

        return true;  // Event handled
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        Log.v("onSensorChanged", "event=" + event.toString());

        // Lots of sensor types...get which one, unpack accordingly
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double ax = event.values[0];
            double ay = event.values[1];
            double az = event.values[2];

            for (Ball b : balls) {
                b.setAcc(ax / 3, ay / 3, az / 3);  //acc for each ball
            }

            Log.v("onSensorChanged", "ax=" + ax + " ay=" + ay + " az=" + az);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.v("onAccuracyChanged", "event=" + sensor.toString());
    }

}
