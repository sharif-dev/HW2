package com.example.sensors.Shake;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

public class shakeService extends Service implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private SharedPreferences sharedPreferences;
    private double sleepCriticalSpeed;
    public static DevicePolicyManager mDevicePolicyManager;
    public static ComponentName mComponentName;

    private float mAccelCurrent;
    private float mAccelLast;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        sleepCriticalSpeed = Double.parseDouble(sharedPreferences.getString("SPEED_TEXT", "8"));

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mAccelCurrent = 9.80999660f;
        mAccelLast = 9.80999660f;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;

            if (Math.abs(delta * 1000000) >= sleepCriticalSpeed) {
                if (mDevicePolicyManager != null) {
                    mDevicePolicyManager.lockNow();
                } else {
                    mDevicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                    mDevicePolicyManager.lockNow();
                }
            }
        }
    }
}
