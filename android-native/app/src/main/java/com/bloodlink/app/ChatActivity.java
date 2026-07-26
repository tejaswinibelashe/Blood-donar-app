package com.bloodlink.app;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bloodlink.app.R;

public class ChatActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        EditText etMessage = findViewById(R.id.etMessage);
        ImageButton btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> {
            String msg = etMessage.getText().toString();
            if(!msg.isEmpty()) {
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
                etMessage.setText("");
                // In full implementation: Update RecyclerView & send to Spring Boot backend
            }
        });
    }
}
