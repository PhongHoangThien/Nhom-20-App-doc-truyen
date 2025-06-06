package com.example.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.demo.databinding.ActivityChangeEmailBinding;
import com.example.demo.network.ApiService;
import com.example.demo.network.ChangeEmailRequest;
import com.example.demo.network.RetrofitClient;
import com.example.demo.utils.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.IOException;

public class ChangeEmailActivity extends AppCompatActivity {
    private static final String TAG = "ChangeEmailActivity";
    private ActivityChangeEmailBinding binding;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Change Email");
        }

        // Initialize API service
        apiService = RetrofitClient.getInstance().getClient().create(ApiService.class);

        // Set up click listeners
        setupClickListeners();
        Log.d(TAG, "Activity created and initialized");
    }

    private void setupClickListeners() {
        binding.buttonChangeEmail.setOnClickListener(v -> {
            Log.d(TAG, "Button clicked");
            changeEmail();
        });
    }

    private void changeEmail() {
        String newEmail = binding.editTextNewEmail.getText().toString().trim();
        String currentPassword = binding.editTextPassword.getText().toString().trim();

        Log.d(TAG, "Validating input: newEmail=" + newEmail + ", currentPassword=" + currentPassword);

        // Validate input
        if (newEmail.isEmpty()) {
            binding.editTextNewEmail.setError("Please enter new email");
            Log.d(TAG, "New email is empty");
            return;
        }
        if (currentPassword.isEmpty()) {
            binding.editTextPassword.setError("Please enter your password");
            Log.d(TAG, "Current password is empty");
            return;
        }

        // Show loading
        binding.buttonChangeEmail.setEnabled(false);
        binding.progressBar.setVisibility(android.view.View.VISIBLE);

        // Get token from SharedPreferences
        String token = SharedPreferencesManager.getInstance(this).getToken();
        if (token == null || token.isEmpty()) {
            Log.e(TAG, "Token is null or empty");
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Create request
        ChangeEmailRequest request = new ChangeEmailRequest(newEmail, currentPassword);
        Log.d(TAG, "Request created: " + request.getNewEmail() + ", " + request.getCurrentPassword());
        Log.d(TAG, "Token: " + token);

        // Call API
        Log.d(TAG, "Calling API...");
        apiService.changeEmail("Bearer " + token, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                binding.buttonChangeEmail.setEnabled(true);
                binding.progressBar.setVisibility(android.view.View.GONE);

                Log.d(TAG, "Response received: code=" + response.code());
                if (response.isSuccessful()) {
                    Log.d(TAG, "Email changed successfully");
                    Toast.makeText(ChangeEmailActivity.this, "Email changed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String errorMessage = "Failed to change email";
                    if (response.code() == 401) {
                        errorMessage = "Invalid password";
                    } else if (response.code() == 409) {
                        errorMessage = "Email already exists";
                    }
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e(TAG, "Error: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(ChangeEmailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                binding.buttonChangeEmail.setEnabled(true);
                binding.progressBar.setVisibility(android.view.View.GONE);
                Log.e(TAG, "Network error", t);
                Toast.makeText(ChangeEmailActivity.this, "Network error", Toast.LENGTH_SHORT).show();
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