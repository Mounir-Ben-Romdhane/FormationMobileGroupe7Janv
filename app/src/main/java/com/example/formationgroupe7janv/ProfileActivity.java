package com.example.formationgroupe7janv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private EditText fullName, email, cin, phone;
    private Button btnEdit, btnLogOut;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullName = findViewById(R.id.fullNameProfile);
        email = findViewById(R.id.emailProfile);
        cin = findViewById(R.id.cinProfile);
        phone = findViewById(R.id.phoneProfile);
        btnEdit = findViewById(R.id.btnEditProfile);
        btnLogOut = findViewById(R.id.btnSignOut);


        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        user = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference().child("Users").child(user.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullName.setText( snapshot.child("fullName").getValue().toString() );
                email.setText( snapshot.child("email").getValue().toString() );
                cin.setText( snapshot.child("cin").getValue().toString() );
                phone.setText( snapshot.child("phone").getValue().toString() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "error!", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogOut.setOnClickListener(v -> {
            firebaseAuth.signOut();
            SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("rememberMe", false);
            editor.apply();
            startActivity(new Intent(ProfileActivity.this, SignInActivity.class));
        });



        btnEdit.setOnClickListener(v -> {

            btnEdit.setText("save");
            fullName.setFocusableInTouchMode(true);
            cin.setFocusableInTouchMode(true);
            phone.setFocusableInTouchMode(true);

            btnEdit.setOnClickListener(v1 -> {

                String updatedFullName = fullName.getText().toString().trim();
                String updatedCIN = cin.getText().toString().trim();
                String updatedPhone = phone.getText().toString().trim();

                databaseReference.child("fullName").setValue(updatedFullName);
                databaseReference.child("cin").setValue(updatedCIN);
                databaseReference.child("phone").setValue(updatedPhone);

                Toast.makeText(this, "Your data has been changed successfully !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));

            });

        });

    }
}