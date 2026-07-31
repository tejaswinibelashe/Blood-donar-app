package com.bloodlink.app;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bloodlink.app.api.ApiClient;
import com.bloodlink.app.api.ApiService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private TextInputEditText etFullName, etEmail, etPhone, etPassword;
    private AutoCompleteTextView actvRole, actvBloodGroup;
    private MaterialButton btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        actvRole = findViewById(R.id.actvRole);
        actvBloodGroup = findViewById(R.id.actvBloodGroup);
        btnRegister = findViewById(R.id.btnRegister);

        setupDropdowns();

        btnRegister.setOnClickListener(v -> performRegistration());
    }

    private void setupDropdowns() {
        String[] roles = { "DONOR", "PATIENT", "HOSPITAL" };
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, roles);
        actvRole.setAdapter(roleAdapter);

        String[] bloodGroups = { "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" };
        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                bloodGroups);
        actvBloodGroup.setAdapter(bloodAdapter);
    }

    private void performRegistration() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String role = actvRole.getText().toString().trim();
        String bloodGroup = actvBloodGroup.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields (Name, Email, Password, Role)", Toast.LENGTH_LONG).show();
            return;
        }

        btnRegister.setEnabled(false);
        Toast.makeText(this, "Signing up...", Toast.LENGTH_SHORT).show();

        Map<String, Object> userData = new HashMap<>();
        userData.put("fullName", fullName);
        userData.put("email", email);
        userData.put("phone", phone);
        userData.put("password", password);
        userData.put("role", role);
        userData.put("bloodGroup", bloodGroup);
        userData.put("username", email);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.registerUser(userData).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                btnRegister.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    String errorText = "Registration failed (" + response.code() + ")";
                    try {
                        if (response.errorBody() != null) {
                            errorText = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading errorBody", e);
                    }
                    Toast.makeText(RegisterActivity.this, errorText, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                btnRegister.setEnabled(true);
                Log.e(TAG, "Network error", t);
                String msg = "Connection Failed. Ensure phone and laptop are on same Wi-Fi and backend is running.";
                if (t.getLocalizedMessage() != null) {
                    msg += "\n" + t.getLocalizedMessage();
                }
                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
