package com.example.authenticationassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

        String email, mobile, name;

        TextView emailTv,phoneTv, nameTv, updateDataTv;
        ImageView imageView;

//        Button updateBtn = findViewById(R.id.save_changes_button);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailTv = findViewById(R.id.email);
        nameTv = findViewById(R.id.name);
        phoneTv = findViewById(R.id.phone);

        updateDataTv = findViewById(R.id.tvUpdateData);

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadProfilePicture.class);
                startActivity(intent);
            }
        });

        updateDataTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UpdateDataActivity.class);
                startActivity(intent);
            }
        });

//        FirebaseMessaging.getInstance().subscribeToTopic("Samar")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Log.e("tag", "Done");
//                        if (!task.isSuccessful()){
//                            Log.e("tag", "Failed");
//                        }
//                    }
//                });


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null){
            Toast.makeText(MainActivity.this, "something error", Toast.LENGTH_SHORT).show();
        }else {
            showUserProfile(firebaseUser);
        }
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null){
                    email = firebaseUser.getEmail();
                    name = firebaseUser.getDisplayName();
                    mobile = readUserDetails.mobile;

                    emailTv.setText(email);
                    nameTv.setText(name);
                    phoneTv.setText(mobile);

                    Uri uri = firebaseUser.getPhotoUrl();

                    Picasso.with(MainActivity.this).load(uri).into(imageView);
                }else {
                    Toast.makeText(MainActivity.this, "Something error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "something error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

 