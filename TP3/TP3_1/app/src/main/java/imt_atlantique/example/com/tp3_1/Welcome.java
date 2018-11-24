package imt_atlantique.example.com.tp3_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.tp3_1_name);
        setContentView(R.layout.activity_welcome);
    }

    public void choisirTP31(View v){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void choisirTP32(View v){

    }
}
