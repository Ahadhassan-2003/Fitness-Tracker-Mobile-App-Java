package com.example.fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class StepsMenu extends AppCompatActivity {
    private int stepCount;
    private double kilometers,kcal;
    private TextView stepTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_menu);

        SharedPreferences sharedPreferences = getSharedPreferences("StepCountPrefs", MODE_PRIVATE);
        int stepCount = sharedPreferences.getInt("stepCount", 0);

        kilometers = stepCount*0.0056;
        kcal = stepCount*0.063/1000;
        stepTextView = findViewById(R.id.stepmenu_stepcount_textview);
        stepTextView.setText("\n\n"+stepCount+" steps"+"\n"+kilometers+" km"+"\n"+kcal+" kcal");
    }
}