package com.example.imt_atlantique.tp1final;


//region Importations des librairies

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.io.File;


//endregion

//region Activité principale

public class MainActivity extends AppCompatActivity {

    //region  Variables globales
    private static int I_nbClics = 0;
    private static String S_text = " Clic", S_Key = "SavedClics";
    private static Button B_Compteur;

    //endregion

    //region  Méthode onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final File dataDirectory = Environment.getDataDirectory();
        Log.i("Lifecycle Chemin data", dataDirectory.getPath());

        // L'application (re)demarre
        if (savedInstanceState==null){
            SharedPreferences preferences = getSharedPreferences("NOMBRE_CLICS", MODE_PRIVATE);
            I_nbClics = preferences.getInt(S_Key,0);
            if (I_nbClics > 1) {
                S_text = " Clics";
            }
        }


        B_Compteur = findViewById(R.id.btnCompteur);
        B_Compteur.setText(String.valueOf(I_nbClics) + S_text);
        B_Compteur.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                I_nbClics++;
                if (I_nbClics > 1){
                    S_text = " Clics";
                }
                B_Compteur.setText(String.valueOf(I_nbClics) + S_text);
            }
        });
    }

    //endregion

    //region methodes de Gestion d'état

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(S_Key,I_nbClics);
    }


    @Override
    protected void onStop() {
        super.onStop();

        //Sauvegarde des données de l'activité
        SharedPreferences preferences= getSharedPreferences("NOMBRE_CLICS", MODE_PRIVATE);
        SharedPreferences.Editor editorPref = preferences.edit();
        editorPref.putInt(S_Key,I_nbClics);
        editorPref.apply();

        Log.i("Lifecycle", "onStop method");
    }

    //endregion

    //region Autres methodes de Callback
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Lifecycle", "onDestroy method");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Lifecycle", "onRestart method");
    }

    @Override // Que pour les methodes a redefinir
    protected void onPause() {
        super.onPause();
        Log.i("Lifecycle", "onPause method");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Lifecycle", "onStart method");
    }
    //endregion

    //region Gestion du menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("TEST MENU", "Je suis dans le menu");
        getMenuInflater().inflate(R.menu.menu_compteur, menu);
        return true;
    }

    public void redemarrerCompteur(MenuItem item) {
        I_nbClics = 0;
        S_text = " Clic";
        B_Compteur = findViewById(R.id.btnCompteur);
        B_Compteur.setText(String.valueOf(I_nbClics) + S_text);
    }

    //endregion


}

//endregion


