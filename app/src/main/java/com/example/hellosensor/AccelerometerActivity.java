package com.example.hellosensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magneto;
    private float mLastAccelerometer[] = new float[3];
    private float mLastMagnetometer[] = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float gravity[];
    private float linear_acceleration[] = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneto = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        setContentView(R.layout.activity_accelerometer);
        if(accelerometer == null){
            throw new NullPointerException("WTF, IT IS EMPTY! DOES THIS NOT WORK IN EMULATOR?");
        }

    }

    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magneto, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == accelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == magneto) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }

        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        final float alpha = (float) 0.8;

        //gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        //gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        //gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        linear_acceleration[0] = event.values[0];// - gravity[0];
        linear_acceleration[1] = event.values[1];// - gravity[1];
        linear_acceleration[2] = event.values[2];// - gravity[2];

        ((TextView) findViewById(R.id.xTextView)).setText(String.valueOf(linear_acceleration[0]));
        ((TextView) findViewById(R.id.yTextView)).setText(String.valueOf(linear_acceleration[1]));
        ((TextView) findViewById(R.id.zTextView)).setText(String.valueOf(linear_acceleration[2]));

        float[] rotationMatrix = new float[9];
        float[] inclinationMatrix = new float[9];
        SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, mLastAccelerometer, mLastMagnetometer);
        int inclination = (int) Math.round(Math.toDegrees(Math.acos(rotationMatrix[6])));
        int inclination2 = (int) Math.round(Math.toDegrees(Math.acos(rotationMatrix[8])));

        ((TextView) findViewById(R.id.inclineTextView)).setText(String.valueOf(inclination-90) + "°");
        ((TextView) findViewById(R.id.inclineTextView2)).setText(String.valueOf(inclination2) + "°");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
