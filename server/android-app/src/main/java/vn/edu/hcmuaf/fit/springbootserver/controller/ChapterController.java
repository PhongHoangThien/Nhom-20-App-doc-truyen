package vn.edu.hcmuaf.fit.springbootserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.springbootserver.entity.Chapter;
import vn.edu.hcmuaf.fit.springbootserver.service.ChapterService;

import java.util.List;

@RestController
@RequestMapping("/chapters")
@CrossOrigin(origins = "*")
public class ChapterController {
    @Autowired
    private ChapterService chapterService;

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Chapter>> getChaptersByBookId(@PathVariable Long bookId) {
        return ResponseEntity.ok(chapterService.getChaptersByBookId(bookId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chapter> getChapterById(@PathVariable Long id) {
        return ResponseEntity.ok(chapterService.getChapterById(id));
    }

    @PostMapping
    public ResponseEntity<Chapter> createChapter(@RequestBody Chapter chapter) {
        return ResponseEntity.ok(chapterService.createChapter(chapter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chapter> updateChapter(@PathVariable Long id, @RequestBody Chapter chapter) {
        return ResponseEntity.ok(chapterService.updateChapter(id, chapter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return ResponseEntity.ok().build();
    }
} 