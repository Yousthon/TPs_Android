package com.example.imt_atlantique.tp1final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Intent intent = getIntent();

        if (intent != null) {
            User user = intent.getParcelableExtra("user");

            if (user != null) {
                TextView nom = findViewById(R.id.Text_Nom);
                nom.setText(user.nomUser);

                TextView prenom = findViewById(R.id.Text_Prenom);
                prenom.setText(user.prenomUser);

                TextView date = findViewById(R.id.Text_Date);
                date.setText(user.dateUser);

                TextView ville = findViewById(R.id.Text_Ville);
                ville.setText(user.villeUser);

                TextView department = findViewById(R.id.Text_Depart);
                department.setText(user.departUser);

                //les numéros

                if(user.numero != null )
                    if (user.numero.size() > 0) {
                        PhoneListViewAdapter adapter = new PhoneListViewAdapter(user.numero, getApplicationContext());
                        //handle listview and assign adapter
                        ListView listPhone = findViewById(R.id.list_Num);
                        listPhone.setAdapter(adapter);
                        setListViewHeightBasedOnChildren(listPhone);
                    } else {
                        TextView text = findViewById(R.id.Text_Num);
                        text.setText(R.string.Num_Tel_Saisi);
                    }
            }
        }
    }

    //Pour étendre notre ListView
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}