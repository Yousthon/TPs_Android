package com.example.imt_atlantique.tp1final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class  VoirNom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voir_nom);

        TextView NOM = findViewById(R.id.textView1);

        Intent intent = getIntent();
        String name = intent.getStringExtra("NOM");
        NOM.setText(name);
    }

}

