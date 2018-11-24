package com.example.imt_atlantique.tp1final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class DialNumber extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial_number);

        ArrayList<String> L_Numeros;
        //désérialisation et recuperation de notre arraylist
        L_Numeros = getIntent().getStringArrayListExtra("liste_Num");

        if (L_Numeros.size() > 0) {
            PhoneListViewAdapter adapter = new PhoneListViewAdapter(L_Numeros, getApplicationContext());
            //handle listview and assign adapter
            ListView listPhone = findViewById(R.id.listNum);
            listPhone.setAdapter(adapter);
        } else {
            TextView text = findViewById(R.id.Text_Dial_Number);
            text.setText(R.string.Num_Tel_Saisi);
        }
    }
}
