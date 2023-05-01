package com.thebitanpaul.cubuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

public class SplashScreen extends AppCompatActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //video view
        videoView = findViewById(R.id.bg);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.cu);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),Login.class));

            }
        },7000);
    }

    @Override
    protected void onResume(){
        videoView.resume();
        super.onResume();
    }

    @Override
    protected void onRestart(){
        videoView.start();
        super.onRestart();
    }

    @Override
    protected void onPause(){
        videoView.suspend();
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        videoView.stopPlayback();
        super.onDestroy();
    }
}