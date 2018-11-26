package imt_atlantique.example.com.tp3_1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.id.B_TP31);
        setContentView(R.layout.activity_main);
    }

    public void poserQuestion(View v){
        Intent reponse  = new Intent(getApplicationContext(), Reponse.class);
        EditText question = findViewById(R.id.T_Question);
        reponse.putExtra( "Question_posee",question.getText().toString());
        startActivityForResult(reponse,0);

    }

    @SuppressLint("SetTextI18n")
    protected void onActivityResult(int requestCode, int resultCode, Intent msg) {
        super.onActivityResult(requestCode, resultCode, msg);
        Toast myToast = Toast.makeText(getApplicationContext(),
                "",Toast.LENGTH_LONG);
        //personalisation de notre toast
        myToast.setGravity(Gravity.CENTER_VERTICAL,0,-30);
        TextView tv = new TextView(this);
        tv.setBackgroundColor(Color.BLUE);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(25);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTypeface(Typeface.create("serif",Typeface.BOLD_ITALIC));
        tv.setText("Réponse à votre question : "+msg.getStringExtra("Reponse"));
        myToast.setView(tv);
        myToast.show();
    }
}
