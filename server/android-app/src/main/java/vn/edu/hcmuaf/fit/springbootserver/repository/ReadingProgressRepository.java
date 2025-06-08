package vn.edu.hcmuaf.fit.springbootserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmuaf.fit.springbootserver.entity.ReadingProgress;
import java.util.Optional;

public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, Long> {
    Optional<ReadingProgress> findByUserIdAndBookId(Long userId, Long bookId);
} 