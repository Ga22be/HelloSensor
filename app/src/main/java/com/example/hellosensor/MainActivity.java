package com.example.hellosensor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    static final float ALPHA = 0.75f; // if ALPHA = 1 OR 0, no filter applies.
    public static String EXTRA_MESSAGE = "something";

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
