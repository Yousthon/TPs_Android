package com.example.imt_atlantique.myfirstapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // R= ressources générées à partir des pts xml


        // Spinner pour la ville
        final Spinner spVilleNaiss = (Spinner) findViewById(R.id.SpinnerVilleNaiss);
         // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.villeNaiss, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spVilleNaiss.setAdapter(adapter);


        // Recuperation des textes des variables
        final EditText nom = findViewById(R.id.editTextNom);
        final EditText prenom = findViewById(R.id.editTextPrenom);
       // final EditText dateNaiss = findViewById(R.id.editTextDateNaiss);
        final Button btVal = findViewById(R.id.btnValidate);

        // Action du bouton
        btVal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code après appuis sur le btn

                Toast.makeText(getApplicationContext(), " "+ nom.getText().toString() +" "+ prenom.getText().toString()
                        +" "+ spVilleNaiss.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
        });

        Log.i("Lifecycle", "onCreate method");
    }

}
