package com.example.imt_atlantique.tp4_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int density= getResources().getDisplayMetrics().densityDpi;
        switch(density){
            case DisplayMetrics.DENSITY_LOW:
                Toast.makeText(getApplicationContext(), "LDPI", Toast.LENGTH_LONG).show();
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Toast.makeText(getApplicationContext(), "MDPI", Toast.LENGTH_LONG).show();
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Toast.makeText(getApplicationContext(), "HDPI", Toast.LENGTH_LONG).show();
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                Toast.makeText(getApplicationContext(), "XHDPI", Toast.LENGTH_LONG).show();
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                Toast.makeText(getApplicationContext(), "XXHDPI", Toast.LENGTH_LONG).show();
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                Toast.makeText(getApplicationContext(), "XXXHDPI", Toast.LENGTH_LONG).show();
                break;
        }

    }
}
