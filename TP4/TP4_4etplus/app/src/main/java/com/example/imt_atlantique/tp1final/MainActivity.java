package com.example.imt_atlantique.tp1final;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static android.util.Log.i;


//region Activité principale

public class MainActivity extends AppCompatActivity
        implements Fragment_Main.OnPrenomFragmentListener, Fragment_EditPrenom.reponse{

    private static final String MAIN_FRAG_TAG = "green";
    Fragment_ViewNom NomFragment;
    Fragment_Main MainFragment;
    Fragment_EditPrenom PrenomFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null)
            MainFragment = (Fragment_Main) getSupportFragmentManager().getFragment(savedInstanceState, MAIN_FRAG_TAG);
        else{
            MainFragment = new Fragment_Main();
            i("Lifecycle", "Creation du frag OnCreate");
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentmain_container, MainFragment, MAIN_FRAG_TAG).commit();
        }
    }

    // The Activity handles receiving a message from one Fragment
    // and passing it on to the other Fragment

    public void messageFromNomFragment(String message) {
        NomFragment = new Fragment_ViewNom();
        Bundle bundle=new Bundle();
        bundle.putString("message",message);
        NomFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .remove (MainFragment)
                .addToBackStack (null)
                .add (R.id.fragmentmain_container, NomFragment)
                .commit() ;
    }

    public void messageFromPrenomFragment(String message) {
        PrenomFragment = new Fragment_EditPrenom();
        Bundle bundle=new Bundle();
        bundle.putString("sap",message);
        PrenomFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .remove (MainFragment)
                .addToBackStack (null)
                .add (R.id.fragmentmain_container, PrenomFragment)
                .commit () ;
    }

    public void messagereponse(String message) {
        MainFragment = new Fragment_Main();
        Bundle bundle=new Bundle();
        bundle.putString("reponse",message);
        MainFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .remove (PrenomFragment)
                .addToBackStack (null)
                .add (R.id.fragmentmain_container, MainFragment)
                .commit () ;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.i("Lifecycle", "Sauvegarde du frag dans OnSave");
        MainFragment = new Fragment_Main();
        MainFragment = (Fragment_Main) getSupportFragmentManager().findFragmentByTag(MAIN_FRAG_TAG);
        getSupportFragmentManager().putFragment(outState,MAIN_FRAG_TAG,MainFragment);
    }

}

//endregion



