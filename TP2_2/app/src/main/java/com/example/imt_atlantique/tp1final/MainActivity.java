package com.example.imt_atlantique.tp1final;


//region Importations des librairies

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Layout;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


//endregion


//region Activité principale

public class MainActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    //region  Recuperation des champs dans des variables globales

    //Nbre de fois utilisation application
    private static int I_Nbre=0, I_val=0;
    private List<String> L_numPhone;

    // Spinner pour les villes de naissance
    private static Spinner spVilleNaiss, spDepartement;
    // Recuperation des textes des variables
    private static EditText T_nom;
    private static EditText T_prenom;
    private static EditText T_DateNaiss;
    private static Button B_Valider, B_Supprimer, B_AddNumber;
    private static LinearLayout layoutmain, l, layoutbtn;

    //endregion

    //region  Méthode onCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region  Recuperation des champs dans des variables globales
        // Spinner pour les villes et departements de naissance
        spDepartement = (Spinner) findViewById(R.id.SpinnerDepartement);
        spVilleNaiss = (Spinner) findViewById(R.id.SpinnerVilleNaiss);
        // Recuperation des textes des variables
        T_nom = findViewById(R.id.T_Nom);
        T_prenom = findViewById(R.id.T_Prenom);
        T_DateNaiss = findViewById(R.id.T_Date);
        B_Valider = findViewById(R.id.btnValidate);
        B_AddNumber = findViewById(R.id.btnAddNumber);


        //Restauration des valeurs
        if (savedInstanceState == null) {
            SharedPreferences preferences = getSharedPreferences("DATA_SAVED", MODE_PRIVATE);
            Log.i("Lifecycle", "methode onCreate prob");

            I_Nbre = preferences.getInt("Nombre_Champ_Tel", 0);
            I_val = I_Nbre;

            layoutmain = findViewById(R.id.myLinearMain);
            int childCountnew = layoutmain.getChildCount();

            for (int i = 0; i < childCountnew; i++) {
                ViewGroup v = (ViewGroup) layoutmain.getChildAt(i);
                int nbrElem = v.getChildCount();

                for (int j = 0; j < nbrElem; j++)  {
                    View v1 =  v.getChildAt(j);

                    //Cas EditText
                    if (v1 instanceof EditText) {
                        EditText Txt = (EditText) v1;
                        switch (Txt.getId()){
                            case R.id.T_Nom :
                                Txt.setText(preferences.getString(String.valueOf(R.string.TextNom), ""));
                                break;
                            case R.id.T_Prenom :
                                Txt.setText(preferences.getString(String.valueOf(R.string.TextPrenom), ""));
                                break;
                            //La date
                            default :
                                Txt.setText(preferences.getString(String.valueOf(R.string.TextDateNaiss), ""));
                                break;
                        }
                    }
                    //Cas Spinner
                    else if (v1 instanceof Spinner){
                        Spinner Sp = (Spinner) v1;
                        if (Sp.getId() == R.id.SpinnerVilleNaiss){
                            //Spinner departement
                            Sp.setSelection(preferences.getInt(String.valueOf(R.string.TextVilleNaiss), 0));
                        }
                        Sp.setSelection(preferences.getInt(String.valueOf(R.string.TextDepartement), 0));
                    }
                }
            }

            //Ajout des numéros
            while (I_val > 0){
                addNumero(preferences.getString(String.valueOf(I_val),""));
                I_val--;
            }
        }

        B_Valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), T_Show, Toast.LENGTH_LONG).show();

                Snackbar.make(findViewById(R.id.myRelativeLayout), "Vous êtes " + T_nom.getText().toString() +
                        " " + T_prenom.getText().toString() + " né le " + T_DateNaiss.getText().toString()
                        + " à " + spVilleNaiss.getSelectedItem().toString() + " dans le departement de "
                        + spDepartement.getSelectedItem().toString(), Snackbar.LENGTH_LONG).show();
            }
        });

        B_AddNumber.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addNumero("");
                }
        });
    }

    //endregion


    //region  Remise à 0 des champs saisis
    public void remiseAzero(MenuItem item) {
        Log.i("TEST RAZ", "Je suis dans la méthode RAZ");

        //RAZ pour tous les éléments
        // On parcourt le vertical layout contenant les horizontaux
        // Pour chaque Layout horizontal, on teste les fils pour voi si Spinner ou EditText
        // Enfin, on efface leurs contenu
        layoutmain = findViewById(R.id.myLinearMain);
        int childCountnew = layoutmain.getChildCount();

        for (int i = 0; i < childCountnew; i++) {
            ViewGroup v = (ViewGroup) layoutmain.getChildAt(i);
            int nbrElem = v.getChildCount();

            for (int j = 0; j < nbrElem; j++)  {
                View v1 =  v.getChildAt(j);
                if (v1 instanceof EditText) {
                        EditText Text = (EditText) v1;
                        Text.setText("");
                }
                else if (v1 instanceof Spinner){
                        Spinner Sp = (Spinner) v1;
                        Sp.setSelection(0);
                }
            }
        }
    }

    // endregion

    //region  Methode Ajout dynamique de numero

    public void addNumero(String text){

        // Ajout du champ contenant le num de Tel
        layoutmain = findViewById(R.id.myLinearMain);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // margin = 20
        params.setMargins(20,20,20,20);
        params.weight=5;

        l = new LinearLayout(this);
        l.setId(R.id.numberNew);
        l.setLayoutParams(params);
        EditText number = new EditText (this);
        number.setHint(getString(R.string.TextNumber));
        number.setText(text);
        number.setInputType(InputType.TYPE_CLASS_NUMBER);
        // maxLength = 13
        number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        l.addView(number);
        int childCount1 = layoutmain.getChildCount();
        layoutmain.addView(l,childCount1-2);
        // incrémente le nombre de champs Tel
        I_Nbre++;

        // Suppression
        layoutbtn = (LinearLayout) findViewById(R.id.numberNew);
        int childCount = layoutbtn.getChildCount();
        if (childCount<=1){
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params1.setMargins(20,20,20,20);
            params.weight=4;
            Button deleteBtn = new Button(this);
            deleteBtn.setEms(10);
            deleteBtn.setLayoutParams(params);
            // Give button an ID
            deleteBtn.setId(R.id.reservedNamedId);
            deleteBtn.setText(getString(R.string.btnDeleteName));
            layoutbtn.setLayoutParams(params1);
            layoutbtn.addView(deleteBtn);
            // set the layoutParams on the button
            deleteBtn.setLayoutParams(params);
        }

        B_Supprimer = findViewById(R.id.reservedNamedId);
        B_Supprimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LinearLayout newlayout = (LinearLayout) findViewById(R.id.myLinearMain);
                int childCountnew = newlayout.getChildCount();
                //newlayout.getChildAt(childCountnew);
                Log.i("TEST Nbre ENFANTS", String.valueOf(childCountnew));

                //On supprime les champs Tel
                if (childCountnew > 7) {
                    newlayout.removeView(newlayout.getChildAt(childCountnew - 3));
                    I_Nbre--;
                }

                //On supprime le bouton
                int childCountnew1 = newlayout.getChildCount();
                if (childCountnew1 == 7) {
                    LinearLayout btnlayout = (LinearLayout) findViewById(R.id.numberNew);
                    newlayout.removeView(btnlayout);
                    I_Nbre = 0;
                }
            }
        });
    }

    // endregion

    //region Gestion du menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("TEST MENU", "Je suis dans le menu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //endregion

    //region Gestion du champ contenant la Date

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        ((EditText) findViewById(R.id.T_Date)).setText(dateFormat.format(calendar.getTime()));
    }

    public void datePicker(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }


    /**
     * Creation d'une classe DatePickerFragment qui hérite de DialogFragment et
     * Définition de onCreateDialog(), methode retournant une instance de DatePickerDialog
     */
    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);
        }
    }

    //endregion


    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("Nombre_Champ_Tel",I_Nbre);

        // On parcourt le vertical layout contenant les horizontaux
        // Pour chaque Layout horizontal, on teste les fils pour voir si Spinner ou EditText
        // Enfin, on enregistre le contenu et leurs textes sont pris comme clés
        layoutmain = findViewById(R.id.myLinearMain);
        int childCountnew = layoutmain.getChildCount();
        I_val = I_Nbre;

        for (int i = 0; i < childCountnew; i++) {
            ViewGroup v = (ViewGroup) layoutmain.getChildAt(i);
            int nbrElem = v.getChildCount();

            for (int j = 0; j < nbrElem; j++)  {
                View v1 =  v.getChildAt(j);

                //Cas EditText
                if (v1 instanceof EditText) {
                    EditText Txt = (EditText) v1;
                    switch (Txt.getId()){
                        case R.id.T_Nom :
                            outState.putString(getString(R.string.TextNom),Txt.getText().toString());
                            break;
                        case R.id.T_Prenom :
                            outState.putString(getString(R.string.TextPrenom),Txt.getText().toString());
                            break;
                        case R.id.T_Date :
                            outState.putString(getString(R.string.TextDateNaiss),Txt.getText().toString());
                            break;
                        default:
                            outState.putString(String.valueOf(I_val),Txt.getText().toString());
                            I_val--;
                            break;
                    }
                }

                //Cas Spinner
                else if (v1 instanceof Spinner){
                    Spinner Sp = (Spinner) v1;
                    if (Sp.getId() == R.id.SpinnerVilleNaiss){
                        //Spinner departement
                        outState.putInt(getString(R.string.TextVilleNaiss),Sp.getSelectedItemPosition());
                    }
                    outState.putInt(getString(R.string.TextDepartement),Sp.getSelectedItemPosition());
                }

            }
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // On recupère le nombre de champs Tel
        I_Nbre = savedInstanceState.getInt(" Nombre_Champ_Tel");
        I_val = I_Nbre;

        // On parcourt le vertical layout contenant les horizontaux
        // Pour chaque Layout horizontal, on teste les fils pour voir si Spinner ou EditText
        // Enfin, on restitue les contenu
        // A la sortie des deux boucles, on ajpoute les numéros de Tel

        layoutmain = findViewById(R.id.myLinearMain);
        int childCountnew = layoutmain.getChildCount();

        for (int i = 0; i < childCountnew; i++) {
            ViewGroup v = (ViewGroup) layoutmain.getChildAt(i);
            int nbrElem = v.getChildCount();

            for (int j = 0; j < nbrElem; j++)  {
                View v1 =  v.getChildAt(j);

                //Cas EditText
                if (v1 instanceof EditText) {
                    EditText Txt = (EditText) v1;
                    switch (Txt.getId()){
                        case R.id.T_Nom :
                            Txt.setText(savedInstanceState.getString(String.valueOf(R.string.TextNom)));
                            break;
                        case R.id.T_Prenom :
                            Txt.setText(savedInstanceState.getString(String.valueOf(R.string.TextPrenom)));
                            break;
                            //La date
                        default :
                           Txt.setText(savedInstanceState.getString(String.valueOf(R.string.TextDateNaiss)));
                            break;
                    }
                }

                //Cas Spinner
                else if (v1 instanceof Spinner){
                    Spinner Sp = (Spinner) v1;
                    if (Sp.getId() == R.id.SpinnerVilleNaiss){
                        //Spinner departement
                        Sp.setSelection(savedInstanceState.getInt(String.valueOf(R.string.TextVilleNaiss)));
                    }
                    Sp.setSelection(savedInstanceState.getInt(String.valueOf(R.string.TextDepartement)));
                }

            }
        }

        //Ajout des numéros
        while (I_val > 0){
            addNumero(savedInstanceState.getString(String.valueOf(I_val)));
            I_val--;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Lifecycle", "onDestroy method");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Lifecycle", "onStop method");

        // Preferences
        SharedPreferences preferences = getSharedPreferences("DATA_SAVED", MODE_PRIVATE);
        SharedPreferences.Editor editorPref = preferences.edit();

        //Nbre de champs Tel
        editorPref.putInt("Nombre_Champ_Tel", I_Nbre);
        I_val = I_Nbre;

        //Les autres champs
        // On parcourt le vertical layout contenant les horizontaux
        // Pour chaque Layout horizontal, on teste les fils pour voir si Spinner ou EditText
        // Enfin, on enregistre le contenu et leurs textes sont pris comme clés
        layoutmain = findViewById(R.id.myLinearMain);
        int childCountnew = layoutmain.getChildCount();

        for (int i = 0; i < childCountnew; i++) {
            ViewGroup v = (ViewGroup) layoutmain.getChildAt(i);
            int nbrElem = v.getChildCount();

            for (int j = 0; j < nbrElem; j++) {
                View v1 = v.getChildAt(j);

                //Cas EditText
                if (v1 instanceof EditText) {
                    EditText Txt = (EditText) v1;
                    switch (Txt.getId()) {
                        case R.id.T_Nom:
                            editorPref.putString(String.valueOf(R.string.TextNom), Txt.getText().toString());
                            break;
                        case R.id.T_Prenom:
                            editorPref.putString(String.valueOf(R.string.TextPrenom), Txt.getText().toString());
                            break;
                        case R.id.T_Date:
                            editorPref.putString(String.valueOf(R.string.TextDateNaiss), Txt.getText().toString());
                            break;
                        // dans ce acs, on a un champ num de Tel
                        default:
                            editorPref.putString(String.valueOf(I_val), Txt.getText().toString());
                            I_val--;
                            break;
                    }
                }

                //Cas Spinner
                else if (v1 instanceof Spinner) {
                    Spinner Sp = (Spinner) v1;
                    if (Sp.getId() == R.id.SpinnerVilleNaiss) {
                        //Spinner departement
                        editorPref.putInt(String.valueOf(R.string.TextVilleNaiss), Sp.getSelectedItemPosition());
                    }
                    editorPref.putInt(String.valueOf(R.string.TextDepartement), Sp.getSelectedItemPosition());
                }

            }
        }
        editorPref.apply();
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

}


//endregion


