package com.example.fitnesstracker;
import static java.lang.String.format;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;


import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class menu_screen extends AppCompatActivity {
    private TextView stepCountTextView;
    private Button stepButton,enterBodyCompositioButton,bodyCompositionButton,caloriesenterButton;
    private FloatingActionButton walkingButton,runningButton,cyclingButton;
    private String previousUserId;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        bodyCompositionButton = findViewById(R.id.body_composition_button);
        bodyCompositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu_screen.this, BodyComposition_Screen.class));
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, StepCountService.class));
        }

        // Initialize your views and variables
        stepCountTextView = findViewById(R.id.stepcounter_textview);
        stepButton = findViewById(R.id.stepsButton);

        walkingButton = (FloatingActionButton) findViewById(R.id.walkingButton);
        //runningButton = findViewById(R.id.runningButton);
        //cyclingButton = findViewById(R.id.cyclingButton);

        walkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle walking button click
                Intent walkingIntent = new Intent(menu_screen.this, Walking_Screen.class);
                startActivity(walkingIntent);
            }
        });

        cyclingButton = findViewById(R.id.cyclingButton);
        cyclingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu_screen.this,Walking_Screen.class));
            }
        });
        caloriesenterButton = findViewById(R.id.enter_food_button);
        caloriesenterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu_screen.this,EnterCalories_Screen.class));
            }
        });
        /*runningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle running button click
                Intent runningIntent = new Intent(menu_screen.this, Running_Screen.class);
                startActivity(runningIntent);
            }
        });

        cyclingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle cycling button click
                Intent cyclingIntent = new Intent(menu_screen.this, Cycling_Screen.class);
                startActivity(cyclingIntent);
            }
        });*/


        // Set up the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            // Handle navigation item selection
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    return true;
                case R.id.bottom_fitness:
                    startActivity(new Intent(getApplicationContext(), Fitness_screen.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_profile:
                    startActivity(new Intent(getApplicationContext(), User_profile.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

        // Set up the click listener for the step button
        stepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StepsMenu.class));
            }
        });
        runningButton = findViewById(R.id.runningButton);
        runningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu_screen.this, Walking_Screen.class));
            }
        });
        enterBodyCompositioButton = findViewById(R.id.body_composition_enter_button);
        enterBodyCompositioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EnterBodyComposition.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Retrieve and display the step count from Firestore
        retrieveStepCountFromFirestore();
        retrieveWeightFromFirestore();
    }
    private void retrieveStepCountFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(currentUserId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("steps")) {
                        String stepCount = documentSnapshot.getString("steps");
                        stepCountTextView.setText(stepCount);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to retrieve the step count
                Toast.makeText(menu_screen.this, "Error retrieving step count: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Sign out the user
        FirebaseAuth.getInstance().signOut();

        // Call super.onBackPressed() to allow the default back button behavior
        super.onBackPressed();
        Toast.makeText(menu_screen.this,"Signed Out",Toast.LENGTH_SHORT).show();
    }
    private void retrieveWeightFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(currentUserId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    UserModel userModel = documentSnapshot.toObject(UserModel.class);
                    if (userModel != null) {
                        double weight = userModel.getWeight();
                        bodyCompositionButton.setText(format("Body Composition:\n\t\t\t%.1fkg",weight));
                        // Use the retrieved weight value
                        Toast.makeText(menu_screen.this, "Retrieved weight: " + weight, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Document does not exist
                    Toast.makeText(menu_screen.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error retrieving weight
                Toast.makeText(menu_screen.this, "Error retrieving weight: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}