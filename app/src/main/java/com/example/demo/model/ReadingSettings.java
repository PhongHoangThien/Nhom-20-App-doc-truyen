package com.example.demo.model;

public class ReadingSettings {
    private int fontSize;
    private String fontFamily;
    private boolean isDarkMode;
    private int lineSpacing;
    private int paragraphSpacing;
    private int marginHorizontal;
    private int marginVertical;

    public ReadingSettings() {
        // Default settings
        this.fontSize = 18;
        this.fontFamily = "Roboto";
        this.isDarkMode = false;
        this.lineSpacing = 8;
        this.paragraphSpacing = 16;
        this.marginHorizontal = 16;
        this.marginVertical = 16;
    }

    // Getters and setters
    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) { this.fontSize = fontSize; }

    public String getFontFamily() { return fontFamily; }
    public void setFontFamily(String fontFamily) { this.fontFamily = fontFamily; }

    public boolean isDarkMode() { return isDarkMode; }
    public void setDarkMode(boolean darkMode) { isDarkMode = darkMode; }

    public int getLineSpacing() { return lineSpacing; }
    public void setLineSpacing(int lineSpacing) { this.lineSpacing = lineSpacing; }

    public int getParagraphSpacing() { return paragraphSpacing; }
    public void setParagraphSpacing(int paragraphSpacing) { this.paragraphSpacing = paragraphSpacing; }

    public int getMarginHorizontal() { return marginHorizontal; }
    public void setMarginHorizontal(int marginHorizontal) { this.marginHorizontal = marginHorizontal; }

    public int getMarginVertical() { return marginVertical; }
    public void setMarginVertical(int marginVertical) { this.marginVertical = marginVertical; }
} 