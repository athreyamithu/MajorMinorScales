package com.mathreya.majorminorscales;

import android.content.Intent;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

public class SearchFragment extends Fragment {

    private Spinner tonicSpin, scaleSpin;
    private Button searchBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        tonicSpin = (Spinner) view.findViewById(R.id.tonic_spinner);
        scaleSpin = (Spinner) view.findViewById(R.id.scale_spinner);
        searchBtn = (Button) view.findViewById(R.id.search_button);
        //addListenerButton();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        addListenerButton();
    }

    public void addListenerButton() {
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), ScaleActivity.class);
                intent.putExtra("Tonic", String.valueOf(tonicSpin.getSelectedItem()));
                intent.putExtra("Scale", String.valueOf(scaleSpin.getSelectedItem()));
                startActivity(intent);
            }
        });

    }
}
