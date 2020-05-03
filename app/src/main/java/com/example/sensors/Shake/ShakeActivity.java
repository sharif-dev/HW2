package com.example.sensors.Shake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sensors.R;

public class ShakeActivity extends AppCompatActivity {
    private EditText speedText;
    public SharedPreferences sharedPreferences;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        speedText = (EditText) findViewById(R.id.speed);
        speedText.setText(sharedPreferences.getString("SPEED_TEXT", "0.0"));
        button = (Button) findViewById(R.id.shakeSave);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("SPEED_TEXT", speedText.getText().toString());
                myEdit.apply();
                Toast.makeText(ShakeActivity.this, "Speed saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
