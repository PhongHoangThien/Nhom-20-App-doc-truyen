package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.demo.databinding.ActivityMainBinding;
import com.example.demo.model.User;
import com.example.demo.network.ApiService;
import com.example.demo.network.RetrofitClient;
import com.example.demo.ui.home.HomeFragment;
import com.example.demo.ui.library.LibraryFragment;
import com.example.demo.ui.search.SearchFragment;
import com.example.demo.ui.profile.ProfileFragment;
import com.example.demo.utils.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ApiService apiService;
    private User currentUser;
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up toolbar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Home");
        }

        // Initialize API service
        apiService = RetrofitClient.getInstance().getClient().create(ApiService.class);

        // Load user data
        loadUserData();

        // Set up logout button
        binding.buttonLogout.setOnClickListener(v -> logout());

        // Initialize ViewPager and BottomNavigation
        viewPager = binding.viewPager;
        bottomNavigation = binding.bottomNavigation;

        // Set up ViewPager with fragments
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new LibraryFragment());
        fragments.add(new SearchFragment());
        fragments.add(new ProfileFragment());

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(this, fragments);
        viewPager.setAdapter(pagerAdapter);

        // Connect BottomNavigation with ViewPager
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNavigation.setSelectedItemId(getNavigationId(position));
            }
        });

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                viewPager.setCurrentItem(0);
                return true;
            } else if (itemId == R.id.navigation_library) {
                viewPager.setCurrentItem(1);
                return true;
            } else if (itemId == R.id.navigation_search) {
                viewPager.setCurrentItem(2);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                viewPager.setCurrentItem(3);
                return true;
            }
            return false;
        });
    }

    private void loadUserData() {
        String token = SharedPreferencesManager.getInstance(this).getToken();
        if (token != null && !token.isEmpty()) {
            apiService.getCurrentUser("Bearer " + token).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        currentUser = response.body();
                        updateUI();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateUI() {
        if (currentUser != null) {
            binding.textViewUserInfo.setText(String.format("Welcome, %s!", currentUser.getFullName()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_account) {
            startActivity(new Intent(this, AccountActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        String token = SharedPreferencesManager.getInstance(this).getToken();
        if (token != null && !token.isEmpty()) {
            apiService.logout("Bearer " + token).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    // Clear local data and go to login screen regardless of server response
                    SharedPreferencesManager.getInstance(MainActivity.this).clearUserData();
                    navigateToLogin();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Clear local data and go to login screen even if network fails
                    SharedPreferencesManager.getInstance(MainActivity.this).clearUserData();
                    navigateToLogin();
                }
            });
        } else {
            navigateToLogin();
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private int getNavigationId(int position) {
        switch (position) {
            case 0:
                return R.id.navigation_home;
            case 1:
                return R.id.navigation_library;
            case 2:
                return R.id.navigation_search;
            case 3:
                return R.id.navigation_profile;
            default:
                return R.id.navigation_home;
        }
    }
}
