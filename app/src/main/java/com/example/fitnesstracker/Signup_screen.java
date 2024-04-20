package com.example.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Signup_screen extends AppCompatActivity {

    EditText fullNameEditText,mobileNumberEditText,emailEditText,passwordEditText;
    Button registerButton_signup;
    private AlertDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        emailEditText = findViewById(R.id.edittext_email_signup);
        fullNameEditText = findViewById(R.id.edittext_fullname);
        mobileNumberEditText = findViewById(R.id.edittext_mobilenumber);
        passwordEditText = findViewById(R.id.edittext_password_signup);
        registerButton_signup = findViewById(R.id.button_register_signup);
        registerButton_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String fullName = fullNameEditText.getText().toString();
                String mobileNumber = mobileNumberEditText.getText().toString();
                if(TextUtils.isEmpty(fullName)){
                    Toast.makeText(Signup_screen.this,"Enter fullName",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mobileNumberEditText.getText().toString().equals("")){
                    Toast.makeText(Signup_screen.this,"Enter Mobile Number",Toast.LENGTH_SHORT).show();
                    return;
                }
                if((mobileNumberEditText.getText().toString().length() != 11)){
                    Toast.makeText(Signup_screen.this, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(mobileNumber.matches("[0-9]+")){
                        showProgressDialog();
                        firebaseAuth.createUserWithEmailAndPassword(email,password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(Signup_screen.this,"ACCOUNT CREATED",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Signup_screen.this,MainActivity.class));
                                        hideProgressDialog();

                                        firebaseFirestore.collection("users")
                                                .document(FirebaseAuth.getInstance().getUid())
                                                .set(new UserModel(fullName,mobileNumber,email,password,"0",80.0,75.0,20.0,1.0,0,0));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Signup_screen.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        hideProgressDialog();
                                    }
                                });
                    }
                    else
                        Toast.makeText(Signup_screen.this, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                }


                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Signup_screen.this,"Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Signup_screen.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

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
}