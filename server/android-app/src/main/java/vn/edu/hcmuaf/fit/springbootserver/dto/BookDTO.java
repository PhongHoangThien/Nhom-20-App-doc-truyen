package vn.edu.hcmuaf.fit.springbootserver.dto;

import vn.edu.hcmuaf.fit.springbootserver.entity.Book;
import vn.edu.hcmuaf.fit.springbootserver.entity.Category;
import java.time.format.DateTimeFormatter;

public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String description;
    private String coverImage;
    private Double price;
    private CategoryDTO category;
    private Float rating;
    private Integer viewCount;
    private String createdAt;
    private String updatedAt;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public BookDTO() {
    }

    public BookDTO(Long id, String title, String author, String description, String coverImage, 
                  Double price, Category category, Float rating, Integer viewCount, 
                  String createdAt, String updatedAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.coverImage = coverImage;
        this.price = price;
        this.category = category != null ? CategoryDTO.fromEntity(category) : null;
        this.rating = rating;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static BookDTO fromEntity(Book book) {
        if (book == null) {
            return null;
        }
        return new BookDTO(
            book.getId(),
            book.getTitle(),
            book.getAuthor(),
            book.getDescription(),
            book.getCoverImage(),
            book.getPrice(),
            book.getCategory(),
            book.getRating(),
            book.getViewCount(),
            book.getCreatedAt() != null ? book.getCreatedAt().format(formatter) : null,
            book.getUpdatedAt() != null ? book.getUpdatedAt().format(formatter) : null
        );
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

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
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
} 