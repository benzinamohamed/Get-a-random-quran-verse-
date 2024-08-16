package com.example.myapplication;

import android.os.Bundle;

import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Random rand = new Random();
    int versenum = rand.nextInt(6236)+1;
    String url_ar="https://api.alquran.cloud/v1/ayah/"+versenum+"/quran-uthmani";
    String url_en="https://api.alquran.cloud/v1/ayah/"+versenum+"/en.asad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        TextView ar_ayat = findViewById(R.id.actmain2_ar_ayat);
        TextView en_ayat = findViewById(R.id.actmain2_en_ayat);
        ar_ayat.setTextSize(28);
        en_ayat.setTextSize(28);
        if(savedInstanceState!=null){
          ar_ayat.setText(savedInstanceState.getString("textkey"));
          en_ayat.setText(savedInstanceState.getString("textkey2"));


        }else {
          apiReq(ar_ayat,url_ar);
          apiReq(en_ayat,url_en);

      }


    }

    private void apiReq(TextView tv,String url) {



        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonreq =new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject data = response.getJSONObject("data");

                    String text = data.getString("text");
                    tv.setText(text);



                } catch (JSONException e) {

                }

            }
        },new Response.ErrorListener(){


            @Override
            public void onErrorResponse(VolleyError error) {
                tv.setText("Bad Interner conection,please connect");}
        });
        queue.add(jsonreq);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView tv = findViewById(R.id.actmain2_ar_ayat);
        TextView tv2 = findViewById(R.id.actmain2_en_ayat);
        outState.putString("textkey", tv.getText().toString());
        outState.putString("textkey2", tv2.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String saved = savedInstanceState.getString("textkey");
        TextView tv = findViewById(R.id.actmain2_ar_ayat);
        tv.setText(saved);

    }
}


