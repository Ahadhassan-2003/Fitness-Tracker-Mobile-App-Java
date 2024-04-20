package com.example.fitnesstracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
public class StepCountService extends Service implements SensorEventListener {

    private static final long TIMEOUT_DURATION = 5000; // Timeout duration in milliseconds (adjust as needed)

    private SensorManager sensorManager;
    private Sensor stepSensor;
    private int previousStepCount;
    private int sessionSteps;
    private String currentUserId;
    private Handler timeoutHandler;
    private Runnable timeoutRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepSensor != null) {
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            retrieveStepCountFromFirestore(currentUserId);
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // Step counter sensor not available on this device
            Toast.makeText(this, "Step counter sensor not available", Toast.LENGTH_SHORT).show();
        }

        timeoutHandler = new Handler();
        timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                // Timeout occurred, assuming no movement detected
                if (sessionSteps > 0) {
                    updateStepCountInFirestore(String.valueOf(previousStepCount + sessionSteps));
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("empty_channel", "Empty Channel", NotificationManager.IMPORTANCE_LOW);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "empty_channel")
                .setSmallIcon(R.drawable.ic_custom_notification)
                .setContentTitle("App is running")
                .setContentText("Service is active")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setCategory(NotificationCompat.CATEGORY_SERVICE);

        // Start the service as a foreground service with the empty notification
        startForeground(1, builder.build());

        // Start the timeout runnable
        timeoutHandler.postDelayed(timeoutRunnable, TIMEOUT_DURATION);

        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (previousStepCount == 0) {
                // First step count reading after login, initialize sessionSteps
                previousStepCount = (int) event.values[0];
            } else {
                int currentStepCount = (int) event.values[0];
                sessionSteps = currentStepCount - previousStepCount;
            }

            // Reset the timeout runnable when steps are detected
            timeoutHandler.removeCallbacks(timeoutRunnable);
            timeoutHandler.postDelayed(timeoutRunnable, TIMEOUT_DURATION);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);

        // Remove the timeout callback when the service is destroyed
        timeoutHandler.removeCallbacks(timeoutRunnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void updateStepCountInFirestore(String stepCount) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(currentUserId);

        userRef.update("steps", stepCount)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Steps updated successfully
                        Toast.makeText(StepCountService.this, "Steps updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error updating steps
                        Toast.makeText(StepCountService.this, "Error updating steps: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void retrieveStepCountFromFirestore(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("steps")) {
                        String stepsString = documentSnapshot.getString("steps");
                        int previousSteps = 0;
                        try {
                            previousSteps = Integer.parseInt(stepsString);
                        } catch (NumberFormatException e) {
                            // Handle the case when the "steps" field is not a valid integer
                            Toast.makeText(StepCountService.this, "Invalid data format for 'steps' field", Toast.LENGTH_SHORT).show();
                        }
                        previousStepCount = previousSteps;

                        Toast.makeText(StepCountService.this, "Previous steps retrieved successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to retrieve the step count
                Toast.makeText(StepCountService.this, "Error retrieving step count: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}