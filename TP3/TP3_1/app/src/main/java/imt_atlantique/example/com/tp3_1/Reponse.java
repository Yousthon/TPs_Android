package imt_atlantique.example.com.tp3_1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Reponse extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reponse);

        TextView T_Question = findViewById(R.id.TV_QuestionPosee);
        T_Question.setText(T_Question.getText().toString()+" \n\n "+
                getIntent().getStringExtra("Question_posee"));

    }

    public void repondreQuestion(View v){
        EditText asw = findViewById(R.id.T_Reponse);
        setResult(1,new Intent(getApplicationContext(), MainActivity.class).
                putExtra("Reponse",asw.getText().toString()));
        finish();
    }

}
