package com.example.sensors;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.sensors.Shake.ShakeActivity;
import com.example.sensors.Shake.shakeService;


public class MainActivity extends AppCompatActivity {
    private Switch heavySleepSwitch;
    private Switch vibrationSwitch;
    private Switch sleepSwitch;
    private Button heavySleepButton;
    private Button vibrationButton;
    private Button sleepButton;
    public static DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;
    private ComponentName componentName2;
    private Intent intent1;
    private Intent intent2;

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

        intent1 = new Intent(MainActivity.this, SleepService.class);
        intent2 = new Intent(MainActivity.this, shakeService.class);

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
                startActivity(new Intent(MainActivity.this, heavySleepActivity.class));
            }
        });
        vibrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShakeActivity.class));
            }
        });

        heavySleepSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("SWITCH1", isChecked);
                editor.apply();
            }
        });
        vibrationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("SWITCH2", vibrationSwitch.isChecked());
                editor.apply();
                if (!vibrationSwitch.isChecked()) {
                    devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                    try {
                        componentName2 = new ComponentName(MainActivity.this, DeviceAdmin.class);
                        boolean active = devicePolicyManager.isAdminActive(componentName2);
                        if (active) {
                            devicePolicyManager.removeActiveAdmin(componentName2);
                            stopService(intent2);
                        } else {
                            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName2);
                            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!");
                            stopService(intent2);
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                } else {
                    devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                    try {
                        componentName2 = new ComponentName(MainActivity.this, DeviceAdmin.class);
                        boolean active = devicePolicyManager.isAdminActive(componentName2);
                        if (active) {
                            devicePolicyManager.removeActiveAdmin(componentName2);
                        } else {
                            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName2);
                            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!");
                            startActivityForResult(intent, 12);
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
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
                        componentName = new ComponentName(MainActivity.this, DeviceAdmin.class);
                        boolean active = devicePolicyManager.isAdminActive(componentName);
                        if (active) {
                            devicePolicyManager.removeActiveAdmin(componentName);
                            stopService(intent1);
                        } else {
                            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!");
                            stopService(intent1);
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                } else {
                    devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                    try {
                        componentName = new ComponentName(MainActivity.this, DeviceAdmin.class);
                        boolean active = devicePolicyManager.isAdminActive(componentName);
                        if (active) {
                            devicePolicyManager.removeActiveAdmin(componentName);
                        } else {
                            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!");
                            startActivityForResult(intent, 11);
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            if (resultCode == Activity.RESULT_OK) {
                sleepSwitch.setChecked(true);
                SleepService.devicePolicyManager = devicePolicyManager;
                SleepService.componentName = componentName;
                startService(intent1);
            } else {
                sleepSwitch.setChecked(false);
            }
        }else if (requestCode == 12) {
            if (resultCode == Activity.RESULT_OK) {
                vibrationSwitch.setChecked(true);
                shakeService.mDevicePolicyManager = devicePolicyManager;
                shakeService.mComponentName = componentName2;
                startService(intent2);
            } else {
                vibrationSwitch.setChecked(false);
            }
        }
    }
}
