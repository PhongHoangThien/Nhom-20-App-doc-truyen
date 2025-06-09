package vn.edu.hcmuaf.fit.springbootserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.springbootserver.entity.Chapter;
import vn.edu.hcmuaf.fit.springbootserver.repository.ChapterRepository;
import vn.edu.hcmuaf.fit.springbootserver.service.ChapterService;

import java.util.List;

@Service
public class ChapterServiceImpl implements ChapterService {
    @Autowired
    private ChapterRepository chapterRepository;

    @Override
    public List<Chapter> getChaptersByBookId(Long bookId) {
        return chapterRepository.findByBookIdOrderByChapterNumberAsc(bookId);
    }

    @Override
    public Chapter getChapterById(Long id) {
        return chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
    }

    @Override
    public Chapter createChapter(Chapter chapter) {
        return chapterRepository.save(chapter);
    }

    @Override
    public Chapter updateChapter(Long id, Chapter chapter) {
        Chapter existingChapter = getChapterById(id);
        existingChapter.setTitle(chapter.getTitle());
        existingChapter.setContent(chapter.getContent());
        existingChapter.setChapterNumber(chapter.getChapterNumber());
        return chapterRepository.save(existingChapter);
    }

    @Override
    public void deleteChapter(Long id) {
        chapterRepository.deleteById(id);
    }
} 