package com.example.demo.model;

public class ReadingSettings {
    private int fontSize;
    private String fontFamily;
    private int lineSpacing;
    private boolean darkMode;
    private int marginHorizontal;
    private int marginVertical;

    public ReadingSettings() {
        // Default values
        this.fontSize = 16;
        this.fontFamily = "sans-serif";
        this.lineSpacing = 8;
        this.darkMode = false;
        this.marginHorizontal = 16;
        this.marginVertical = 16;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public int getMarginHorizontal() {
        return marginHorizontal;
    }

    public void setMarginHorizontal(int marginHorizontal) {
        this.marginHorizontal = marginHorizontal;
    }

    public int getMarginVertical() {
        return marginVertical;
    }

    public void setMarginVertical(int marginVertical) {
        this.marginVertical = marginVertical;
    }
} 