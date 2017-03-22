package com.example.hellosensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Vibrator;

import java.io.Console;

public class CompassActivity extends AppCompatActivity implements SensorEventListener{

    private ImageView mPointer;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    private long saveTime;
    private long saveTime2;
    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    ToneGenerator toneGen2 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        setContentView(R.layout.activity_compass);
        mPointer = (ImageView) findViewById(R.id.pointer);
        if(mPointer == null){
            throw new NullPointerException("BREH, WHY?");
        }
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree,
                    -azimuthInDegress,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            ra.setDuration(250);

            ra.setFillAfter(true);

            mPointer.startAnimation(ra);
            mCurrentDegree = -azimuthInDegress;
            ((TextView) findViewById(R.id.angleTextView)).setText(String.valueOf(mCurrentDegree));
            if((mCurrentDegree >= (-45.0) || mCurrentDegree <= (-315.0f)) && 1000 < System.currentTimeMillis()-saveTime2){
                saveTime2 = System.currentTimeMillis();
                toneGen2.stopTone();
                toneGen2.startTone(ToneGenerator.TONE_CDMA_PIP, 500);
            } else if ((mCurrentDegree >= (-15.0) || mCurrentDegree <= (-345.0f)) && 100 < System.currentTimeMillis()-saveTime2){
                saveTime2 = System.currentTimeMillis();
                toneGen2.stopTone();
                toneGen2.startTone(ToneGenerator.TONE_CDMA_PIP, 100);
            }

            float[] rotationMatrix = new float[9];
            float[] inclinationMatrix = new float[9];
            SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, mLastAccelerometer, mLastMagnetometer);
            int inclination = (int) Math.round(Math.toDegrees(Math.acos(rotationMatrix[8])));

            if (inclination < 175)
            {
                // face up
            }

            if (inclination > 175)
            {
                if(1000 < (System.currentTimeMillis()-saveTime)) {
                    saveTime = System.currentTimeMillis();
                    Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    v.vibrate(100);

                    //toneGen1.stopTone();
                    //toneGen1.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 1000);
                }
            }
                // face down
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        
    }

}
