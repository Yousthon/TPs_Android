package imt_atlantique.example.com.tp_4_3;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayFragment extends Fragment {

    private TextView mTextView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_display, container, false);
        mTextView = v.findViewById(R.id.TextNom);
        return v;
    }

    // methode public permettant au fragment de recuperer les donnes envoyés par l'autre fragment

    public void ReceiveMessage(String message) {
        mTextView.setText(message);
    }
}

