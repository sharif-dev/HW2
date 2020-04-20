package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class sleepActivity extends AppCompatActivity {
    private EditText angleText;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        angleText = findViewById(R.id.angle);
        angleText.setText(sharedPreferences.getString("ANGLE_TEXT", "0.0"));
        Button saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("ANGLE_TEXT", angleText.getText().toString());
                myEdit.apply();
            }
        });
    }
}
