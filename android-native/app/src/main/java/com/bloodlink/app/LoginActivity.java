package com.bloodlink.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bloodlink.app.api.ApiClient;
import com.bloodlink.app.api.ApiService;
import com.bloodlink.app.util.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private TextInputEditText etEmail, etPassword;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);

        btnLogin.setOnClickListener(v -> performLogin());

        tvForgotPassword.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", email);
        loginData.put("password", password);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.loginUser(loginData).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try (ResponseBody responseBody = response.body()) {
                        String responseStr = responseBody.string();
                        JSONObject jsonObject = new JSONObject(responseStr);
                        JSONObject userObject = jsonObject.getJSONObject("user");
                        String role = userObject.getString("role");

                        sessionManager.createLoginSession(email, role);

                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        navigateToDashboard(role);
                        finish();
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing response", e);
                        Toast.makeText(LoginActivity.this, "Response Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMsg = "Invalid email or password";
                    if (response.code() == 404) errorMsg = "Service not found (404)";
                    else if (response.code() == 500) errorMsg = "Server error (500)";
                    Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Network error", t);
                String msg = "Connection Failed. Please check if backend is running.";
                if (t.getMessage() != null && t.getMessage().contains("CLEARTEXT")) {
                    msg = "Security Error: Cleartext traffic not permitted.";
                }
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
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
