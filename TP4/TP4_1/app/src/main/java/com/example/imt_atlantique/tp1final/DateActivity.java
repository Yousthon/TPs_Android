package com.example.imt_atlantique.tp1final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;


public class DateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        String date = getIntent().getStringExtra("tadate");
        DatePicker samedate = findViewById(R.id.datePicker);

        //Recuperation Date et initialisation du datePicker
        if(isInteger(date)) {
            samedate.init(Integer.parseInt(date.substring(4, 8)),
                    Integer.parseInt(date.substring(2, 4))-1,
                    Integer.parseInt(date.substring(0, 2)), null);
        }
        else
            samedate.init(2018,01,01, null);
    }

    //Verifier si integer
    public boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

    public void okdate(View view){
        DatePicker chosen = findViewById(R.id.datePicker);
        String chosenday = String.valueOf(chosen.getDayOfMonth()),
                chosenmonth = String.valueOf(chosen.getMonth()+1),
                chosenyear = String.valueOf(chosen.getYear());

        if(chosen.getMonth()+1 < 10)
            chosenmonth = "0" + chosenmonth;
        if(chosen.getDayOfMonth()< 10)
            chosenday  = "0" + chosenday ;

        String alldate = new String(chosenday+"/"+chosenmonth+"/"+chosenyear);
        Intent datewritten = new Intent(getApplicationContext(), MainActivity.class);
        datewritten.putExtra("FINAL", alldate);
        setResult(RESULT_OK,datewritten);
        finish();
    }

    public void notneed(View view){
        String alldate = new String("");
        Intent datewritten = new Intent(getApplicationContext(), MainActivity.class);
        datewritten.putExtra("FINALLY", alldate);
        setResult(RESULT_CANCELED,datewritten);
        finish();
    }
    }

