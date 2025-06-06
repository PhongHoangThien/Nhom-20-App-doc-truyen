package com.example.demo;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.demo.databinding.ActivityChangePasswordBinding;
import com.example.demo.network.ApiService;
import com.example.demo.network.ChangePasswordRequest;
import com.example.demo.network.RetrofitClient;
import com.example.demo.utils.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Change Password");
        }

        // Initialize API service
        apiService = RetrofitClient.getInstance().getClient().create(ApiService.class);

        // Set up click listeners
        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.buttonChangePassword.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String currentPassword = binding.editTextCurrentPassword.getText().toString().trim();
        String newPassword = binding.editTextNewPassword.getText().toString().trim();
        String confirmPassword = binding.editTextConfirmPassword.getText().toString().trim();

        // Validate input
        if (currentPassword.isEmpty()) {
            binding.editTextCurrentPassword.setError("Please enter current password");
            return;
        }
        if (newPassword.isEmpty()) {
            binding.editTextNewPassword.setError("Please enter new password");
            return;
        }
        if (confirmPassword.isEmpty()) {
            binding.editTextConfirmPassword.setError("Please confirm new password");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            binding.editTextConfirmPassword.setError("Passwords do not match");
            return;
        }
        if (newPassword.length() < 6) {
            binding.editTextNewPassword.setError("Password must be at least 6 characters");
            return;
        }

        // Show loading
        binding.buttonChangePassword.setEnabled(false);
        binding.progressBar.setVisibility(android.view.View.VISIBLE);

        // Get token from SharedPreferences
        String token = SharedPreferencesManager.getInstance(this).getToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Create request
        ChangePasswordRequest request = new ChangePasswordRequest(currentPassword, newPassword);

        // Call API
        apiService.changePassword("Bearer " + token, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                binding.buttonChangePassword.setEnabled(true);
                binding.progressBar.setVisibility(android.view.View.GONE);

                if (response.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String errorMessage = "Failed to change password";
                    if (response.code() == 401) {
                        errorMessage = "Invalid current password";
                    }
                    Toast.makeText(ChangePasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                binding.buttonChangePassword.setEnabled(true);
                binding.progressBar.setVisibility(android.view.View.GONE);
                Toast.makeText(ChangePasswordActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}