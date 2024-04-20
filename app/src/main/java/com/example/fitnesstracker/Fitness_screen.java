package com.example.fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Fitness_screen extends AppCompatActivity {

    Button pushups_button,reversecrunches_button,jumpingsquats_button,slitjumps_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_screen);

        pushups_button = findViewById(R.id.pushups_button);
        reversecrunches_button = findViewById(R.id.reversecrunches_button);
        jumpingsquats_button = findViewById(R.id.jumpingsquats_button);
        slitjumps_button = findViewById(R.id.slitjumps_button);

        reversecrunches_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Fitness_screen.this,Pushups_Screen.class));
            }
        });
        slitjumps_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Fitness_screen.this,Pushups_Screen.class));
            }
        });
        jumpingsquats_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Fitness_screen.this,Pushups_Screen.class));
            }
        });
        pushups_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Fitness_screen.this,Pushups_Screen.class));
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_fitness);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), menu_screen.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_fitness:
                    return true;
                case R.id.bottom_profile:
                    startActivity(new Intent(getApplicationContext(), User_profile.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });
    }
}