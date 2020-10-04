package com.example.russ.m03_bounce2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.Random;


/**
 * Created by Russ on 08/04/2014.
 */
public class Ball {

    float radius = 50;      // Ball's radius
    float x;                // Ball's center (x,y)
    float y;
    float speedX;           // Ball's speed
    float speedY;
    protected RectF bounds;   // Needed for Canvas.drawOval
    protected Paint paint;    // The paint style, color used for drawing

    // Add accelerometer
    // Add ... implements SensorEventListener
    private double ax, ay, az = 0; // acceration from different axis

    public void setAcc(double ax, double ay, double az){
        this.ax = ax;
        this.ay = ay;
        this.az = az;
    }

    Random r = new Random();  // seed random number generator

    // Create a ball of random color
    public Ball(){ this(functions.randomColor()); }
    public Ball(float x, float y, float vx, float vy){
        this(functions.randomColor(), x, y, vx, vy);
    }

    public Ball(int color) {
        bounds = new RectF();
        paint = new Paint();
        paint.setColor(color);

        // random position and speed
        x = radius + r.nextInt(800);
        y = radius + r.nextInt(800);
        speedX = r.nextInt(10) - 5;
        speedY = r.nextInt(10) - 5;
    }

    // Constructor
    public Ball(int color, float x, float y, float speedX, float speedY) {
        bounds = new RectF();
        paint = new Paint();
        paint.setColor(color);

        // use parameter position and speed
        this.x = x;
        this.y = y;
//        this.speedX = speedX;
//        this.speedY = speedY;

        boolean slow = Math.random() < 0.5f;
        float top_speed = (float) Math.random() * 50f;
        float min_speed = (float) Math.random();

        this.speedX = (slow ? min_speed : top_speed);
        this.speedY = (slow ? min_speed : top_speed);
    }

    public void moveWithCollisionDetection(Box box) {
        // Get new (x,y) position
        x += speedX;
        y += speedY;

        // Add acceleration to speed
        speedX += ax;
        speedY += ay;

        // Detect collision and react
        if (x + radius > box.xMax) {
            speedX = -speedX;
            x = box.xMax - radius;
//            collision();
        } else if (x - radius < box.xMin) {
            speedX = -speedX;
            x = box.xMin + radius;
//            collision();
        }
        if (y + radius > box.yMax) {
            speedY = -speedY;
            y = box.yMax - radius;
//            collision();
        } else if (y - radius < box.yMin) {
            speedY = -speedY;
            y = box.yMin + radius;
//            collision();
        }

        // Collide with other balls
        for(Ball ball : BouncingBallView.balls){

            if(ball != this){
                float xdiff = x - ball.x;
                float ydiff = y - ball.y;

                if(Math.abs(xdiff) < radius && Math.abs(ydiff) < radius){
//                    Log.i("MOBI3002", "close: " + Math.abs(x - ball.x));
                    x += xdiff * 1.1f;
                    y += ydiff * 1.1f;

                    speedX *= -1;
                    speedY *= -1;

                    collision();
                }
            }
        }
    }

    public void collision(){
    }

    public void draw(Canvas canvas) {
        bounds.set(x - radius, y - radius, x + radius, y + radius);
        canvas.drawOval(bounds, paint);
    }

}
