package com.example.imt_atlantique.tp1final;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.R.drawable.ic_delete;
import static android.util.Log.i;


//region Activité principale

public class MainActivity extends AppCompatActivity {

    //region  Recuperation des champs dans des variables globales
    private static Spinner SP_VilleNaiss, SP_Departement;
    private static EditText T_nom, T_prenom, T_DateNaiss;
    private static LinearLayout layoutmain,l;
    private ArrayList<String> L_Numeros;
    private static int Nbre_Numero = 0;
    private static Button B_Valider;
    private static Button B_Number;

    String dateFormat = "dd/MM/yyyy";
    DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.GERMAN);

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
        B_Number = findViewById(R.id.btnDialNumber);
        T_DateNaiss = findViewById(R.id.T_Date);
        T_DateNaiss.addTextChangedListener(mDateEntryWatcher);

        //endregion

        final File dataDirectory = Environment.getDataDirectory();
        i("Lifecycle Chemin data", dataDirectory.getPath());

        //region  Recuperation de l'état de l'instance si killed

        if(savedInstanceState == null) {
            SharedPreferences MyData = getSharedPreferences("TP32_DATA",MODE_PRIVATE);
            // Restauration des valeurs de base
            T_nom.setText(MyData.getString("nom",""));
            T_prenom.setText(MyData.getString("prenom",""));
            T_DateNaiss.setText(MyData.getString("date_naiss",""));
            SP_VilleNaiss.setSelection(MyData.getInt("ville_naiss",0));
            SP_Departement.setSelection(MyData.getInt("departement",0));

            i("Lifecycle", "avant");
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
            //On désactive le bouton permettant de voir les numéros
        }
        //endregion

        //region  Bouton valider
        B_Valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String name =T_nom.getText().toString();
                String prenom=T_prenom.getText().toString();
                String date=T_DateNaiss.getText().toString();
                String ville = SP_VilleNaiss.getSelectedItem().toString();
                String depart = SP_Departement.getSelectedItem().toString();
                User user;
                Intent intent = new Intent(getApplicationContext(), DisplayActivity.class);


                user = new User(name, prenom, date, ville, depart, null);
                if(Nbre_Numero>0) {
                    saveNumero(L_Numeros = new ArrayList<>());
                    user = new User(name, prenom, date, ville, depart, L_Numeros);
                }

                // envoi de l'objet User dans l'intent
                intent.putExtra("user", user); // la clé, la valeur
                startActivity(intent);
            }
        });

        //endregion

        //region  Bouton voir les num
        B_Number.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                i("Nbre Num After Supp", String.valueOf(Nbre_Numero));
                if (Nbre_Numero > 0) {
                    Intent intent = new Intent(getApplicationContext(), DialNumber.class);
                    saveNumero(L_Numeros = new ArrayList<>());

                    //Serialisation puis envoi
                    intent.putStringArrayListExtra("liste_Num", L_Numeros);
                    startActivity(intent);
                }
                else
                    Snackbar.make(findViewById(R.id.myRelativeLayout),
                            R.string.Num_Tel_Saisi,
                            Snackbar.LENGTH_LONG).show();
            }
        });
        //endregion
    }


  //region TextWatcher on EditText date
    private TextWatcher mDateEntryWatcher = new TextWatcher() {

      private String current = "";
      private String ddmmyyyy = "DDMMYYYY";
      private Calendar cal = Calendar.getInstance();
      //When user changes text of the EditText

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
          if (!s.toString().equals(current)) {
              String clean = s.toString().replaceAll("[^\\d.]", "");
              String cleanC = current.replaceAll("[^\\d.]", "");

              int cl = clean.length();
              int sel = cl;
              for (int i = 2; i <= cl && i < 6; i += 2) {
                  sel++;
              }
              //Fix for pressing delete next to a forward slash
              if (clean.equals(cleanC)) sel--;

              if (clean.length() < 8){
                  clean = clean + ddmmyyyy.substring(clean.length());
              }else{
                  //This part makes sure that when we finish entering numbers
                  //the date is correct, fixing it otherwise
                  int day  = Integer.parseInt(clean.substring(0,2));
                  int mon  = Integer.parseInt(clean.substring(2,4));
                  int year = Integer.parseInt(clean.substring(4,8));

                  if(mon > 12) mon = 12;
                  cal.set(Calendar.MONTH, mon-1);
                  year = (year<1900)?1900:(year>2100)?1900:year;
                  cal.set(Calendar.YEAR, year);
                  // ^ first set year for the line below to work correctly
                  //with leap years - otherwise, date e.g. 29/02/2012
                  //would be automatically corrected to 28/02/2012

                  day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                  clean = String.format("%02d%02d%02d",day, mon, year);
              }

              clean = String.format("%s/%s/%s", clean.substring(0, 2),
                      clean.substring(2, 4),
                      clean.substring(4, 8));

              sel = sel < 0 ? 0 : sel;
              current = clean;
              T_DateNaiss.setText(current);
              T_DateNaiss.setSelection(sel < current.length() ? sel : current.length());
          }
      }
      //We also implement the other two functions because we have to

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

      @Override
      public void afterTextChanged(Editable s) {}
    };

    //endregion

    //region  Ajout dynamique de numero

    public void ajouterNumero(View view) {
        addNum("");
        Nbre_Numero++;
        i("Nbre Numeros", String.valueOf(Nbre_Numero));
    }

    public void addNum(String text){
        // Ajout du champ contenant le num de Tel

        layoutmain = findViewById(R.id.myLinearMain);
        LinearLayout.LayoutParams params, params1;
        params = params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // margin = 20
        params.setMargins(20,20,20,20);
        params.weight=5;
        params1.weight=1;

        l = new LinearLayout(this);
        l.setId(R.id.numberNew);
        l.setTag(String.valueOf(Nbre_Numero+5));
        l.setLayoutParams(params);
        EditText number = new EditText (this);
        number.setHint(getString(R.string.TextNumber));
        number.setText(text);
        number.setInputType(InputType.TYPE_CLASS_NUMBER);
        number.setLayoutParams(params1);
        // maxLength = 13
        number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        l.addView(number);
        int childCount1 = layoutmain.getChildCount();
        layoutmain.addView(l,childCount1-1);

        Button deleteBtn = new Button(this);
        deleteBtn.setEms(10);
        deleteBtn.setLayoutParams(params1);
        // Give button an ID
        deleteBtn.setId(R.id.reservedNamedId);

        deleteBtn.setCompoundDrawablesWithIntrinsicBounds(ic_delete,0,0,0);
        deleteBtn.setTextColor(this.getResources().getColor(R.color.colorPrimary));
        deleteBtn.setText(getString(R.string.btnDeleteName));

        l.addView(deleteBtn);
        // ecouteur de clic pour la suppression
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LinearLayout layoutSup = (LinearLayout)v.getParent();
                layoutmain = findViewById(R.id.myLinearMain);
                layoutmain.removeView(layoutSup);
                Nbre_Numero--;
                i("Nbre Num After Supp", String.valueOf(Nbre_Numero));
            }
        });
    }

    // endregion

    //region  Gestion de l'état de l'activité

    // Sauvegarde des données de l'instance courante
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        i("Lifecycle", "SAUVEGARDE DES VALEURS DANS OnSaved");

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
        i("Lifecycle", "Restauration des valeurs dans OnRestore");

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
        i("Lifecycle", "SAUVEGARDE DES DONNEES DANS OnStop");

        SharedPreferences preferences = getSharedPreferences("TP32_DATA", MODE_PRIVATE);
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

            for (int i = Nbre_Elem - (Nbre_Numero + 1); i < Nbre_Elem - 1; i++) {

                l = (LinearLayout) layoutmain.getChildAt(i);
                if (l.getChildAt(0) instanceof EditText) {
                    v1 = (EditText) l.getChildAt(0);
                } else {
                    v1 = (EditText) l.getChildAt(1);
                }
                String str = v1.getText().toString();
                if( !(str == null) && !(str.isEmpty()))
                    liste.add(str);
            }
    }

    //endregion

    //region Gestion du menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        i("TEST MENU", "Je suis dans le menu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //endregion

    //region  Remise à 0 des champs saisis

    public void remiseAzero(MenuItem item) {
        i("TEST RAZ", "Je suis dans la méthode RAZ");

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

        for (int i = Nbre_Elem - (Nbre_Numero + 1); i < Nbre_Elem - 1; i++) {
            l = (LinearLayout) layoutmain.getChildAt(i);
            if (l.getChildAt(0) instanceof EditText) {
                v1 = (EditText) l.getChildAt(0);
            } else {
                v1 = (EditText) l.getChildAt(1);
            }
            v1.setText("");
        }
    }

    public boolean search(MenuItem item) {
        SP_VilleNaiss = findViewById(R.id.SpinnerVilleNaiss);
        String ville_naiss = SP_VilleNaiss.getSelectedItem().toString();
        Uri site = Uri.parse("http://fr.wikipedia.org/?search=" + ville_naiss);
        Intent web = new Intent(Intent.ACTION_VIEW,site);
        String title =getResources().getString(R.string.chooser);
        Intent choose = Intent.createChooser(web, title);
        if (web.resolveActivity(getPackageManager())!=null){
            startActivity(choose);}
        return true;
    }

    public void share(MenuItem item){
        SP_VilleNaiss = findViewById(R.id.SpinnerVilleNaiss);
        String ville_naiss = SP_VilleNaiss.getSelectedItem().toString();
        Intent sendintent = new Intent(Intent.ACTION_SEND);
        sendintent.putExtra(Intent.EXTRA_TEXT,ville_naiss);
        sendintent.setType("text/plain");
        startActivity(Intent.createChooser(sendintent, "Send a simple text"));
    }
    // endregion

    //creation intent_date
    public void choiceDate(View v){
        //Intent pagedate = new Intent(getApplicationContext(),DateActivity.class);
        Intent pagedate = new Intent(getApplicationContext(), DateActivity.class);
        pagedate.setAction(Intent.ACTION_PICK);
        EditText madate = findViewById(R.id.T_Date);
        // call la page
        String str = madate.getText().toString().replace("/","");
        pagedate.putExtra("tadate",str);
        Log.i("Date",str);
        startActivityForResult(pagedate,0 );

    }

    //recupere la date selectionner sur l'autre activité
    protected void onActivityResult(int requestCode, int resultCode, Intent datewritten) {
        super.onActivityResult(requestCode, resultCode, datewritten);
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {

            String text = datewritten.getStringExtra("FINAL");
            EditText datechosen = findViewById(R.id.T_Date);
            datechosen.setText(text);
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 4){
            String text = datewritten.getStringExtra("prenom_ok");
            EditText prenom =  findViewById(R.id.T_Prenom);
            prenom.setText(text);
        }

    }


    //intent implicite pour voir le nom
    public void view_name(View v){
        String name =T_nom.getText().toString();
        Intent sendintent = new Intent(getApplicationContext(), VoirNom.class);
        sendintent.setAction(Intent.ACTION_VIEW);
        sendintent.putExtra("NOM",name);
        startActivity(sendintent);


    }

    public void edit_prenom(View v){

        EditText prenom = findViewById(R.id.T_Prenom);
        String name = prenom.getText().toString();
        Intent sendintent = new Intent(getApplicationContext(), EditPrenom.class);
        sendintent.setAction(Intent.ACTION_EDIT);
        sendintent.putExtra("PRENOM",name);
        startActivityForResult(sendintent,4);


    }


    //endregion
}

//endregion



