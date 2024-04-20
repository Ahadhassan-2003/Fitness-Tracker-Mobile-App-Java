package com.example.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EnterBodyComposition extends AppCompatActivity {
    String currentUserId;
    AlertDialog progressDialog;
    EditText weightedittext,skeletalMuscleedittext,bodyFatedittext;
    Button enterdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_body_composition);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        weightedittext = findViewById(R.id.enterweight_edittext);
        skeletalMuscleedittext = findViewById(R.id.enterskeletalmuscle_edittext);
        bodyFatedittext = findViewById(R.id.enterbodyfat_edittext);
        enterdata = findViewById(R.id.finalenterbodycomposition_button);
        enterdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double weight = Double.parseDouble(weightedittext.getText().toString());
                double skeletalmuscle = Double.parseDouble(skeletalMuscleedittext.getText().toString());
                double bodyFat = Double.parseDouble(bodyFatedittext.getText().toString());
                updateBodyFatInFirestore(bodyFat);
                updateWeightInFirestore(weight);
                updateSkeletalMuscleInFirestore(skeletalmuscle);
                startActivity(new Intent(EnterBodyComposition.this,menu_screen.class));
            }
        });
    }
    private void showProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.custom_progress_dialog);
        builder.setCancelable(false); // Optional: Set whether the dialog is cancelable or not

        progressDialog = builder.create();
        progressDialog.show();
    }
    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    private void updateWeightInFirestore(double weight) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(currentUserId);

        userRef.update("weight", weight)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Weight updated successfully
                        Toast.makeText(EnterBodyComposition.this, "Weight updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error updating weight
                        Toast.makeText(EnterBodyComposition.this, "Error updating weight: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void updateBodyFatInFirestore(double bodyFat) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(currentUserId);

        userRef.update("bodyFat", bodyFat)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Body fat updated successfully
                        Toast.makeText(EnterBodyComposition.this, "Body fat updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error updating body fat
                        Toast.makeText(EnterBodyComposition.this, "Error updating body fat: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void updateSkeletalMuscleInFirestore(double skeletalMuscle) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(currentUserId);

        userRef.update("skeletalMuscle", skeletalMuscle)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Skeletal muscle updated successfully
                        Toast.makeText(EnterBodyComposition.this, "Skeletal muscle updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error updating skeletal muscle
                        Toast.makeText(EnterBodyComposition.this, "Error updating skeletal muscle: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}