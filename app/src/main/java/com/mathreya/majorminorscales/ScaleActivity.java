package com.mathreya.majorminorscales;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.HashMap;

public class ScaleActivity extends AppCompatActivity {
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scale);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TextView scaleHeader = (TextView) findViewById(R.id.scale_header);
        scaleHeader.setText(bundle.getString("Tonic") + " " + bundle.getString("Scale"));
        new ScaleNotes().execute(bundle.getString("Tonic"), bundle.getString("Scale").toLowerCase().replace(" ", ""));
    }

    private class ScaleNotes extends AsyncTask<String, Void, Void> {
        String notes;
        String json;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ScaleActivity.this);
            mProgressDialog.setTitle("Major Scales");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        protected Void doInBackground(String... args) {
            try {
                InputStream is = getAssets().open("scales.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");

                JSONObject obj = new JSONObject(json);
                String tonic = args[0];
                String scale = args[1];
                JSONObject ton = obj.getJSONObject(tonic);
                Log.i(tonic + " " + scale, ton.getString(scale));
                notes = ton.getString(scale);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
            TextView txt = (TextView) findViewById(R.id.scale_notes);
            txt.setText(notes);
        }
    }
}
