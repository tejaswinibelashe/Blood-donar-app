package com.bloodlink.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bloodlink.app.util.SessionManager;
import com.google.android.material.button.MaterialButton;

public class PatientDashboardActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        sessionManager = new SessionManager(this);

        MaterialButton btnEmergency = findViewById(R.id.btnEmergencyRequest);
        btnEmergency.setOnClickListener(v -> {
            Toast.makeText(this, "Emergency Request Created! Notifying Donors...", Toast.LENGTH_LONG).show();
        });

        MaterialButton btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(v -> logout());
    }

    private void logout() {
        sessionManager.logoutUser();
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
