package com.example.fitnesstracker;

import static java.lang.String.format;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BodyComposition_Screen extends AppCompatActivity {

    TextView weighttv,bmitv;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_composition_screen);
        weighttv = findViewById(R.id.weight_textview);
        bmitv = findViewById(R.id.bmi_textview);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        retrieveWeightFromFirestore();
        bmitv.setText("BMI:\n\n"+18.5);
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
                        weighttv.setText(format("Weight:\n%.1f",weight));
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to retrieve the step count
                Toast.makeText(BodyComposition_Screen.this, "Error retrieving step count: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}