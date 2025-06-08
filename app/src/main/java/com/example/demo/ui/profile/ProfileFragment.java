package com.example.demo.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.demo.R;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileFragment extends Fragment {
    private ShapeableImageView profileImage;
    private TextView usernameText;
    private TextView emailText;
    private TextView editProfileButton;
    private TextView changePasswordButton;
    private TextView changeEmailButton;
    private Button logoutButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        profileImage = root.findViewById(R.id.profileImage);
        usernameText = root.findViewById(R.id.usernameText);
        emailText = root.findViewById(R.id.emailText);
        editProfileButton = root.findViewById(R.id.editProfileButton);
        changePasswordButton = root.findViewById(R.id.changePasswordButton);
        changeEmailButton = root.findViewById(R.id.changeEmailButton);
        logoutButton = root.findViewById(R.id.logoutButton);

        // Set up click listeners
        editProfileButton.setOnClickListener(v -> {
            // TODO: Navigate to edit profile
        });

        changePasswordButton.setOnClickListener(v -> {
            // TODO: Navigate to change password
        });

        changeEmailButton.setOnClickListener(v -> {
            // TODO: Navigate to change email
        });

        logoutButton.setOnClickListener(v -> {
            // TODO: Handle logout
        });

        // TODO: Load user data

        return root;
    }
} 