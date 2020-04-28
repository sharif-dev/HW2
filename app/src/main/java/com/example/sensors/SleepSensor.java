package com.example.sensors;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SleepSensor implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private double angle;
    private DevicePolicyManager devicePolicyManager;

    public SleepSensor(SensorManager sensorManager, Sensor gravitySensor, double angle) {
        this.sensorManager = sensorManager;
        this.accelerometerSensor = gravitySensor;
        this.angle = angle;
        this.sensorManager.registerListener(SleepSensor.this, this.accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void setDevicePolicyManager(DevicePolicyManager devicePolicyManager) {
        this.devicePolicyManager = devicePolicyManager;
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
        if (Math.abs(theta) < angle * Math.PI / 180) {
            devicePolicyManager.lockNow();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
