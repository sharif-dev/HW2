package com.example.sensors;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;


import androidx.annotation.Nullable;

public class SleepService extends Service{
    private SharedPreferences sharedPreferences;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        double sleepCriticalAngle = Double.parseDouble(sharedPreferences.getString("ANGLE_TEXT", "0.0"));
        DevicePolicyManager devicePolicyManager = MainActivity.devicePolicyManager;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SleepSensor sleepSensor = new SleepSensor(sensorManager, accelerometerSensor, sleepCriticalAngle);
        sleepSensor.setDevicePolicyManager(devicePolicyManager);
    }

    @Override
    public void onDestroy() {

    }
}
