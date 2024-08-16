package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class MainActivity3 extends AppCompatActivity {
    Button puase,play;
    TextView actmain3_text;
    SeekBar seekbar;
    MediaPlayer mediaPlayer;
    boolean usertouching=false;
    Handler handler = new Handler();
    Random random =new Random();
    int versnum = random.nextInt(6236)+1;
    String url = "https://api.alquran.cloud/v1/ayah/"+versnum+"/ar.alafasy";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onStart(){
        super.onStart();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main3);
        seekbar= findViewById(R.id.actmain3_seekbar);
       puase= findViewById(R.id.actmain3_pause);
      play = findViewById(R.id.actmain3_play);
      actmain3_text = findViewById(R.id.actmain3_text);
       mediaPlayer = new MediaPlayer();
      apireq(url);



      play.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
               play.setVisibility(View.INVISIBLE);
             puase.setVisibility(View.VISIBLE);
              mediaPlayer.start();
          }
      });

      puase.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

    puase.setVisibility(View.INVISIBLE);
    play.setVisibility(View.VISIBLE);
    mediaPlayer.pause();

          }
      });
      mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
          @Override
          public void onCompletion(MediaPlayer mediaPlayer) {
              puase.setVisibility(View.INVISIBLE);
              play.setVisibility(View.VISIBLE);
          }
      });
seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(usertouching){
        mediaPlayer.seekTo(i);
    }}

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        usertouching= true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        usertouching=false;

    }
});


    }



   @Override
    protected void onStop() {
       super.onStop();
       if(mediaPlayer.isPlaying())
           mediaPlayer.stop();

       mediaPlayer.release();
       mediaPlayer=null;
    }

    private void apireq(String url){
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        JsonObjectRequest jsonreq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());

                JSONObject data ;
                JSONObject data1 ;
                JSONObject edition ;
                try {
                    data1 =response.getJSONObject("data");
                    edition=data1.getJSONObject("edition");
                    data = response.getJSONObject("data");
                    String text = edition.getString("name");
                    actmain3_text.setText(text);
                    mediaPlayer.setDataSource(data.getString("audio").toString());
                    mediaPlayer.prepare();
                    seekbar.setMax(mediaPlayer.getDuration());
                   handler.postDelayed(runnable,70);


                } catch (IOException |JSONException e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                actmain3_text.setText("BAD INTERNET,retry!");

            }

        });
        queue.add(jsonreq);


    }
 Runnable runnable = new Runnable() {
     @Override
     public void run() {
         if( mediaPlayer!=null){
         seekbar.setProgress(mediaPlayer.getCurrentPosition());
         handler.postDelayed(runnable,70);
     }}
 };



}
