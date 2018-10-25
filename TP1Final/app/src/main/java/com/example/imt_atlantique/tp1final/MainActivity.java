package com.example.imt_atlantique.tp1final;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;



public class MainActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Récupération des variables ou champs
         * Definition des actions sur le bouton de validation
         */

        // Spinner pour les villes de naissance
        final Spinner spVilleNaiss = (Spinner) findViewById(R.id.SpinnerVilleNaiss);
        // Recuperation des textes des variables
        final EditText T_nom = findViewById(R.id.T_Nom);
        final EditText T_prenom = findViewById(R.id.T_Prenom);
        final EditText T_DateNaiss = findViewById(R.id.T_Date);
        final Button B_Valider = findViewById(R.id.btnValidate);

        B_Valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), T_Show, Toast.LENGTH_LONG).show();

                Snackbar.make(findViewById(R.id.myRelativeLayout), "Vous êtes " + T_nom.getText().toString() +
                        " " + T_prenom.getText().toString() +" né le " + T_DateNaiss.getText().toString()
                                + " à " + spVilleNaiss.getSelectedItem().toString(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /**Gestion du champ contenant la Date
     * Creation d'une classe DatePickerFragment qui hérite de DialogFragment.
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
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }
    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        ((EditText) findViewById(R.id.T_Date)).setText(dateFormat.format(calendar.getTime()));
    }
    public void datePicker(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }
}

