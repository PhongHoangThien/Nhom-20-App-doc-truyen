package com.example.demo.ui.reader;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.demo.R;
import com.example.demo.model.ReadingSettings;

public class FontSettingsDialog extends Dialog {
    private ReadingSettings readingSettings;
    private OnSettingsChangedListener listener;

    private SeekBar fontSizeSeekBar;
    private Spinner fontFamilySpinner;
    private SeekBar lineSpacingSeekBar;
    private TextView previewText;

    public interface OnSettingsChangedListener {
        void onSettingsChanged(ReadingSettings settings);
    }

    public FontSettingsDialog(Context context, ReadingSettings settings, OnSettingsChangedListener listener) {
        super(context);
        this.readingSettings = settings;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_font_settings);

        // Initialize views
        fontSizeSeekBar = findViewById(R.id.fontSizeSeekBar);
        fontFamilySpinner = findViewById(R.id.fontFamilySpinner);
        lineSpacingSeekBar = findViewById(R.id.lineSpacingSeekBar);
        previewText = findViewById(R.id.previewText);
        Button applyButton = findViewById(R.id.applyButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        // Set initial values
        fontSizeSeekBar.setProgress(readingSettings.getFontSize() - 12); // Min size is 12
        lineSpacingSeekBar.setProgress(readingSettings.getLineSpacing() - 4); // Min spacing is 4

        // Set up listeners
        fontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatePreview();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        lineSpacingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updatePreview();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        applyButton.setOnClickListener(v -> {
            // Update settings
            readingSettings.setFontSize(fontSizeSeekBar.getProgress() + 12);
            readingSettings.setFontFamily(fontFamilySpinner.getSelectedItem().toString());
            readingSettings.setLineSpacing(lineSpacingSeekBar.getProgress() + 4);

            // Notify listener
            if (listener != null) {
                listener.onSettingsChanged(readingSettings);
            }

            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());

        // Initial preview update
        updatePreview();
    }

    private void updatePreview() {
        int fontSize = fontSizeSeekBar.getProgress() + 12;
        int lineSpacing = lineSpacingSeekBar.getProgress() + 4;
        String fontFamily = fontFamilySpinner.getSelectedItem().toString();

        previewText.setTextSize(fontSize);
        previewText.setTypeface(android.graphics.Typeface.create(fontFamily, android.graphics.Typeface.NORMAL));
        previewText.setLineSpacing(0, lineSpacing);
    }
} 