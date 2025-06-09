package vn.edu.hcmuaf.fit.springbootserver.service;

import vn.edu.hcmuaf.fit.springbootserver.entity.Chapter;
import java.util.List;

public interface ChapterService {
    List<Chapter> getChaptersByBookId(Long bookId);
    Chapter getChapterById(Long id);
    Chapter createChapter(Chapter chapter);
    Chapter updateChapter(Long id, Chapter chapter);
    void deleteChapter(Long id);
} 