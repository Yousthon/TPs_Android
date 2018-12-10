package com.example.imt_atlantique.tp1final;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.util.Log.i;

public class Fragment_EditPrenom extends Fragment {

    private EditText Prenom;
    private reponse mCallback;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_prenom, container, false);

        Prenom = v.findViewById(R.id.editTextPrenom);
        Bundle bundle=getArguments();
        String message=bundle.getString("sap");
        Prenom.setText(message);

        i("Lifecycle", "avant btn send");

        Button send = v.findViewById(R.id.BtnValiderPrenom);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText Prenom2 = getView().findViewById(R.id.editTextPrenom);
                String message = Prenom2.getText().toString();
                mCallback.messagereponse(message);

            }
        });
        return v;
    }


    public interface reponse {
        void messagereponse(String text);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Fragment_EditPrenom.reponse) {
            mCallback = (Fragment_EditPrenom.reponse) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement reponse");
        }
    }


    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
