package com.example.formationgroupe7janv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private TextView goToSignUp, goToForgetPass;
    private EditText email,password;
    private Button btnSignIn;
    private CheckBox rememberMe;
    private String emailString, passwordString;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();

        goToSignUp = findViewById(R.id.goToSignUpAct);
        goToForgetPass = findViewById(R.id.goToForgetPass);
        email = findViewById(R.id.emailSignIn);
        password = findViewById(R.id.passwordSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        rememberMe = findViewById(R.id.rememberMe);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        goToSignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });

        goToForgetPass.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, ForgetPasswordActivity.class));
        });

        SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
        boolean checkBox = preferences.getBoolean("rememberMe", false);
        if (checkBox) {
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
        }

        rememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("rememberMe", true);
                editor.apply();
            } else {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("rememberMe", false);
                editor.apply();
            }
        });


        btnSignIn.setOnClickListener(v -> {
            progressDialog.setMessage("Please wait...!");
            progressDialog.show();
            emailString = email.getText().toString().trim();
            passwordString = password.getText().toString().trim();
            firebaseAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(task -> {
               if (task.isSuccessful()) {
                   checkEmailVerification();
               }else {
                   Toast.makeText(this, "Invalid credentials !", Toast.LENGTH_SHORT).show();
                   progressDialog.dismiss();
               }
            });
        });

    }

    private void checkEmailVerification() {
        SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
        FirebaseUser logedUser = firebaseAuth.getCurrentUser();
        if (logedUser.isEmailVerified()) {
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("emailUser", logedUser.getEmail());
            editor.apply();
            progressDialog.dismiss();
            finish();
        }else {
            Toast.makeText(this, "Please verify your account !", Toast.LENGTH_SHORT).show();
            logedUser.sendEmailVerification();
            firebaseAuth.signOut();
            progressDialog.dismiss();
        }
    }
}