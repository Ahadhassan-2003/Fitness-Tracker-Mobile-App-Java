package com.example.fitnesstracker;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class Walking_Screen extends AppCompatActivity implements SensorEventListener {
    private String currentUserId;
    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;
    private Chronometer stopwatchChronometer;
    private TextView stepCountTextView;
    private int stepCount;
    private Button startStopButton;
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking_screen);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Initialize views
        stopwatchChronometer = findViewById(R.id.stopwatchChronometer_walking);
        stepCountTextView = findViewById(R.id.stepCountTextView_walking);
        startStopButton = findViewById(R.id.startStopButton_walking);
        resetButton = findViewById(R.id.resetButton_walking);

        // Initialize sensor manager and step detector sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        }

        // Set button click listeners
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

    @Override
    protected void onResume() {
        super.onResume();
        if (stepDetectorSensor != null) {
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (stepDetectorSensor != null) {
            sensorManager.unregisterListener(this, stepDetectorSensor);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            stepCount++;
            stepCountTextView.setText("Step Count: " + stepCount);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    private void startStopwatch() {
        stopwatchChronometer.setBase(SystemClock.elapsedRealtime());
        stopwatchChronometer.start();
        stopwatchChronometer.setActivated(true);
        startStopButton.setText("Stop");
        stepCount = 0;
        stepCountTextView.setText("Step Count: " + stepCount);
    }

    private void stopStopwatch() {
        stopwatchChronometer.stop();
        stopwatchChronometer.setActivated(false);
        startStopButton.setText("Start");
    }

    private void resetStopwatch() {
        stopwatchChronometer.stop();
        stopwatchChronometer.setBase(SystemClock.elapsedRealtime());
        stopwatchChronometer.setActivated(false);
        startStopButton.setText("Start");
        stepCount = 0;
        stepCountTextView.setText("Step Count: " + stepCount);
    }

    private void updateStepCountInFirestore(int stepCount) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(currentUserId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String existingSteps = documentSnapshot.getString("steps");
                    int previousSteps = 0;
                    try {
                        previousSteps = Integer.parseInt(existingSteps);
                    } catch (NumberFormatException e) {
                        // Handle the case when the "steps" field is not a valid integer
                        Toast.makeText(Walking_Screen.this, "Invalid data format for 'steps' field", Toast.LENGTH_SHORT).show();
                        return; // Exit the method if the format is invalid
                    }
                    int newSteps = previousSteps + stepCount;

                    userRef.update("steps", String.valueOf(newSteps))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Steps updated successfully
                                    Toast.makeText(Walking_Screen.this, "Steps updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Error updating steps
                                    Toast.makeText(Walking_Screen.this, "Error updating steps: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to retrieve the step count
                Toast.makeText(Walking_Screen.this, "Error retrieving step count: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}