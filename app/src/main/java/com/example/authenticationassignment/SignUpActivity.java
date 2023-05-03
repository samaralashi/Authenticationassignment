package com.example.authenticationassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText editTextSignupEmail = findViewById(R.id.signup_email);
        EditText editTextSignupPassword = findViewById(R.id.signup_password);
        EditText editTextFullName = findViewById(R.id.signup_fullName);
        EditText editTextMobile = findViewById(R.id.signup_mobile);
        EditText editTextGender = findViewById(R.id.signup_gender);
        Button signupBtn = findViewById(R.id.signup_btn);
        TextView loginTxt = findViewById(R.id.loginText);

        loginTxt.setOnClickListener(view1 -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));


        signupBtn.setOnClickListener(view -> {
            String email = editTextSignupEmail.getText().toString().trim();
            String pass = editTextSignupPassword.getText().toString().trim();
            String name = editTextFullName.getText().toString().trim();
            String mobile = editTextMobile.getText().toString().trim();
            String gender = editTextGender.getText().toString().trim();

            if (TextUtils.isEmpty(email)){
                editTextSignupEmail.setError("Fill in the email field");
            } else if (TextUtils.isEmpty(name)){
                editTextFullName.setError("Fill in the fullName field");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editTextSignupEmail.setError("Valid email is required");
                editTextFullName.requestFocus();
            } else if (TextUtils.isEmpty(pass)) {
                editTextSignupPassword.setError("Password is required");

            }else if (TextUtils.isEmpty(mobile)) {
                editTextMobile.setError("Fill in the mobile field");
            }else {
                registerUser(name,email,pass,mobile,gender);
            }
        });
    }

    private void registerUser(String name, String email, String pass, String mobile, String gender) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(SignUpActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this, "User Register Successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            firebaseUser.updateProfile(profileChangeRequest);

                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(email,name,mobile,gender);
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");

                            reference.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        firebaseUser.sendEmailVerification();
                                        Toast.makeText(SignUpActivity.this, "User registered successfully, PLease verify your email", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(SignUpActivity.this, "User registered failed", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });


//                            firebaseUser.sendEmailVerification();


                        }
//                        else{
//                            Toast.makeText(SignUpActivity.this, "errorrrrrrrrrrrrrrrrr", Toast.LENGTH_SHORT).show();
//
//                        }
                    }
                });
    }
}