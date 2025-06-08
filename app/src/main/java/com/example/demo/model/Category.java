package com.example.demo.model;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class Category {
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    // Remove books field to prevent infinite recursion
    // @SerializedName("books")
    // @Expose
    // private List<Book> books;

    public Category() {
    }

    public Category(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Category{id=%d, name='%s', description='%s'}",
            id != null ? id : -1,
            name != null ? name : "null",
            description != null ? description : "null");
    }
} 