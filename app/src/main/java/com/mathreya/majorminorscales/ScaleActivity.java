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
        if (bundle.getString("Scale").equals("Major")) {
            new MajScaleNotes().execute(bundle.getString("Tonic"));
        } else if (bundle.getString("Scale").equals("Natural Minor")) {
            new NatMinScaleNotes().execute(bundle.getString("Tonic"));
        }
    }

    private class MajScaleNotes extends AsyncTask<String, Void, Void> {
        String notes;

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
            String json = null;
            try {
                InputStream is = getAssets().open("scales.json");
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

    private class NatMinScaleNotes extends AsyncTask<String, Void, Void> {
        String notes;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ScaleActivity.this);
            mProgressDialog.setTitle("Natural Minor Scales");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        protected Void doInBackground(String... args) {
            try {

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
