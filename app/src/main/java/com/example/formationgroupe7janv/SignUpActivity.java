package com.example.formationgroupe7janv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.formationgroupe7janv.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText fullName, email, cin, phone, password;
    private Button btnSignUp;
    private TextView goToSignIn;
    private String fullNameString, emailString, cinString, phoneString, passwordString;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_PATTERN =
            "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullName = findViewById(R.id.fullNameSignUp);
        email = findViewById(R.id.emailSignUp);
        cin = findViewById(R.id.cinSignUp);
        phone = findViewById(R.id.phoneSignUp);
        password = findViewById(R.id.passwordSignUp);
        btnSignUp = findViewById(R.id.btnSignUp);
        goToSignIn = findViewById(R.id.goToSignIn);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        goToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });

        btnSignUp.setOnClickListener(v -> {
            if (validate()) {
                progressDialog.setMessage("Please wait...!");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sendEmailVerification();
                    }else {
                        Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });


    }

    private void sendEmailVerification() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(task -> {
               if (task.isSuccessful()) {
                   sendUserData();
                   Toast.makeText(this, "Regisration done ! Please check your email !", Toast.LENGTH_LONG).show();
                   firebaseAuth.signOut();
                   startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                   finish();
                   progressDialog.dismiss();
               }else {
                   Toast.makeText(this, "Failed !", Toast.LENGTH_SHORT).show();
                   progressDialog.dismiss();
               }
            });
        }
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myReferance = firebaseDatabase.getReference("Users");
        User user = new User(fullNameString,emailString,cinString,phoneString);
        myReferance.child(""+firebaseAuth.getUid()).setValue(user);
    }

    private boolean validate() {
        boolean result = false;
        fullNameString = fullName.getText().toString().trim();
        emailString = email.getText().toString().trim();
        cinString = cin.getText().toString().trim();
        phoneString = phone.getText().toString().trim();
        passwordString = password.getText().toString().trim();

        if (fullNameString.length()<7 ) {
            fullName.setError("Full name is invalid !");
        }
        else if(!isValidPattern(emailString, EMAIL_PATTERN)) {
            email.setError("Email is invalid !");
        } else if (cinString.length() != 8) {
            cin.setError("Cin is invalid !");
        } else if (phoneString.length() != 8) {
            phone.setError("Phone is invalid !");
        } else if (passwordString.length()<5) {
            password.setError("Password is invalid !");
        } else {
            result = true;
        }

        return result;
    }

    private boolean isValidPattern(String string, String patternn) {
        Pattern pattern = Pattern.compile(patternn);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

}