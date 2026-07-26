package com.bloodlink.app;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import com.bloodlink.app.R;

public class PatientDashboardActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        MaterialButton btnEmergency = findViewById(R.id.btnEmergencyRequest);
        btnEmergency.setOnClickListener(v -> {
            Toast.makeText(this, "Emergency Request Created! Notifying Donors...", Toast.LENGTH_LONG).show();
            // Phase 2 logic: Retrofit POST request to backend
        });
        
        // RecyclerView setup would go here
    }
}
