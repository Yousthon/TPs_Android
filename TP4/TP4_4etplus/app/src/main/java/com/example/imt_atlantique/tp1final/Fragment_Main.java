package com.example.imt_atlantique.tp1final;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.R.drawable.ic_delete;
import static android.content.Context.MODE_PRIVATE;
import static android.util.Log.i;


public class Fragment_Main extends Fragment {

    private static Spinner SP_VilleNaiss, SP_Departement;
    private static EditText T_nom, T_prenom, T_DateNaiss;
    private static LinearLayout layoutmain,l;
    private ArrayList<String> L_Numeros;
    private static int Nbre_Numero = 0;
    private static Button B_Valider, B_Nom, B_Prenom, B_Number, B_AddNumber, B_DateNaiss;


    DatePickerDialog.OnDateSetListener date;
    private OnPrenomFragmentListener mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_main, container, false);

        SP_Departement = v.findViewById(R.id.SpinnerDepartement);
        SP_VilleNaiss = v.findViewById(R.id.SpinnerVilleNaiss);
        T_nom = v.findViewById(R.id.T_Nom);
        T_prenom = v.findViewById(R.id.T_Prenom);

        B_Nom = v.findViewById(R.id.Btn_voirNom);
        B_Prenom = v.findViewById(R.id.Btn_editPrenom);
        B_Valider = v.findViewById(R.id.btnValidate);
        B_Number = v.findViewById(R.id.btnDialNumber);
        B_AddNumber = v.findViewById(R.id.btnAddNumber);
        T_DateNaiss = v.findViewById(R.id.T_Date);
        B_DateNaiss = v.findViewById(R.id.Btn_editDate);
        T_DateNaiss.addTextChangedListener(mDateEntryWatcher);

        Bundle bundle = getArguments();
        //restitution

        if(savedInstanceState == null) {

            SharedPreferences MyData = getActivity().getSharedPreferences("A", MODE_PRIVATE);
            // Restauration des valeurs de base

            T_nom.setText(MyData.getString("nom",""));
            T_prenom.setText(MyData.getString("prenom",""));
            T_DateNaiss.setText(MyData.getString("date_naiss",""));
            SP_VilleNaiss.setSelection(MyData.getInt("ville_naiss",0));
            SP_Departement.setSelection(MyData.getInt("departement",0));

            i("Lifecycle", "avant 0");
            //Restauration des numéros s'il y'en a
            Nbre_Numero = MyData.getInt("nbre_tel",0);
            i("Lifecycle", "avant 1 " + Nbre_Numero);
            if (Nbre_Numero > 0) {
                L_Numeros = new ArrayList<>();
                Gson gson = new Gson();
                //désérialisation et recuperation de notre arraylist
                L_Numeros = gson.fromJson(MyData.getString("liste_num",null),
                        new TypeToken<ArrayList<String>>(){}.getType());

                //Création des boutons avec les numéros
                for (String values:L_Numeros) {
                    i("Lifecycle", "avant boucle" + values);
                    addNum1(values,v);
                }
            }
            i("Lifecycle", "avant fin");
            //On désactive le bouton permettant de voir les numéros
        }

        if (bundle!= null) {
            String mes = bundle.getString("reponse");
            T_prenom.setText(mes);
        }

        i("Lifecycle", "Dans le fragment Main");

        //Les boutons

        B_Prenom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = T_prenom.getText().toString();
                mCallback.messageFromPrenomFragment(message);

            }
        });

        B_Nom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = T_nom.getText().toString();
                mCallback.messageFromNomFragment(message);
            }
        });

        B_DateNaiss.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                        DialogFragment newFragment = new DateFragment();
                        newFragment.show(getFragmentManager(), "DatePicker");

                    }
                });

        //Button validate
        B_Valider.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String name =T_nom.getText().toString();
                String prenom=T_prenom.getText().toString();
                String date=T_DateNaiss.getText().toString();
                String ville = SP_VilleNaiss.getSelectedItem().toString();
                String depart = SP_Departement.getSelectedItem().toString();
                User user;
                Intent intent = new Intent(getActivity(), DisplayActivity.class);

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

        B_Number.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                i("Nbre Num After Supp", String.valueOf(Nbre_Numero));
                if (Nbre_Numero > 0) {
                    Intent intent = new Intent(getActivity(), DialNumber.class);
                    saveNumero(L_Numeros = new ArrayList<>());

                    //Serialisation puis envoi
                    intent.putStringArrayListExtra("liste_Num", L_Numeros);
                    startActivity(intent);
                }
                else{
                    i("Nbre ", " Je suis ici "+String.valueOf(Nbre_Numero));

                    Snackbar.make(getView().findViewById(R.id.myRelativeLayout),
                            R.string.Num_Tel_Saisi,
                            Snackbar.LENGTH_LONG).show();
            }
            }
        });


        B_AddNumber.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addNum("");
                Nbre_Numero++;
            }
        });

        return v;
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
    public void addNum(String text){
        // Ajout du champ contenant le num de Tel
        i("Lifecycle", "adNum 0");

        layoutmain = new LinearLayout(getActivity());
        //Bizarre un probleme lié aux vues
        layoutmain = getView().findViewById(R.id.myLinearMain);
        i("Lifecycle", "adNum 1");
        LinearLayout.LayoutParams params, params1;
        params = params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // margin = 20
        params.setMargins(20,20,20,20);
        params.weight=5;
        params1.weight=1;

        i("Lifecycle", "adNum 2");

        l = new LinearLayout(getActivity());
        l.setId(R.id.numberNew);
        l.setLayoutParams(params);
        EditText number = new EditText (getActivity());
        number.setHint(getString(R.string.TextNumber));
        number.setText(text);
        number.setInputType(InputType.TYPE_CLASS_NUMBER);
        number.setLayoutParams(params1);
        // maxLength = 13
        number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        l.addView(number);

        i("Lifecycle", "adNum 3");
        int childCount1 =  layoutmain.getChildCount();

        layoutmain.addView(l,childCount1-1);

        Button deleteBtn = new Button(getActivity());
        deleteBtn.setEms(10);
        deleteBtn.setLayoutParams(params1);
        // Give button an ID
        deleteBtn.setId(R.id.reservedNamedId);

        deleteBtn.setCompoundDrawablesWithIntrinsicBounds(ic_delete,0,0,0);
        deleteBtn.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
        deleteBtn.setText(getString(R.string.btnDeleteName));

        l.addView(deleteBtn);
        // ecouteur de clic pour la suppression
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                i("Lifecycle", "adNum 4");
                LinearLayout layoutSup = (LinearLayout)v.getParent();
                i("Lifecycle", "adNum 5");
                layoutmain = getView().findViewById(R.id.myLinearMain);
                i("Lifecycle", "adNum 6");
                layoutmain.removeView(layoutSup);
                Nbre_Numero--;
            }
        });

        i("Lifecycle", "adNum fin");
    }
    // endregion

    //region  Ajout dynamique de numero
    public void addNum1(String text,View v){
        // Ajout du champ contenant le num de Tel
        i("Lifecycle", "adNum 0");

        //Bizarre un probleme lié aux vues
        layoutmain = v.findViewById(R.id.myLinearMain);
        i("Lifecycle", "adNum 1");
        LinearLayout.LayoutParams params, params1;
        params = params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // margin = 20
        params.setMargins(20,20,20,20);
        params.weight=5;
        params1.weight=1;

        i("Lifecycle", "adNum 2");

        l = new LinearLayout(getActivity());
        l.setId(R.id.numberNew);
        l.setLayoutParams(params);
        EditText number = new EditText (getActivity());
        number.setHint(getString(R.string.TextNumber));
        number.setText(text);
        number.setInputType(InputType.TYPE_CLASS_NUMBER);
        number.setLayoutParams(params1);
        // maxLength = 13
        number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        l.addView(number);

        i("Lifecycle", "adNum 3");
        int childCount1 =  layoutmain.getChildCount();

        layoutmain.addView(l,childCount1-1);

        Button deleteBtn = new Button(getActivity());
        deleteBtn.setEms(10);
        deleteBtn.setLayoutParams(params1);
        // Give button an ID
        deleteBtn.setId(R.id.reservedNamedId);

        deleteBtn.setCompoundDrawablesWithIntrinsicBounds(ic_delete,0,0,0);
        deleteBtn.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
        deleteBtn.setText(getString(R.string.btnDeleteName));

        l.addView(deleteBtn);
        // ecouteur de clic pour la suppression
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                i("Lifecycle", "adNum 4");
                LinearLayout layoutSup = (LinearLayout)v.getParent();
                i("Lifecycle", "adNum 5");
                layoutmain = getView().findViewById(R.id.myLinearMain);
                i("Lifecycle", "adNum 6");
                layoutmain.removeView(layoutSup);
                Nbre_Numero--;
            }
        });

        i("Lifecycle", "adNum fin");
    }
    // endregion

    //region  Gestion du menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        int i = Log.i("TEST Menu", "Je suis dans la méthode");
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.resetAction:
                remiseAzero(item);
                return true;
            case R.id.menu2:
                search(item);
                return true;
            case R.id.menu3:
                share(item);

            default:
                return super.onOptionsItemSelected(item);
        }

    }

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
        l = new LinearLayout(getActivity());
        layoutmain = getView().findViewById(R.id.myLinearMain);
        int Nbre_Elem = layoutmain.getChildCount();

        for (int i = Nbre_Elem - (Nbre_Numero+1); i < Nbre_Elem - 1; i++) {
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
        SP_VilleNaiss = getView().findViewById(R.id.SpinnerVilleNaiss);
        String ville_naiss = SP_VilleNaiss.getSelectedItem().toString();
        Uri site = Uri.parse("http://fr.wikipedia.org/?search=" + ville_naiss);
        Intent web = new Intent(Intent.ACTION_VIEW,site);
        String title =getResources().getString(R.string.chooser);
        Intent choose = Intent.createChooser(web, title);
        if (web.resolveActivity(getActivity().getPackageManager())!=null){
            startActivity(choose);}
        return true;
    }

    public void share(MenuItem item){
        SP_VilleNaiss = getView().findViewById(R.id.SpinnerVilleNaiss);
        String ville_naiss = SP_VilleNaiss.getSelectedItem().toString();
        Intent sendintent = new Intent(Intent.ACTION_SEND);
        sendintent.putExtra(Intent.EXTRA_TEXT,ville_naiss);
        sendintent.setType("text/plain");
        startActivity(Intent.createChooser(sendintent, "Send a simple text"));
    }
    // endregion


    //Sauvegarde des numéros
    public void saveNumero(ArrayList<String> liste) {
        EditText v1;
        l = new LinearLayout(getActivity());
        layoutmain = getView().findViewById(R.id.myLinearMain);
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

    public interface OnPrenomFragmentListener {
        void messageFromNomFragment(String text);
        void messageFromPrenomFragment(String text);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("Lifecycle", "Sauvegarde dans OnSave debut");
        outState.putCharSequence("nom", T_nom.getText().toString());
        outState.putCharSequence("prenom", T_prenom.getText().toString());
        outState.putCharSequence("date_naiss", T_DateNaiss.getText().toString());
        outState.putInt("departement", SP_Departement.getSelectedItemPosition());
        outState.putInt("ville_naiss", SP_VilleNaiss.getSelectedItemPosition());

        //Sauvegarde des numéros s'il y'en a
        outState.putInt("nbre_tel", Nbre_Numero);
        if(Nbre_Numero>0){
            saveNumero(L_Numeros = new ArrayList<>());
            outState.putStringArrayList("liste_num", L_Numeros);
            Log.i("Lifecycle", "Sauvegarde dans Onsave Num");
        }
        Log.i("Lifecycle", "Sauvegarde dans OnSave fin");
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            Log.i("Lifecycle", "Restitution dans OnCreate");
            // Recuperation de l'état de l'instance n'est pas tuée - rotation de l'ecran par exemple
            T_nom.setText(savedInstanceState.getCharSequence("nom"));
            T_prenom.setText(savedInstanceState.getCharSequence("prenom"));
            T_DateNaiss.setText(savedInstanceState.getCharSequence("date_naiss"));
            SP_VilleNaiss.setSelection(savedInstanceState.getInt("ville_naiss"));
            SP_Departement.setSelection(savedInstanceState.getInt("departement"));

            //Restauration des numéros s'il y'en a
            Nbre_Numero = savedInstanceState.getInt("nbre_tel");
            if (Nbre_Numero>0) {
                L_Numeros = new ArrayList<>();
                L_Numeros = savedInstanceState.getStringArrayList("liste_num");
                for (String value : L_Numeros) {
                    addNum1(value, getView());
                }
            }

        }

    }

    @Override
    public void onStop() {
        super.onStop();
        i("Lifecycle", "SAUVEGARDE DES DONNEES DANS OnStop");

        SharedPreferences preferences = getActivity().getSharedPreferences("A", MODE_PRIVATE);
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


    //endregion

    // This method insures that the Activity has actually implemented our
    // listener and that it isn't null.

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPrenomFragmentListener) {
            mCallback = (OnPrenomFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGreenFragmentListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

}
