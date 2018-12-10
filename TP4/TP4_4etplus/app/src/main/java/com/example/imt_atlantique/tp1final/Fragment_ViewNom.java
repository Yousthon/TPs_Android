package com.example.imt_atlantique.tp1final;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment_ViewNom extends Fragment {

    private TextView mTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_nom, container, false);
        mTextView = v.findViewById(R.id.TextNom);
        Bundle bundle=getArguments();
        String message=bundle.getString("message");
        mTextView.setText(message);
        return v;
    }

}
