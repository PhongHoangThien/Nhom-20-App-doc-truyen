package com.example.demo;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.databinding.ActivityRegisterBinding;
import com.example.demo.model.AuthenticationModel;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private AuthenticationModel authModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up toolbar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");

        // Initialize AuthenticationModel
        authModel = new AuthenticationModel(this);

        // Set up click listeners
        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.buttonRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String username = binding.editTextUsername.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();
        String confirmPassword = binding.editTextConfirmPassword.getText().toString().trim();
        String phone = binding.editTextPhone.getText().toString().trim();

        // Validate input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register user
        authModel.register(username, email, password, phone, new AuthenticationModel.RegisterCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                finish(); // Return to login screen
            }

            @Override
            public void onError(String message) {
                Toast.makeText(RegisterActivity.this, "Registration failed: " + message, Toast.LENGTH_SHORT).show();
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