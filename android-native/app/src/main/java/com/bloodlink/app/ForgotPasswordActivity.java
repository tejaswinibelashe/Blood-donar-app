package com.bloodlink.app;

import android.os.Bundle;
import android.widget.Toast;

import com.bloodlink.app.R;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class ForgotPasswordActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        MaterialButton btnSendOtp = findViewById(R.id.btnSendOtp);

        btnSendOtp.setOnClickListener(v -> Toast.makeText(this, "OTP Sent to Email", Toast.LENGTH_SHORT).show());
    }
}
