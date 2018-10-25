package com.example.imt_atlantique.myfirstapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends Activity {
    private static int nbr=0;
    private static int val=0;
    private static int posi=0;
    private static String nom="";
    protected static String surname="";

    private static String date_naiss="";
    private static String ville_naiss="";
    private static String departement="";



    Context context = this;
    EditText editDate;
    Calendar myCalendar = Calendar.getInstance();
    String dateFormat = "dd/MM/yyyy";
    DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.GERMAN);

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        if (savedInstanceState==null){
            SharedPreferences preferences= getSharedPreferences("CLIKS_SAVED", MODE_PRIVATE);
            nbr = preferences.getInt("SavegrdeClics",0);
            val = preferences.getInt("val",0);
            posi = preferences.getInt("posi",0);
            nom = preferences.getString("nom","");
            surname=preferences.getString("surname","");
            date_naiss=preferences.getString("date_naiss","");
            ville_naiss=preferences.getString("ville_naiss","");
            departement=preferences.getString("departement","");


            EditText firstname = (EditText) findViewById(R.id.firstname);
            firstname.setText(nom);

            EditText prenom = (EditText) findViewById(R.id.prenom);
            prenom.setText(surname);

            EditText dateNaissance = (EditText) findViewById(R.id.dateNaissance);
            dateNaissance.setText(date_naiss);

            EditText villeNaissance = (EditText) findViewById(R.id.villeNaissance);
            villeNaissance.setText(ville_naiss);

            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            spinner.setSelection(posi);



            //EditText datenaissance = (EditText) findViewById(R.id.dateNaissance);
            //datenaissance.setText(date_naiss);


            while (nbr>=0){


                LinearLayout layoutmain = (LinearLayout) findViewById(R.id.linearmain);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout l = new LinearLayout(this);
                l.setId(R.id.numberNew);
                EditText number = new EditText (this);
                number.setText("060000000");
                number.setInputType(InputType.TYPE_CLASS_PHONE);
                TextView textNum= new TextView(this);
                textNum.setText("Numéro de tel: ");
                l.addView(textNum);
                l.addView(number);
                int childCount1 = layoutmain.getChildCount();
                layoutmain.addView(l,childCount1-2);

                // Delete
                LinearLayout layoutbtn = (LinearLayout) findViewById(R.id.numberlayout);
                int childCount = layoutbtn.getChildCount();
                if (childCount<=1){
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    Button deleteBtn = new Button(this);
                    // Give button an ID
                    deleteBtn.setId(R.id.reservedNamedId);

                    deleteBtn.setText("supprimer Num");
                    layoutbtn.addView(deleteBtn);

                    //deleteBtn.setGravity(Gravity.CENTER_HORIZONTAL);

                    // set the layoutParams on the button
                    //deleteBtn.setLayoutParams(params);


                }



                final Button b = (Button) findViewById(R.id.reservedNamedId);
                b.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        LinearLayout newlayout = (LinearLayout) findViewById(R.id.linearmain);
                        int childCountnew = newlayout.getChildCount();
                        //newlayout.getChildAt(childCountnew);
                        Log.i("TEST TAGADA", String.valueOf(childCountnew));
                        if (childCountnew>7){
                            newlayout.removeView(newlayout.getChildAt(childCountnew-3));
                            nbr=val;
                            nbr=nbr-1;
                            val=nbr;
                        }
                        int childCountnew1 = newlayout.getChildCount();
                        if (childCountnew1==7){
                            LinearLayout btnlayout = (LinearLayout) findViewById(R.id.numberlayout);

                            btnlayout.removeView(b);
                            nbr=val;
                            nbr=nbr-1;
                            val=nbr;

                        }

                    }
                });

                nbr=nbr-1;




            }
        }
        nbr=val;
        final File dataDirectory = Environment.getDataDirectory();
        Log.i("TEST TAGADA", "Je suis dans la méthode");


        Button b = (Button) findViewById(R.id.validateBtn);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //firstName
                EditText firstname = (EditText) findViewById(R.id.firstname);
                nom=firstname.getText().toString();
                //lastName
                EditText prenom = (EditText) findViewById(R.id.prenom);
                surname=prenom.getText().toString();
                //date de naissance
                EditText dateNaissance = (EditText) findViewById(R.id.dateNaissance);
                date_naiss=dateNaissance.getText().toString();
                //ville de naissance
                EditText villeNaissance = (EditText) findViewById(R.id.villeNaissance);
                ville_naiss=villeNaissance.getText().toString();


                //departement
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                String value = spinner.getSelectedItem().toString();
                posi=spinner.getSelectedItemPosition();
                departement=value;
                String textToShow = new String(firstname.getText().toString() + " \n"
                        + prenom.getText().toString() + " \n" + dateNaissance.getText().toString() + "\n " +
                        villeNaissance.getText().toString() + "\n " + value
                );

                Toast.makeText(getApplicationContext(), textToShow, Toast.LENGTH_LONG).show();

            }


        });


        //fonction date

        editDate = (EditText) findViewById(R.id.dateNaissance);

        // initialisation de la date actuelle
        long currentdate = System.currentTimeMillis();
        String dateString = sdf.format(currentdate);
        editDate.setText(dateString);

        // mise à jour de la date
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }


        };
        // le date picker popup
        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }



    private void updateDate() {
        editDate.setText(sdf.format(myCalendar.getTime()));
    }





    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("TEST TAGADA","Je suis dans la méthode");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;


    } ;
   /* public boolean onOptionsItemSelected(MenuItem item) {
//tagada
        int id = item.getItemId();
        if (id==R.id.resetAction){
            tagada(item);
        }
        return true;
    };*/

    public void tagada (MenuItem item) {
        Log.i("TEST TAGADA","Je suis dans la méthode");
        //firstName
        EditText firstname = (EditText) findViewById(R.id.firstname);
        firstname.setText("");
        //lastName
        EditText prenom = (EditText) findViewById(R.id.prenom);
        prenom.setText("");

        //date de naissance
        EditText dateNaissance = (EditText) findViewById(R.id.dateNaissance);
        dateNaissance.setText("02/02/2010");
        //ville de naissance
        EditText villeNaissance = (EditText) findViewById(R.id.villeNaissance);
        villeNaissance.setText("");




    }
    public void addNumber (View v){
        // add number
        LinearLayout layoutmain = (LinearLayout) findViewById(R.id.linearmain);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout l = new LinearLayout(this);
        l.setId(R.id.numberNew);
        EditText number = new EditText (this);
        number.setText("060000000");
        number.setInputType(InputType.TYPE_CLASS_PHONE);
        TextView textNum= new TextView(this);
        textNum.setText("Numéro de tel: ");
        l.addView(textNum);
        l.addView(number);
        int childCount1 = layoutmain.getChildCount();
        layoutmain.addView(l,childCount1-2);
        nbr=nbr+1;
        val=nbr;



        // Delete
        LinearLayout layoutbtn = (LinearLayout) findViewById(R.id.numberlayout);
        int childCount = layoutbtn.getChildCount();
        if (childCount<=1){
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            Button deleteBtn = new Button(this);
            // Give button an ID
            deleteBtn.setId(R.id.reservedNamedId);

            deleteBtn.setText("supprimer Num");
            layoutbtn.addView(deleteBtn);

            //deleteBtn.setGravity(Gravity.CENTER_HORIZONTAL);

            // set the layoutParams on the button
            //deleteBtn.setLayoutParams(params);


        }

        final Button b = (Button) findViewById(R.id.reservedNamedId);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                LinearLayout newlayout = (LinearLayout) findViewById(R.id.linearmain);
                int childCountnew = newlayout.getChildCount();
                //newlayout.getChildAt(childCountnew);
                Log.i("TEST TAGADA", String.valueOf(childCountnew));
                if (childCountnew>7){
                    newlayout.removeView(newlayout.getChildAt(childCountnew-3));

                    nbr=nbr-1;
                    val=nbr;
                }
                int childCountnew1 = newlayout.getChildCount();
                if (childCountnew1==7){
                    LinearLayout btnlayout = (LinearLayout) findViewById(R.id.numberlayout);

                    btnlayout.removeView(b);

                    nbr=nbr-1;
                    val=nbr;

                }

            }
        });






    }


    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("SavegrdeClics",nbr);
        outState.putInt("val",val);
        outState.putString("nom",nom);
        outState.putString("surname",surname);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nbr=savedInstanceState.getInt("SavegrdeClics");
        val=savedInstanceState.getInt("val");
        nom=savedInstanceState.getString("nom");
        surname=savedInstanceState.getString("surname");
        //Button nbrtimes = (Button) findViewById(R.id.button);
        // nbrtimes.setText(String.valueOf(nbr));
        nbr=nbr+1;

        while (nbr>=0){


            LinearLayout layoutmain = (LinearLayout) findViewById(R.id.linearmain);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout l = new LinearLayout(this);
            l.setId(R.id.numberNew);
            EditText number = new EditText (this);
            number.setText("060000000");
            number.setInputType(InputType.TYPE_CLASS_PHONE);
            TextView textNum= new TextView(this);
            textNum.setText("Numéro de tel: ");
            l.addView(textNum);
            l.addView(number);
            int childCount1 = layoutmain.getChildCount();
            layoutmain.addView(l,childCount1-2);

            LinearLayout layoutbtn = (LinearLayout) findViewById(R.id.numberlayout);
            int childCount = layoutbtn.getChildCount();
            if (childCount<=1){
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Button deleteBtn = new Button(this);
                // Give button an ID
                deleteBtn.setId(R.id.reservedNamedId);

                deleteBtn.setText("supprimer Num");
                layoutbtn.addView(deleteBtn);

                //deleteBtn.setGravity(Gravity.CENTER_HORIZONTAL);

                // set the layoutParams on the button
                //deleteBtn.setLayoutParams(params);


            }

            final Button b = (Button) findViewById(R.id.reservedNamedId);
            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    LinearLayout newlayout = (LinearLayout) findViewById(R.id.linearmain);
                    int childCountnew = newlayout.getChildCount();
                    //newlayout.getChildAt(childCountnew);
                    Log.i("TEST TAGADA", String.valueOf(childCountnew));
                    if (childCountnew>7){
                        newlayout.removeView(newlayout.getChildAt(childCountnew-3));
                        nbr=val;
                        nbr=nbr-1;
                        val=nbr;
                    }
                    int childCountnew1 = newlayout.getChildCount();
                    if (childCountnew1==7){
                        LinearLayout btnlayout = (LinearLayout) findViewById(R.id.numberlayout);

                        btnlayout.removeView(b);
                        nbr=val;
                        nbr=nbr-1;
                        val=nbr;

                    }

                }
            });


            nbr=nbr-1;



        }
        nbr=val;

    }

    protected void onStop() {
        super.onStop();
        // Preferences
        SharedPreferences preferences= getSharedPreferences("CLIKS_SAVED", MODE_PRIVATE);
        SharedPreferences.Editor editorPref= preferences.edit();
        editorPref.putInt("SavegrdeClics",nbr);
        editorPref.putInt("val",val);
        editorPref.putInt("posi",posi);
        editorPref.putString("nom",nom);
        editorPref.putString("surname",surname);
        editorPref.putString("date_naiss",date_naiss);
        editorPref.putString("ville_naiss",ville_naiss);
        editorPref.putString("departement",departement);

        editorPref.apply();

    }






}
