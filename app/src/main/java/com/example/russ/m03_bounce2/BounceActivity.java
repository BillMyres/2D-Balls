package com.example.russ.m03_bounce2;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

//import android.support.v7.app.AppCompatActivity;

/**
 * Revised Bouncing Ball example.  Chopped away
 * as much as possible, those bits not needed
 * other than to support old Android versions.
 */
public class BounceActivity extends AppCompatActivity {


    private View bouncingBallView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main_delete);

        // Add Bouncing Ball
        // http://www3.ntu.edu.sg/home/ehchua/programming/android/Android_2D.html
        bouncingBallView = new BouncingBallView(this);
        setContentView(bouncingBallView);
        Log.v("SENSORS", "onCreate bouncingBallView=" + bouncingBallView.toString());


        //Check sensors...what difference does this line make?
        //setupSensors();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (bouncingBallView!=null) {
            Log.v("SENSORS", "onResume bouncingBallView=" + bouncingBallView.toString());
            if (my_Sensor !=null) {
                Log.v("SENSORS", "onResume my_Sensor=" + my_Sensor.toString());
                mSensorManager.registerListener((SensorEventListener) bouncingBallView, my_Sensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        } else {
            Log.v("SENSORS", "onResume bouncingBallView=null");
        }
        Log.v("SENSORS", "onResume ACCELLEROMETER" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener((SensorEventListener) bouncingBallView);
        Log.v("SENSORS", "onPause ACCELLEROMETER" );
    }


    // Sensors
    private SensorManager mSensorManager;
    private Sensor my_Sensor;
    List<Sensor> deviceSensors = new ArrayList<Sensor>();  // Declare List with specific contents

    private void setupSensors() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        Log.v("SENSORS", "Sensor List=" + deviceSensors.toString());

        // Use the accelerometer.
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            my_Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            //my_Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            Log.v("SENSORS", "my_Sensor=" + my_Sensor.toString() );
        }
        else{
            // Sorry, there are no accelerometers on your device.
            // You can't play this game.
            Log.v("SENSORS", "NO SENSOR TYPE?" );
        }

    }

}
