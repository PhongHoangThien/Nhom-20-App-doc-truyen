package vn.edu.hcmuaf.fit.springbootserver.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_tokens")
@Data
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token", nullable = false, length = 500)
    private String token;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "is_valid", nullable = false)
    private boolean isValid = true;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        // Token hết hạn sau 24 giờ
        expiresAt = createdAt.plusHours(24);
    }
} 