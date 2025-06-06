package com.example.demo;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.databinding.ActivityForgotPasswordBinding;
import com.example.demo.model.AuthenticationModel;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;
    private AuthenticationModel authModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up toolbar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");

        // Initialize AuthenticationModel
        authModel = new AuthenticationModel(this);

        // Set up click listeners
        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.buttonResetPassword.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String email = binding.editTextEmail.getText().toString().trim();

        // Validate input
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Implement password reset with Spring Boot API
        Toast.makeText(this, "Password reset will be implemented with Spring Boot API", Toast.LENGTH_SHORT).show();
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