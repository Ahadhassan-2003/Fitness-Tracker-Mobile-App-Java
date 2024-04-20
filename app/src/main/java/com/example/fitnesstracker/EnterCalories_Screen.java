package com.example.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EnterCalories_Screen extends AppCompatActivity {
    EditText caloriestv;
    Button enter;
    private String currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_calories_screen);

        caloriestv = findViewById(R.id.entercalorieseaten);
        enter = findViewById(R.id.entercalorieseatenbutton);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double calories = Double.parseDouble(caloriestv.getText().toString());
                    updateCalorieseatenInFirestore(calories);
                } catch (NumberFormatException e) {
                    Toast.makeText(EnterCalories_Screen.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateCalorieseatenInFirestore(double calorieseaten) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(currentUserId);

        userRef.update("calorieseaten", calorieseaten)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Calories updated successfully

                        Toast.makeText(EnterCalories_Screen.this, "Calories updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error updating calories
                        Toast.makeText(EnterCalories_Screen.this, "Error updating Calories: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}