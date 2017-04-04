package com.example.hellosensor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Help used:
 * http://stackoverflow.com/questions/14477133/how-to-use-android-sensor-event-to-determine-if-device-is-facing-up-or-down
 * https://www.built.io/blog/applying-low-pass-filter-to-android-sensor-s-readings
 * http://www.techrepublic.com/article/pro-tip-create-your-own-magnetic-compass-using-androids-internal-sensors/
 * https://developer.android.com/reference/android/hardware/SensorManager.html
 * https://developer.android.com/reference/android/hardware/SensorEvent.html
 * http://stackoverflow.com/questions/29509010/how-to-play-a-short-beep-to-android-phones-loudspeaker-programmatically
 * http://stackoverflow.com/questions/13950338/how-to-make-an-android-device-vibrate
 */

public class MainActivity extends AppCompatActivity {
    static final float ALPHA = 0.10f; // if ALPHA = 1 OR 0, no filter applies.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Send button */
    public void goToAccelerometer(View view){
        Intent intent = new Intent(this, AccelerometerActivity.class);
        startActivity(intent);
    }

    public void goToCompass(View view){
        Intent intent = new Intent(this, CompassActivity.class);
        startActivity(intent);
    }

    public static  float[] lowPass(float[] input, float[] output){
        if ( output == null ) return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }



}
