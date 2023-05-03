package com.example.authenticationassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateDataActivity extends AppCompatActivity {

    private EditText editTextUpdateEmail, editTextUpdateName, editTextUpdateMobile, editTextGender;
    private String txtEmail, txtFullName, txtMobile, txtGender;
    private FirebaseAuth authProfile;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        editTextUpdateEmail = findViewById(R.id.etEmail);
        editTextUpdateName = findViewById(R.id.etName);
        editTextUpdateMobile = findViewById(R.id.etPhone);
        editTextGender = findViewById(R.id.etGender);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        showProfile(firebaseUser);

        Button btnUploadProfilePic = findViewById(R.id.btnUploadProfileImg);
        btnUploadProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateDataActivity.this, UploadProfilePicture.class));
                finish();
            }
        });

//        Button btnUpdateEmail = findViewById(R.id.btnUpdateEmail);
//        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(UpdateDataActivity.this, UploadProfilePicture.class));
//                finish();
//            }
//        });

        Button btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(firebaseUser);
            }
        });
    }

    private void updateProfile(FirebaseUser firebaseUser) {

//        String mobile = editTextMobile.getText().toString().trim();

         if (TextUtils.isEmpty(txtFullName)){
            editTextUpdateName.setError("Fill in the fullName field");
//        } else if (TextUtils.isEmpty(txtEmail)) {
//            editTextUpdateEmail.setError("Fill in the email field");
        }else if (TextUtils.isEmpty(txtMobile)) {
             editTextUpdateMobile.setError("Fill in the mobile field");
        }else {
            txtEmail = editTextUpdateEmail.getText().toString();
            txtFullName = editTextUpdateName.getText().toString();
            txtMobile = editTextUpdateMobile.getText().toString();
            txtGender = editTextGender.getText().toString();

            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(txtEmail,txtFullName,txtMobile,txtGender);
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
            String userID = firebaseUser.getUid();
             if (progressDialog.isShowing()) {
                 progressDialog.dismiss();
             }

             referenceProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     if (task.isSuccessful()){
                         UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                 .setDisplayName(txtFullName).build();
                         firebaseUser.updateProfile(profileUpdate);

                         Toast.makeText(UpdateDataActivity.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(UpdateDataActivity.this, MainActivity.class);
                         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                         startActivity(intent);
                         finish();
                     }else {
                         Toast.makeText(UpdateDataActivity.this, "errorrrrrrrrrrrrrr", Toast.LENGTH_SHORT).show();
                     }
                     if (progressDialog.isShowing()) {
                         progressDialog.dismiss();
                     }
                 }

             });
        }
    }

    private void showProfile(FirebaseUser firebaseUser){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();

        String userIDRegister = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userIDRegister).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null){
                    txtEmail = firebaseUser.getEmail();
                    txtFullName = firebaseUser.getDisplayName();
                    txtMobile = readUserDetails.mobile;

                    editTextUpdateEmail.setText(txtEmail);
                    editTextUpdateName.setText(txtFullName);
                    editTextUpdateMobile.setText(txtMobile);
                }
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateDataActivity.this, "Something error", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });

    }
}