package com.example.demo.model;

import android.util.Log;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class Book {
    private static final String TAG = "Book";

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("coverImage")
    @Expose
    private String coverImage;

    @SerializedName("price")
    @Expose
    private Double price;

    @SerializedName("category")
    @Expose(serialize = false, deserialize = true)
    private Category category;

    @SerializedName("rating")
    @Expose
    private Float rating;

    @SerializedName("viewCount")
    @Expose
    private Integer viewCount;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public Book() {
    }

    public Book(Long id, String title, String author, String description, String coverImage, Double price) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.coverImage = coverImage;
        this.price = price;
        this.rating = 0.0f;
        this.viewCount = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        try {
            return String.format("Book{id=%d, title='%s', author='%s', category=%s}",
                id != null ? id : -1,
                title != null ? title : "null",
                author != null ? author : "null",
                category != null ? category.getName() : "null");
        } catch (Exception e) {
            Log.e(TAG, "Error in toString()", e);
            return "Book{error in toString}";
        }
    }
}