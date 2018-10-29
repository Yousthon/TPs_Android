package com.example.imt_atlantique.tp1final;


//region Importations des librairies

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


//endregion


//region Activité principale

public class MainActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    //region  Recuperation des champs dans des variables globales
    // Spinner pour les villes de naissance
    private static Spinner spVilleNaiss, spDepartement;
    // Recuperation des textes des variables
    private static EditText T_nom;
    private static EditText T_prenom;
    private static EditText T_DateNaiss;
    private static Button B_Valider;
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

        B_Valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), T_Show, Toast.LENGTH_LONG).show();

                Snackbar.make(findViewById(R.id.myRelativeLayout), "Vous êtes " + T_nom.getText().toString() +
                        " " + T_prenom.getText().toString() + " né le " + T_DateNaiss.getText().toString()
                        + " à " + spVilleNaiss.getSelectedItem().toString() + " dans le departement de "
                        + spDepartement.getSelectedItem().toString(), Snackbar.LENGTH_LONG).show();
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


    //region  Ajout dynamique de numero
    public void ajouterNumero(View view) {

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
        number.setInputType(InputType.TYPE_CLASS_NUMBER);
        // maxLength = 13
        number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        l.addView(number);
        int childCount1 = layoutmain.getChildCount();
        layoutmain.addView(l,childCount1-2);

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

        final Button B_Supprimer = (Button) findViewById(R.id.reservedNamedId);
        B_Supprimer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                LinearLayout newlayout = (LinearLayout) findViewById(R.id.myLinearMain);
                int childCountnew = newlayout.getChildCount();
                //newlayout.getChildAt(childCountnew);
                Log.i("TEST Nbre ENFANTS", String.valueOf(childCountnew));

                //On supprime les champs Tel
                if (childCountnew>7){
                    newlayout.removeView(newlayout.getChildAt(childCountnew-3));
                }

                //On supprime le bouton
                int childCountnew1 = newlayout.getChildCount();
                if (childCountnew1==7){
                    LinearLayout btnlayout = (LinearLayout) findViewById(R.id.numberNew);
                    newlayout.removeView(btnlayout);
                }

            }
        });
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

    //region Gestion du menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("TEST MENU", "Je suis dans le menu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //endregion

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


