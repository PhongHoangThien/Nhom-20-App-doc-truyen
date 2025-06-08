package com.example.demo.model;

import com.google.gson.annotations.SerializedName;

public class ReadingProgress {
    @SerializedName("id")
    private Long id;

    @SerializedName("userId")
    private Long userId;

    @SerializedName("bookId")
    private Long bookId;

    @SerializedName("currentChapterId")
    private Long currentChapterId;

    @SerializedName("lastReadAt")
    private String lastReadAt;

    private int chapterOrder;
    private int scrollPosition;
    private long lastReadTimestamp;

    // Default constructor
    public ReadingProgress() {
    }

    // Constructor with parameters
    public ReadingProgress(int bookId, int chapterId, int scrollPosition) {
        this.bookId = (long) bookId;
        this.currentChapterId = (long) chapterId;
        this.chapterOrder = chapterId;
        this.scrollPosition = scrollPosition;
        this.lastReadTimestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getCurrentChapterId() {
        return currentChapterId;
    }

    public void setCurrentChapterId(Long currentChapterId) {
        this.currentChapterId = currentChapterId;
    }

    public String getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(String lastReadAt) {
        this.lastReadAt = lastReadAt;
    }

    public int getChapterOrder() {
        return chapterOrder;
    }

    public void setChapterOrder(int chapterOrder) {
        this.chapterOrder = chapterOrder;
    }

    public int getScrollPosition() {
        return scrollPosition;
    }

    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }

    public long getLastReadTimestamp() {
        return lastReadTimestamp;
    }

    public void setLastReadTimestamp(long lastReadTimestamp) {
        this.lastReadTimestamp = lastReadTimestamp;
    }
}