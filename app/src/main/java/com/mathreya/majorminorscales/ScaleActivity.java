package com.mathreya.majorminorscales;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
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
    private String preview;
    private boolean playOrPause;
    private ImageButton playPauseBtn;
    private MediaPlayer mPlayer;
    private boolean initialize = true;
    private ProgressBar progressBar;
    private TextView seconds;
    private Handler mHandler = new Handler();
    private ProgressTimeUtils utils = new ProgressTimeUtils();

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
        playPauseBtn = (ImageButton) findViewById(R.id.playButton);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        playPauseBtn.setOnClickListener(playPauseListener);
        seconds = (TextView) findViewById(R.id.seconds);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(100);
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
            String[] arr = songtitle.split(" - ");
            TextView trackText = (TextView) findViewById(R.id.trackname);
            trackText.setText(arr[0]);
            TextView artistText = (TextView) findViewById(R.id.artistname);
            artistText.setText(arr[1]);
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
            preview = track.preview_url;
            Log.i(songtitle, preview);
            ImageView imageView = (ImageView) findViewById(R.id.album_img);
            imageView.setImageBitmap(loadBitmap(track.album.images.get(1).url));
            final String url = track.external_urls.get("spotify");
            ImageButton spotifyButton = (ImageButton) findViewById(R.id.spotifyButton);
            spotifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        try {
            InputStream in = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private View.OnClickListener playPauseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!playOrPause) {
                playPauseBtn.setImageResource(android.R.drawable.ic_media_pause);
                //playPauseBtn.setBackgroundTintList(ColorStateList.valueOf((int) Long.parseLong("1ED760", 16)));
                if (initialize) new Player().execute(preview);
                if (!mPlayer.isPlaying()) mPlayer.start();
                playOrPause = true;
                updateProgressBar();
            } else {
                playPauseBtn.setImageResource(android.R.drawable.ic_media_play);
                if (mPlayer.isPlaying()) mPlayer.pause();
                playOrPause = false;
            }
        }
    };

    public void updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 100);
    }

    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            long currentDuration = mPlayer.getCurrentPosition();
            seconds.setText("" + utils.milliSecondsToTimer(currentDuration));
            int progress = utils.getProgressPercentage(currentDuration, 30000);
            progressBar.setProgress(progress);
            mHandler.postDelayed(this, 100);
        }
    };

    private class Player extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ScaleActivity.this);
            mProgressDialog.setTitle("MediaPlayer");
            mProgressDialog.setMessage("Loading...");
        }

        @Override
        protected Void doInBackground(String... args) {
            try {
                mPlayer.setDataSource(args[0]);
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playOrPause = false;
                        initialize = true;
                        playPauseBtn.setImageResource(android.R.drawable.ic_media_play);
                        mPlayer.stop();
                        mPlayer.reset();
                    }
                });
                mPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            mPlayer.start();
            initialize = false;
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
