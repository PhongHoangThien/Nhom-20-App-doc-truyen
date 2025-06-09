package vn.edu.hcmuaf.fit.springbootserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "chapters")
public class Chapter {
    // Getters and Setters
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Getter
    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Getter
    @Column(columnDefinition = "TEXT")
    private String content;

    @Setter
    @Getter
    @Column(name = "chapter_number")
    private Integer chapterNumber;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}