package com.example.sensors;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SleepSensor implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor gravitySensor;
    private double angle;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;

    public SleepSensor(SensorManager sensorManager, Sensor gravitySensor, double angle) {
        this.sensorManager = sensorManager;
        this.gravitySensor = gravitySensor;
        this.angle = angle;
        this.sensorManager.registerListener(SleepSensor.this , this.gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void setDevicePolicyManager(DevicePolicyManager devicePolicyManager) {
        this.devicePolicyManager = devicePolicyManager;
    }

    public void setComponentName(ComponentName componentName) {
        this.componentName = componentName;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double dotProduct = -9.81 * sensorEvent.values[2];
        double cosine = dotProduct / (9.81 * 9.81);
        double theta = Math.acos(cosine);
        if (theta < angle * Math.PI / 180) {
            devicePolicyManager.lockNow();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
