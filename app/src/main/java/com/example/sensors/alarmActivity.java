package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.widget.TextView;

public class alarmActivity extends AppCompatActivity {

    private MediaPlayer player;
    private TextView time_txt;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliSeconds = 10000; //10min
    private Gyroscope gyroscope;
    private SharedPreferences sharedPreferences;
    private Double speedLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        speedLimit = Double.parseDouble(sharedPreferences.getString("SPEED_LIMIT", "1.0"));
        gyroscope = new Gyroscope(this);
        time_txt = findViewById(R.id.time_text);
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        player.start();
        countDownTimer = new CountDownTimer(timeLeftInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliSeconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                player.release();
            }
        }.start();
        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float rx, float ry, float rz) {
                // TODO: 5/1/20 
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
