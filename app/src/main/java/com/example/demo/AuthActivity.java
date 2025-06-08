package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.demo.databinding.ActivityAuthBinding;
import com.example.demo.model.AuthResponse;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.api.ApiService;
import com.example.demo.api.RetrofitClient;
import com.example.demo.utils.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {
    private static final String TAG = "AuthActivity";
    private ActivityAuthBinding binding;
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private TextView tvForgotPassword;
    private ApiService apiService;
    private SharedPreferencesManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize views
        etEmail = binding.etEmail;
        etPassword = binding.etPassword;
        btnLogin = binding.btnLogin;
        btnRegister = binding.btnRegister;
        tvForgotPassword = binding.tvForgotPassword;

        // Initialize API service and preferences manager
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        prefsManager = SharedPreferencesManager.getInstance(this);

        // Set click listeners
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Login button clicked");
                login();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Register button clicked");
                Intent intent = new Intent(AuthActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Forgot password clicked");
                Intent intent = new Intent(AuthActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Log.d(TAG, "Attempting login with email: " + email);

        if (email.isEmpty() || password.isEmpty()) {
            Log.w(TAG, "Login failed: Empty email or password");
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create login request
        LoginRequest request = new LoginRequest(email, password);

        // Call login API
        Log.d(TAG, "Calling login API");
        apiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d(TAG, "Login response received. Success: " + response.isSuccessful());
                if (response.isSuccessful() && response.body() != null) {
                    // Save token and user data
                    AuthResponse authResponse = response.body();
                    prefsManager.saveToken(authResponse.getToken());
                    prefsManager.saveUser(authResponse.getUser());

                    // Login successful
                    Log.d(TAG, "Login successful");
                    Toast.makeText(AuthActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    
                    // Navigate to main screen
                    Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Login failed
                    Log.e(TAG, "Login failed: " + response.message());
                    Toast.makeText(AuthActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                // Network error
                Log.e(TAG, "Login network error", t);
                Toast.makeText(AuthActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
