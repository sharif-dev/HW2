package com.example.sensors;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    private Switch heavySleepSwitch;
    private Switch vibrationSwitch;
    private Switch sleepSwitch;
    private Button heavySleepButton;
    private Button vibrationButton;
    private Button sleepButton;
    private double sleepCriticalAngle = 0.0;
    private SensorManager sensorManager;
    private Sensor gravitySensor;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;

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
                editor.apply();
            }
        });
        vibrationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("SWITCH2", isChecked);
                editor.apply();
            }
        });
        sleepSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("SWITCH3", sleepSwitch.isChecked());
                editor.apply();
                if (!sleepSwitch.isChecked()) {
                    devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                    try {
                        boolean active = devicePolicyManager.isAdminActive(componentName);
                        if (active) {
                            devicePolicyManager.removeActiveAdmin(componentName);
                        } else {
                            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!");
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        boolean active = devicePolicyManager.isAdminActive(componentName);
                        if (active) {
                            devicePolicyManager.removeActiveAdmin(componentName);
                        } else {
                            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!");
                            startActivityForResult(intent, 11);
                            if (sleepSwitch.isChecked()) {
                                activeSleepMode(sharedPreferences);
                            }
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void activeSleepMode(SharedPreferences sharedPreferences) {
        sleepCriticalAngle = Double.parseDouble(sharedPreferences.getString("ANGLE_TEXT", "0.0"));
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, DeviceAdmin.class);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SleepSensor sleepSensor = new SleepSensor(sensorManager, gravitySensor, sleepCriticalAngle);
        sleepSensor.setDevicePolicyManager(devicePolicyManager);
        sleepSensor.setComponentName(componentName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            if (resultCode == Activity.RESULT_OK) {
                sleepSwitch.setChecked(true);
            } else {
                sleepSwitch.setChecked(false);
            }
        }
    }
}
