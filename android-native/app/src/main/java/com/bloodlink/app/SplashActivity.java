package com.bloodlink.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import com.bloodlink.app.util.SessionManager;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SessionManager sessionManager = new SessionManager(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (sessionManager.isLoggedIn()) {
                navigateToDashboard(sessionManager.getUserRole());
            } else {
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            }
            finish();
        }, 2000);
    }

    private void navigateToDashboard(String role) {
        Intent intent;
        if ("DONOR".equals(role)) {
            intent = new Intent(this, DonorDashboardActivity.class);
        } else {
            intent = new Intent(this, PatientDashboardActivity.class);
        }
        startActivity(intent);
    }
}
