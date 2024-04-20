package com.example.fitnesstracker;

import static java.lang.String.format;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class User_profile extends AppCompatActivity {

    private ImageView profileImageView;
    private String currentUserId;
    private TextView nameTextView, emailTextView, weightTextView, skeletalMuscleTextView, bodyFatTextView, heightTextView, caloriesBurnedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), menu_screen.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_fitness:
                    startActivity(new Intent(getApplicationContext(), Fitness_screen.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_profile:
                    return true;
            }
            return false;
        });
        profileImageView = findViewById(R.id.profileImageView);
        nameTextView = findViewById(R.id.nameTextView);
        //emailTextView = findViewById(R.id.emailTextView);
        weightTextView = findViewById(R.id.weightTextView);
        //skeletalMuscleTextView = findViewById(R.id.skeletalMuscleTextView);
        //bodyFatTextView = findViewById(R.id.bodyFatTextView);
        heightTextView = findViewById(R.id.heightTextView);
        caloriesBurnedTextView = findViewById(R.id.caloriesBurnedTextView);

        // Retrieve user data from intent or database

        // Set user data to views
        retrieveFullNameFromFirestore();
        retrieveWeightFromFirestore();
        //skeletalMuscleTextView.setText("Skeletal Muscle: " + skeletalMuscle + " kg");
        //bodyFatTextView.setText("Body Fat: " + bodyFat + "%");
        retrieveHeightFromFirestore();
        retrieveCaloriesBurnedFromFirestore();
    }
    private void retrieveFullNameFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(currentUserId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("name")) {
                        String name = documentSnapshot.getString("name");
                        nameTextView.setText("Name: \n"+name);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to retrieve the step count
                Toast.makeText(User_profile.this, "Error retrieving step count: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void retrieveWeightFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(currentUserId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("weight")) {
                        Double weight = documentSnapshot.getDouble("weight");
                        weightTextView.setText(format("Weight:\n%.1f",weight));
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to retrieve the step count
                Toast.makeText(User_profile.this, "Error retrieving step count: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void retrieveHeightFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(currentUserId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("height")) {
                        Double height = documentSnapshot.getDouble("height");
                        heightTextView.setText(format("Height:\n %.2f m",height));
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to retrieve the step count
                Toast.makeText(User_profile.this, "Error retrieving step count: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void retrieveCaloriesBurnedFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(currentUserId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("caloriesburned")) {
                        Double caloriesburned = documentSnapshot.getDouble("caloriesburned");
                        caloriesBurnedTextView.setText(format("Calories Burned:\n%.1f",caloriesburned));
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to retrieve the step count
                Toast.makeText(User_profile.this, "Error retrieving step count: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}