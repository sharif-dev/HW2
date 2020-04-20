package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class sleep_activity extends AppCompatActivity {
    private EditText angleText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        angleText = findViewById(R.id.angle);
        saveButton = findViewById(R.id.save);
    }
}
