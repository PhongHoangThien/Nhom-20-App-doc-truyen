package vn.edu.hcmuaf.fit.springbootserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.springbootserver.dto.ReadingProgressDTO;
import vn.edu.hcmuaf.fit.springbootserver.service.ReadingProgressService;

import java.util.List;

@RestController
@RequestMapping("/api/reading-progress")
public class ReadingProgressController {

    @Autowired
    private ReadingProgressService readingProgressService;

    @PostMapping("/{userId}/{bookId}/{chapterId}")
    public ResponseEntity<ReadingProgressDTO> saveProgress(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @PathVariable Long chapterId) {
        return ResponseEntity.ok(readingProgressService.saveProgress(userId, bookId, chapterId));
    }

    @GetMapping("/{userId}/{bookId}")
    public ResponseEntity<ReadingProgressDTO> getProgress(
            @PathVariable Long userId,
            @PathVariable Long bookId) {
        return ResponseEntity.ok(readingProgressService.getProgress(userId, bookId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadingProgressDTO>> getUserProgress(@PathVariable Long userId) {
        return ResponseEntity.ok(readingProgressService.getUserProgress(userId));
    }
} 