package imt_atlantique.example.com.tp_4_3;

import android.app.FragmentManager;
import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements EditFragment.OnGreenFragmentListener{
    DisplayFragment Fragment_display;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       Fragment_display = (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.frag);
    }

    // L'activité recupere le message provenant du fragment
    // et le passe a l'aure fragment
    //
    public void onDisplayFragment(String message) {
        Fragment_display.ReceiveMessage(message);
    }
}
