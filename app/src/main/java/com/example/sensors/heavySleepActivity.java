package com.example.sensors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class heavySleepActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private TextView time;
    private EditText speed_limit;
    private Button set, cancel, save;
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heavy_sleep);
        speed_limit = findViewById(R.id.speed);
        set = findViewById(R.id.set);
        cancel = findViewById(R.id.cancel);
        save = findViewById(R.id.save_button);
        time = findViewById(R.id.alram_text_view);
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("SPEED_LIMIT", speed_limit.getText().toString());
                myEdit.apply();
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        time.setText("Hour " + hourOfDay + " Minute: " + minute);
    }
}
