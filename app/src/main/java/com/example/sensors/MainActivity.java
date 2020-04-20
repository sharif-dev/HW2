package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private Switch heavySleepSwitch;
    private Switch vibrationSwitch;
    private Switch sleepSwitch;
    private Button heavySleepButton;
    private Button vibrationButton;
    private Button sleepButton;
    private double sleepCriticalAngle = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        heavySleepSwitch = findViewById(R.id.switch1);
        vibrationSwitch = findViewById(R.id.switch2);
        sleepSwitch = findViewById(R.id.switch3);
        heavySleepButton = findViewById(R.id.first);
        vibrationButton = findViewById(R.id.second);
        sleepButton = findViewById(R.id.third);
        final SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        sleepCriticalAngle = Double.parseDouble(sharedPreferences.getString("ANGLE_TEXT", "0.0"));
        heavySleepSwitch.setChecked(sharedPreferences.getBoolean("SWITCH1", false));
        vibrationSwitch.setChecked(sharedPreferences.getBoolean("SWITCH2", false));
        sleepSwitch.setChecked(sharedPreferences.getBoolean("SWITCH3", false));
        sleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, sleepActivity.class));
            }
        });
        heavySleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 4/20/20  
            }
        });
        vibrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 4/20/20  
            }
        });

        heavySleepSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("SWITCH1", isChecked);
            }
        });
        vibrationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("SWITCH2", isChecked);
            }
        });
        sleepSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("SWITCH3", isChecked);
            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
