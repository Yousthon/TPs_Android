package com.example.imt_atlantique.tp1final;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;


//region Activité principale

public class MainActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    //region  Recuperation des champs dans des variables globales
    private static Spinner SP_VilleNaiss, SP_Departement;
    private static EditText T_nom, T_prenom, T_DateNaiss;
    private static LinearLayout layoutmain,l;
    private ArrayList<String> L_Numeros;
    private static int Nbre_Numero = 0;
    private static Button B_Valider;

    //endregion

    //region  Méthode onCreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region  Recuperation des champs dans des variables globales

        SP_Departement = findViewById(R.id.SpinnerDepartement);
        SP_VilleNaiss = findViewById(R.id.SpinnerVilleNaiss);
        T_nom = findViewById(R.id.T_Nom);
        T_prenom = findViewById(R.id.T_Prenom);
        T_DateNaiss = findViewById(R.id.T_Date);
        B_Valider = findViewById(R.id.btnValidate);

        //endregion

        final File dataDirectory = Environment.getDataDirectory();
        Log.i("Lifecycle Chemin data", dataDirectory.getPath());

        //region  Recuperation de l'état de l'instance si killed
        Log.i("Lifecycle", "Restauration des valeurs dans OnRestore");

        if(savedInstanceState == null) {
            SharedPreferences MyData = getSharedPreferences("TP22_DATA",MODE_PRIVATE);

            // Restauration des valeurs de base
            T_nom.setText(MyData.getString("nom",""));
            T_prenom.setText(MyData.getString("prenom",""));
            T_DateNaiss.setText(MyData.getString("date_naiss",""));
            SP_VilleNaiss.setSelection(MyData.getInt("ville_naiss",0));
            SP_Departement.setSelection(MyData.getInt("departement",0));

            //Restauration des numéros s'il y'en a
            Nbre_Numero = MyData.getInt("nbre_tel",0);
            if (Nbre_Numero > 0) {
                L_Numeros = new ArrayList<>();
                Gson gson = new Gson();
                //désérialisation et recuperation de notre arraylist
                L_Numeros = gson.fromJson(MyData.getString("liste_num",null),
                        new TypeToken<ArrayList<String>>(){}.getType());
                //Création des boutons avec les numéros
                for (String values:L_Numeros)
                    addNum(values);
            }
        }

        //endregion

        //region  Bouton valider
        B_Valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), T_Show, Toast.LENGTH_LONG).show();

                Snackbar.make(findViewById(R.id.myRelativeLayout), "Vous êtes " + T_nom.getText().toString()
                        +" " + T_prenom.getText().toString() + " né le " + T_DateNaiss.getText().toString()
                        + " à " + SP_VilleNaiss.getSelectedItem().toString() + " dans le departement de "
                        + SP_Departement.getSelectedItem().toString(), Snackbar.LENGTH_LONG).show();
            }
        });

        //endregion
    }

    //endregion

    //region  Ajout dynamique de numero

    public void ajouterNumero(View view) {
        addNum("");
        Nbre_Numero++;
        Log.i("Nbre Numeros", String.valueOf(Nbre_Numero));
    }

    public void addNum(String text){
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
        l.setTag(String.valueOf(Nbre_Numero+5));
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

        // Suppression
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params1.setMargins(20,20,20,5);
        Button deleteBtn = new Button(this);
        deleteBtn.setEms(10);
        deleteBtn.setLayoutParams(params);

        // Give button an ID
        deleteBtn.setId(R.id.reservedNamedId);
        deleteBtn.setText(getString(R.string.btnDeleteName));

        // set the layoutParams on the button
        deleteBtn.setLayoutParams(params);
        l.setLayoutParams(params1);
        l.addView(deleteBtn);

        // ecouteur de clic pour la suppression
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LinearLayout layoutSup = (LinearLayout)v.getParent();
                layoutmain = findViewById(R.id.myLinearMain);
                layoutmain.removeView(layoutSup);
                Nbre_Numero--;

                Log.i("Nbre Num After Supp", String.valueOf(Nbre_Numero));
            }
        });



    }

    // endregion

    //region  Gestion de l'état de l'activité

    // Sauvegarde des données de l'instance courante
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("Lifecycle", "SAUVEGARDE DES VALEURS DANS OnSaved");

        //Sauvegarde des valeurs de bases
        outState.putString("nom", T_nom.getText().toString());
        outState.putString("prenom", T_prenom.getText().toString());
        outState.putString("date_naiss", T_DateNaiss.getText().toString());
        outState.putInt("departement", SP_Departement.getSelectedItemPosition());
        outState.putInt("ville_naiss", SP_VilleNaiss.getSelectedItemPosition());

        //Sauvegarde des numéros s'il y'en a
        outState.putInt("nbre_tel", Nbre_Numero);
        if(Nbre_Numero>0){
            saveNumero(L_Numeros = new ArrayList<>());
            outState.putStringArrayList("liste_num", L_Numeros);
        }
    }

    //Restauration des données apres rotation ecran
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i("Lifecycle", "Restauration des valeurs dans OnRestore");

        // Recuperation de l'état de l'instance n'est pas tuée - rotation de l'ecran par exemple
        T_nom.setText(savedInstanceState.getString("nom"));
        T_prenom.setText(savedInstanceState.getString("prenom"));
        T_DateNaiss.setText(savedInstanceState.getString("date_naiss"));
        SP_VilleNaiss.setSelection(savedInstanceState.getInt("ville_naiss"));
        SP_Departement.setSelection(savedInstanceState.getInt("departement"));

        //Restauration des numéros s'il y'en a
        Nbre_Numero = savedInstanceState.getInt("nbre_tel");
        if (Nbre_Numero>0) {
            L_Numeros = new ArrayList<>();
            L_Numeros = savedInstanceState.getStringArrayList("liste_num");
            for (String value : L_Numeros) {
                addNum(value);
            }
        }

    }

    //Sauvegarde pour le cas où l'activité est killed
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Lifecycle", "SAUVEGARDE DES DONNEES DANS OnStop");

        SharedPreferences preferences = getSharedPreferences("TP22_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editorPref = preferences.edit();

        //Sauvegarde des valeurs de bases
        editorPref.putString("nom", T_nom.getText().toString());
        editorPref.putString("prenom", T_prenom.getText().toString());
        editorPref.putString("date_naiss", T_DateNaiss.getText().toString());
        editorPref.putInt("departement", SP_Departement.getSelectedItemPosition());
        editorPref.putInt("ville_naiss", SP_VilleNaiss.getSelectedItemPosition());

        //Sauvegarde des numéros s'il y'en a
        editorPref.putInt("nbre_tel", Nbre_Numero);
        if (Nbre_Numero > 0) {
            saveNumero(L_Numeros = new ArrayList<>());
            //Serialisation du ArrayList via la classe Gson de Google et sauvegarde
            Gson gson = new Gson();
            editorPref.putString("liste_num", gson.toJson(L_Numeros));
        }

        //Validation des changements
        editorPref.apply();
    }

    //Sauvegarde des numéros
    public void saveNumero(ArrayList<String> liste) {
            EditText v1;
            l = new LinearLayout(this);
            layoutmain = findViewById(R.id.myLinearMain);
            int Nbre_Elem = layoutmain.getChildCount();

            for (int i = Nbre_Elem - (Nbre_Numero + 2); i < Nbre_Elem - 2; i++) {
                l = (LinearLayout) layoutmain.getChildAt(i);
                if (l.getChildAt(0) instanceof EditText) {
                    v1 = (EditText) l.getChildAt(0);
                } else {
                    v1 = (EditText) l.getChildAt(1);
                }
                liste.add(v1.getText().toString());
            }
    }

    //endregion

    //region Gestion du menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("TEST MENU", "Je suis dans le menu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //endregion

    //region  Remise à 0 des champs saisis

    public void remiseAzero(MenuItem item) {
        Log.i("TEST RAZ", "Je suis dans la méthode RAZ");

        //RAZ pour les éléments de base
        T_DateNaiss.setText("");
        T_nom.setText("");
        T_prenom.setText("");
        SP_Departement.setSelection(0);
        SP_VilleNaiss.setSelection(0);

        //RAZ pour les numeros de Tel
        EditText v1;
        l = new LinearLayout(this);
        layoutmain = findViewById(R.id.myLinearMain);
        int Nbre_Elem = layoutmain.getChildCount();

        for (int i = Nbre_Elem - (Nbre_Numero + 2); i < Nbre_Elem - 2; i++) {
            l = (LinearLayout) layoutmain.getChildAt(i);
            if (l.getChildAt(0) instanceof EditText) {
                v1 = (EditText) l.getChildAt(0);
            } else {
                v1 = (EditText) l.getChildAt(1);
            }
            v1.setText("");
        }
    }
    // endregion

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
}

//endregion



