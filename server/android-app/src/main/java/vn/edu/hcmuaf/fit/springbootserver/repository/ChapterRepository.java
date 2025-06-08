package vn.edu.hcmuaf.fit.springbootserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmuaf.fit.springbootserver.entity.Chapter;
import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findByBookIdOrderByChapterOrderAsc(Long bookId);
    Chapter findByBookIdAndChapterOrder(Long bookId, Integer chapterOrder);
} 