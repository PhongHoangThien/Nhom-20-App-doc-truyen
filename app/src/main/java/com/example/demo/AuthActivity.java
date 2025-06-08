package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.demo.databinding.ActivityAuthBinding;
import com.example.demo.model.AuthenticationModel;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.LoginResponse;
import com.example.demo.model.RegisterRequest;
import com.example.demo.model.RegisterResponse;
import com.example.demo.api.ApiService;
import com.example.demo.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {
    private static final String TAG = "AuthActivity";
    private ActivityAuthBinding binding;
    private boolean isLoginMode = true;
    private ApiService apiService;
    private AuthenticationModel authModel;
    private boolean isNavigating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize API service
        apiService = RetrofitClient.getInstance().getApiService();
        authModel = new AuthenticationModel(this);

        // Check if user is already logged in
        if (authModel.isLoggedIn()) {
            Log.d(TAG, "User already logged in, going to main screen");
            navigateToMain();
            return;
        }

        // Set up toggle between login and register
        binding.tvToggle.setOnClickListener(v -> {
            Log.d(TAG, "Toggle button clicked");
            toggleAuthMode();
        });

        // Set up login button
        binding.btnLogin.setOnClickListener(v -> {
            Log.d(TAG, "Login button clicked");
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(AuthActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            login(email, password);
        });

        // Set up register button
        binding.btnRegister.setOnClickListener(v -> {
            Log.d(TAG, "Register button clicked");
            String username = binding.etUsername.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            String phone = binding.etPhone.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                Toast.makeText(AuthActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            register(username, email, password, phone);
        });

        // Show login mode by default
        showLoginMode();
    }

    private void navigateToMain() {
        if (isFinishing()) return;
        Log.d(TAG, "Navigating to MainActivity");
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void toggleAuthMode() {
        isLoginMode = !isLoginMode;
        if (isLoginMode) {
            showLoginMode();
        } else {
            showRegisterMode();
        }
    }

    private void showLoginMode() {
        binding.etUsername.setVisibility(View.GONE);
        binding.etPhone.setVisibility(View.GONE);
        binding.btnLogin.setVisibility(View.VISIBLE);
        binding.btnRegister.setVisibility(View.GONE);
        binding.tvToggle.setText("Don't have an account? Register");
    }

    private void showRegisterMode() {
        binding.etUsername.setVisibility(View.VISIBLE);
        binding.etPhone.setVisibility(View.VISIBLE);
        binding.btnLogin.setVisibility(View.GONE);
        binding.btnRegister.setVisibility(View.VISIBLE);
        binding.tvToggle.setText("Already have an account? Login");
    }

    private void login(String email, String password) {
        Log.d(TAG, "Attempting login for email: " + email);
        LoginRequest request = new LoginRequest(email, password);
        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    Log.d(TAG, "Login successful for user: " + loginResponse.getUser().getEmail());
                    
                    // Save login data
                    authModel.saveUserData(loginResponse);
                    
                    // Navigate to main activity
                    navigateToMain();
                } else {
                    String errorMessage = "Login failed";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error reading error body", e);
                        }
                    }
                    Log.e(TAG, errorMessage);
                    Toast.makeText(AuthActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Login failed", t);
                Toast.makeText(AuthActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register(String username, String email, String password, String phone) {
        Log.d(TAG, "Attempting registration for email: " + email);
        RegisterRequest request = new RegisterRequest(username, email, password, phone);
        apiService.register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();
                    Log.d(TAG, "Registration successful for user: " + registerResponse.getUser().getEmail());
                    
                    // Save registration data
                    authModel.saveUserData(registerResponse);
                    
                    // Navigate to main activity
                    navigateToMain();
                } else {
                    String errorMessage = "Registration failed";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error reading error body", e);
                        }
                    }
                    Log.e(TAG, errorMessage);
                    Toast.makeText(AuthActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e(TAG, "Registration failed", t);
                Toast.makeText(AuthActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}