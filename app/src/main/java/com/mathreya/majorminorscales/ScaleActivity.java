package com.mathreya.majorminorscales;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ScaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scale);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TextView scaleHeader = (TextView) findViewById(R.id.scale_header);
        scaleHeader.setText(bundle.getString("Tonic") + " " + bundle.getString("Scale"));
    }
}
