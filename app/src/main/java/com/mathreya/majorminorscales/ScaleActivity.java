package com.mathreya.majorminorscales;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.StrictMode;

public class ScaleActivity extends AppCompatActivity {
    ProgressDialog mProgressDialog;
    private OkHttpClient client = new OkHttpClient();
    private static final String CLIENT_ID = "4d62d8c89a1b49f58b2f75caca7ffb53";
    private static final String REDIRECT_URI = "major-minor-login://callback";
    private static final int REQUEST_CODE = 1997;
    private String tonic;
    private String scale;
    private String sc;
    private String json;
    private String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scale);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        tonic = bundle.getString("Tonic");
        scale = bundle.getString("Scale");
        TextView scaleHeader = (TextView) findViewById(R.id.scale_header);
        scaleHeader.setText(tonic + " " + scale);
        new ScaleNotes().execute(tonic, scale.toLowerCase().replace(" ", ""));
        sc = (scale.equals("Major")) ? "major" : "minor";
        login();

    }

    private void login() {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    public void readJSONStream() {
        try {
            InputStream is = getAssets().open("scales.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            //if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                access_token = response.getAccessToken();
                Log.i("ScaleActivity", access_token);
            //}
        }
        createSpotifyPreview(tonic, sc, access_token);
    }

    public void createSpotifyPreview(String tonic, String scale, String access_token) {
        try {
            readJSONStream();
            JSONObject obj = new JSONObject(json);
            String id = obj.getJSONObject(tonic).getString(scale + "spotifyids");
            String songtitle = obj.getJSONObject(tonic).getJSONArray(scale + "popularsongs").get(0).toString();
            TextView songtext = (TextView) findViewById(R.id.song_title);
            songtext.setText(songtitle);
            SpotifyApi api = new SpotifyApi();
            api.setAccessToken(access_token);
            SpotifyService service = api.getService();
            Track track = service.getTrack(id);
//            Headers mAuthHeader = Headers.of("Authorization", "Bearer " + access_token);
//            Request request = new Request.Builder()
//                    .get()
//                    .url("https://api.spotify.com/v1/tracks/" + id)
//                    .headers(mAuthHeader)
//                    .build();
//            Response response = client.newCall(request).execute();
//            JSONObject res = new JSONObject(response.body().string());
//            Log.i("ScaleActivity Spotify", res.getString("preview_url"));
            String preview = track.preview_url;
            Log.i(songtitle, preview);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ScaleNotes extends AsyncTask<String, Void, Void> {
        String notes;
        String json;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ScaleActivity.this);
            mProgressDialog.setTitle("Loading...");
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
