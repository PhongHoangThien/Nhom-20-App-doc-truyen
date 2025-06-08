package com.example.demo.ui.reader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import com.example.demo.R;
import com.example.demo.model.ReadingProgress;
import com.example.demo.model.ReadingSettings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.demo.model.Chapter;
import com.example.demo.api.RetrofitClient;
import com.example.demo.api.ApiService;
import com.example.demo.ui.dialogs.FontSettingsDialog;

public class ReaderActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "ReaderPrefs";
    private static final String KEY_READING_SETTINGS = "reading_settings";
    private static final String KEY_READING_PROGRESS = "reading_progress_";

    private Toolbar toolbar;
    private TextView chapterTitleText;
    private TextView textViewContent;
    private BottomNavigationView bottomNavigation;
    private ScrollView scrollView;
    private FloatingActionButton fabNextChapter;

    private ReadingSettings readingSettings;
    private ReadingProgress readingProgress;
    private Long bookId;
    private Long currentChapterId = 1L;
    private Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        // Initialize views
        toolbar = findViewById(R.id.toolbar);
        chapterTitleText = findViewById(R.id.chapterTitleText);
        textViewContent = findViewById(R.id.textViewContent);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        scrollView = findViewById(R.id.scrollView);
        fabNextChapter = findViewById(R.id.fabNextChapter);

        // Set up toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get book ID from intent
        bookId = getIntent().getLongExtra("bookId", -1);
        userId = getIntent().getLongExtra("userId", -1);
        if (bookId == -1 || userId == -1) {
            Toast.makeText(this, "Invalid book or user ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadReadingProgress();
        loadChapterContent(bookId, currentChapterId);

        // Load reading settings
        loadReadingSettings();

        // Apply reading settings
        applyReadingSettings();

        // Set up bottom navigation
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_previous) {
                loadPreviousChapter();
                return true;
            } else if (itemId == R.id.navigation_font) {
                showFontSettingsDialog();
                return true;
            } else if (itemId == R.id.navigation_theme) {
                toggleTheme();
                return true;
            } else if (itemId == R.id.navigation_next) {
                loadNextChapter();
                return true;
            }
            return false;
        });

        // Set up scroll listener to save progress
        scrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            saveReadingProgress(scrollY);
        });

        // Set up FAB click listener
        fabNextChapter.setOnClickListener(v -> {
            currentChapterId++;
            loadChapterContent(bookId, currentChapterId);
        });
    }

    private void loadReadingSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String settingsJson = prefs.getString(KEY_READING_SETTINGS, null);
        if (settingsJson != null) {
            readingSettings = new Gson().fromJson(settingsJson, ReadingSettings.class);
        } else {
            readingSettings = new ReadingSettings();
        }
    }

    private void saveReadingSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String settingsJson = new Gson().toJson(readingSettings);
        prefs.edit().putString(KEY_READING_SETTINGS, settingsJson).apply();
    }

    private void loadReadingProgress() {
        String progressJson = getSharedPreferences("reading_progress", MODE_PRIVATE)
                .getString(bookId.toString(), null);
        if (progressJson != null) {
            readingProgress = new Gson().fromJson(progressJson, ReadingProgress.class);
            if (readingProgress.getChapterOrder() == currentChapterId.intValue()) {
                scrollView.post(() -> scrollView.scrollTo(0, readingProgress.getScrollPosition()));
            }
        } else {
            readingProgress = new ReadingProgress(bookId.intValue(), currentChapterId.intValue(), 0);
        }
    }

    private void saveReadingProgress(int scrollPosition) {
        readingProgress.setChapterOrder(currentChapterId.intValue());
        readingProgress.setScrollPosition(scrollPosition);
        readingProgress.setLastReadTimestamp(System.currentTimeMillis());
        
        String progressJson = new Gson().toJson(readingProgress);
        getSharedPreferences("reading_progress", MODE_PRIVATE)
                .edit()
                .putString(bookId.toString(), progressJson)
                .apply();
    }

    private void applyReadingSettings() {
        // Apply font settings
        textViewContent.setTextSize(readingSettings.getFontSize());
        textViewContent.setTypeface(android.graphics.Typeface.create(readingSettings.getFontFamily(), android.graphics.Typeface.NORMAL));
        textViewContent.setLineSpacing(0, readingSettings.getLineSpacing());

        // Apply theme
        if (readingSettings.isDarkMode()) {
            textViewContent.setTextColor(getResources().getColor(android.R.color.white));
            textViewContent.setBackgroundColor(getResources().getColor(android.R.color.black));
        } else {
            textViewContent.setTextColor(getResources().getColor(android.R.color.black));
            textViewContent.setBackgroundColor(getResources().getColor(android.R.color.white));
        }

        // Apply margins
        textViewContent.setPadding(
            readingSettings.getMarginHorizontal(),
            readingSettings.getMarginVertical(),
            readingSettings.getMarginHorizontal(),
            readingSettings.getMarginVertical()
        );
    }

    private void loadPreviousChapter() {
        if (currentChapterId > 1) {
            currentChapterId--;
            loadChapterContent(bookId, currentChapterId);
        }
    }

    private void loadNextChapter() {
        currentChapterId++;
        loadChapterContent(bookId, currentChapterId);
    }

    private void showFontSettingsDialog() {
        FontSettingsDialog dialog = new FontSettingsDialog(this, readingSettings, newSettings -> {
            readingSettings = newSettings;
            applyReadingSettings();
            saveReadingSettings();
        });
        dialog.show();
    }

    private void toggleTheme() {
        readingSettings.setDarkMode(!readingSettings.isDarkMode());
        applyReadingSettings();
        saveReadingSettings();
    }

    private void loadChapterContent(long bookId, long chapterId) {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        apiService.getChapter(String.valueOf(bookId), String.valueOf(chapterId))
            .enqueue(new Callback<Chapter>() {
                @Override
                public void onResponse(Call<Chapter> call, Response<Chapter> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Chapter chapter = response.body();
                        chapterTitleText.setText(chapter.getTitle());
                        textViewContent.setText(chapter.getContent());
                        scrollView.scrollTo(0, 0);
                    } else {
                        // If chapter not found, go back to previous chapter
                        currentChapterId--;
                        textViewContent.setText("End of book");
                    }
                }

                @Override
                public void onFailure(Call<Chapter> call, Throwable t) {
                    textViewContent.setText("Error loading chapter");
                }
            });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save progress when leaving the activity
        saveReadingProgress(scrollView.getScrollY());
    }
} 