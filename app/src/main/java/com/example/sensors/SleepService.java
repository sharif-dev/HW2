package com.example.sensors;

import android.app.IntentService;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;


import androidx.annotation.Nullable;

public class SleepService extends Service implements SensorEventListener {
    private SharedPreferences sharedPreferences;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private double sleepCriticalAngle;
    private DevicePolicyManager devicePolicyManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    public void start(Context context) {
//        Intent intent = new Intent(context, SleepService.class);
//        context.startService(intent);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
    }

//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//        if(intent != null) {
//
//        }
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sleepCriticalAngle = Double.parseDouble(sharedPreferences.getString("ANGLE_TEXT", "0.0"));
        devicePolicyManager = MainActivity.devicePolicyManager;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(SleepService.this, this.accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double dotProduct = -9.81 * sensorEvent.values[2];
        double a = Math.pow(sensorEvent.values[0], 2);
        double b = Math.pow(sensorEvent.values[1], 2);
        double c = Math.pow(sensorEvent.values[2], 2);
        double size = Math.sqrt(a + b + c);
        double cosine = dotProduct / (9.81 * size);
        double sinus = Math.sqrt(1 - Math.pow(cosine, 2));
        double theta = Math.asin(sinus);
        if (Math.abs(theta) < sleepCriticalAngle * Math.PI / 180) {
            devicePolicyManager.lockNow();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
