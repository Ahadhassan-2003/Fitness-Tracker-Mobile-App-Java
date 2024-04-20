package com.example.fitnesstracker;

import static java.lang.String.format;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Pushups_Screen extends AppCompatActivity {

    private Chronometer stopwatchChronometer;
    private Button startStopButton;
    private TextView caloriestv;
    private Button resetButton;
    private String currentUserId;
    private double caloriesburned = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushups_screen);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        stopwatchChronometer = findViewById(R.id.stopwatchChronometer_pushups);
        startStopButton = findViewById(R.id.startStopButton_pushups);
        resetButton = findViewById(R.id.resetButton_pushups);
        caloriestv = findViewById(R.id.caloriesburnedtextview_pushups);


        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopwatchChronometer.isActivated()) {
                    stopStopwatch();
                } else {
                    startStopwatch();
                }
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStopwatch();
            }
        });
    }
    private void startStopwatch() {
        stopwatchChronometer.setBase(SystemClock.elapsedRealtime());
        stopwatchChronometer.start();
        stopwatchChronometer.setActivated(true);
        startStopButton.setText("Stop");
    }
    private void stopStopwatch() {
        stopwatchChronometer.stop();
        stopwatchChronometer.setActivated(false);
        startStopButton.setText("Start");
        long elapsedMillis = SystemClock.elapsedRealtime() - stopwatchChronometer.getBase();
        int elapsedSeconds = (int) (elapsedMillis / 1000);
        caloriesburned += (elapsedSeconds*0.36);
        updateCaloriesBurnedInFirestore(caloriesburned);
        caloriestv.setText(format("CaloriesBurned: %.1f cal",caloriesburned));
    }

    private void resetStopwatch() {
        stopwatchChronometer.stop();
        stopwatchChronometer.setBase(SystemClock.elapsedRealtime());
        stopwatchChronometer.setActivated(false);
        startStopButton.setText("Start");

    }
    private void updateCaloriesBurnedInFirestore(double caloriesburned) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(currentUserId);

        userRef.update("caloriesburned", caloriesburned)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Weight updated successfully
                        Toast.makeText(Pushups_Screen.this, "Weight updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error updating weight
                        Toast.makeText(Pushups_Screen.this, "Error updating weight: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}