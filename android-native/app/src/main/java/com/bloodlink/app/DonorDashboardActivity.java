package com.bloodlink.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.bloodlink.app.util.SessionManager;
import com.google.android.material.button.MaterialButton;

public class DonorDashboardActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_dashboard);

        sessionManager = new SessionManager(this);

        SwitchCompat switchAvailability = findViewById(R.id.switchAvailability);
        switchAvailability.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String status = isChecked ? "Available" : "Unavailable";
            Toast.makeText(this, "Status updated to: " + status, Toast.LENGTH_SHORT).show();
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
