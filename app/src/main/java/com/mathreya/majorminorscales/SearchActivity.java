package com.mathreya.majorminorscales;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    private Spinner tonicSpin, scaleSpin;
    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        addListenerButton();
    }

    public void addListenerButton() {
        tonicSpin = (Spinner) findViewById(R.id.tonic_spinner);
        scaleSpin = (Spinner) findViewById(R.id.scale_spinner);
        searchBtn = (Button) findViewById(R.id.search_button);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Toast.makeText(SearchActivity.this,
                        "Selected "+String.valueOf(tonicSpin.getSelectedItem())
                        +" "+String.valueOf(scaleSpin.getSelectedItem()),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SearchActivity.this, ScaleActivity.class);
                intent.putExtra("Tonic", String.valueOf(tonicSpin.getSelectedItem()));
                intent.putExtra("Scale", String.valueOf(scaleSpin.getSelectedItem()));
                startActivity(intent);
            }
        });

    }
}
