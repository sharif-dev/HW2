package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    private Switch heavySleepSwitch;
    private Switch vibrationSwitch;
    private Switch sleepSwitch;
    private Button heavySleepButton;
    private Button vibrationButton;
    private Button sleepButton;
    private double sleepCriticalAngle = 0;
    private EditText angleText;
    private Button saveButton;
    private SensorManager sensorManager;
    private Sensor gravitySensor;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

    }

    public void initialize() {
        heavySleepSwitch = findViewById(R.id.switch1);
        vibrationSwitch = findViewById(R.id.switch2);
        sleepSwitch = findViewById(R.id.switch3);
        heavySleepButton = findViewById(R.id.first);
        vibrationButton = findViewById(R.id.second);
        sleepButton = findViewById(R.id.third);

        sleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.sleep_layout);
                sleepActivity();
            }
        });
    }

    public void sleepActivity() {
        angleText = findViewById(R.id.angle);
        angleText.setText(String.valueOf(sleepCriticalAngle));
        saveButton = findViewById(R.id.save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sleepCriticalAngle = Double.parseDouble(angleText.getText().toString());
                setContentView(R.layout.activity_main);
                initialize();
            }
        });
    }

    public void activeSleepMode() {
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, DeviceAdmin.class);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        boolean active = devicePolicyManager.isAdminActive(componentName);
        if (active) {
            devicePolicyManager.removeActiveAdmin(componentName);
        }
        else {
            SleepSensor sleepSensor = new SleepSensor(sensorManager, gravitySensor, sleepCriticalAngle);
            sleepSensor.setDevicePolicyManager(devicePolicyManager);
            sleepSensor.setComponentName(componentName);
            Intent intent = new Intent(DevicePolicyManager. ACTION_ADD_DEVICE_ADMIN ) ;
            intent.putExtra(DevicePolicyManager. EXTRA_DEVICE_ADMIN , componentName ) ;
            intent.putExtra(DevicePolicyManager. EXTRA_ADD_EXPLANATION , "You should enable the app!" ) ;
            startActivityForResult(intent , 11 ) ;
        }
    }


}
