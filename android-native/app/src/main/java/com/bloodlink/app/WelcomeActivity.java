package com.bloodlink.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.bloodlink.app.R;
import com.google.android.material.button.MaterialButton;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        MaterialButton btnSignIn = findViewById(R.id.btnSignIn);
        MaterialButton btnSignUp = findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(v -> startActivity(new Intent(WelcomeActivity.this, LoginActivity.class)));
        btnSignUp.setOnClickListener(v -> startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class)));
    }
}
