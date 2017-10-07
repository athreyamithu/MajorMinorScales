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

public class ScaleActivity extends AppCompatActivity {
    ProgressDialog mProgressDialog;
//    String majorURL = "https://www.pianoscales.org/major.html";
    String majorURL = "http://www.piano-keyboard-guide.com/major-scales.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scale);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TextView scaleHeader = (TextView) findViewById(R.id.scale_header);
        scaleHeader.setText(bundle.getString("Tonic") + " " + bundle.getString("Scale"));
        new ScaleNotes().execute(bundle.getString("Tonic"));
    }

    private class ScaleNotes extends AsyncTask<String, Void, Void> {
        String notes;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ScaleActivity.this);
            mProgressDialog.setTitle("Major Minor Scales");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        protected Void doInBackground(String... args) {
            try {
                Document document = Jsoup.connect(majorURL).get();
                String text = document.body().text();
                Log.i("AsyncTask_Notes", text);
                String tonic = args[0];
                Elements spa = document.getElementsMatchingText("^" + tonic + ".*" + tonic + "$");
                notes = spa.first().ownText().replace(": ", "");
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
