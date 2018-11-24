package com.example.imt_atlantique.tp1final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditPrenom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prenom);
        EditText prenom = (EditText) findViewById(R.id.editText5);

        Intent intent = getIntent();
        String pren= intent.getStringExtra("PRENOM");
        prenom.setText(pren);

    }


    public void valider_prenom(View view){

        EditText prenom = (EditText)findViewById(R.id.editText5);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //Intent intent = new Intent();
        intent.putExtra("prenom_ok", prenom.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }

    public void annuler_prenom(View view){
        String annule = new String("");
        Intent datewritten = new Intent(getApplicationContext(), MainActivity.class);
        datewritten.putExtra("modif_annule", annule);
        setResult(RESULT_CANCELED,datewritten);
        finish();
    }


}
