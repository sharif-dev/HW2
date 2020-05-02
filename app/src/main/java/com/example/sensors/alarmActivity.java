package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class alarmActivity extends AppCompatActivity {

    private MediaPlayer player;
    private TextView time_txt;
    private CountDownTimer countDownTimer;
    public long timeLeftInMilliSeconds; //10min
    private Gyroscope gyroscope;
    private SharedPreferences sharedPreferences;
    private Double speedLimit;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        speedLimit = Double.parseDouble(sharedPreferences.getString("SPEED_LIMIT", "1.0"));
        timeLeftInMilliSeconds = Long.parseLong(sharedPreferences.getString("TIME_REMAINING", "600000"));
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(timeLeftInMilliSeconds);
        gyroscope = new Gyroscope(this);
        time_txt = findViewById(R.id.time_text);
        player = MediaPlayer.create(this, R.raw.song1);
        player.start();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.start();
            }
        });
        countDownTimer = new CountDownTimer(timeLeftInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliSeconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                player.release();
                vibrator.cancel();
                finish();
            }
        }.start();
        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float rx, float ry, float rz) {
                if (Math.abs(rz) > Math.abs(speedLimit)) {
                    player.release();
                    vibrator.cancel();
                    Toast.makeText(alarmActivity.this, "Alarm off", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void updateTimer() {
        int minutes = (int) timeLeftInMilliSeconds / 60000;
        int seconds = (int) timeLeftInMilliSeconds % 60000 / 1000;

        String timeLeftText;
        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        time_txt.setText(timeLeftText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gyroscope.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gyroscope.unregister();
    }
}
