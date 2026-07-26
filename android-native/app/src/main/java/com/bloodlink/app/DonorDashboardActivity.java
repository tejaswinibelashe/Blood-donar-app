package com.bloodlink.app;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.bloodlink.app.R;

public class DonorDashboardActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_dashboard);

        SwitchCompat switchAvailability = findViewById(R.id.switchAvailability);
        switchAvailability.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String status = isChecked ? "Available" : "Unavailable";
            Toast.makeText(this, "Status updated to: " + status, Toast.LENGTH_SHORT).show();
            // Phase 2 logic: Retrofit call to update donor status
        });
        
        // RecyclerView setup for incoming requests would go here
    }
}
