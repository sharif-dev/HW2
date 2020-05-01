package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
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
}
