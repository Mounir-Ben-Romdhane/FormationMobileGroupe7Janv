package com.example.formationgroupe7janv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText email;
    private Button btnReset, btnBack;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        email = findViewById(R.id.emailReset);
        btnReset = findViewById(R.id.btnReset);
        btnBack = findViewById(R.id.btnBack);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        btnReset.setOnClickListener(v -> {
            progressDialog.setMessage("Please wait...!");
            progressDialog.show();
            firebaseAuth.sendPasswordResetEmail(email.getText().toString().trim()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Password reset email has been send successfully !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    finish();
                } else {
                    Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        });
    }
}