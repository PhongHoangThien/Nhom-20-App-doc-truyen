package vn.edu.hcmuaf.fit.springbootserver.service;

import vn.edu.hcmuaf.fit.springbootserver.dto.ReadingProgressDTO;
import java.util.List;

public interface ReadingProgressService {
    ReadingProgressDTO saveProgress(Long userId, Long bookId, Long chapterId);
    ReadingProgressDTO getProgress(Long userId, Long bookId);
    List<ReadingProgressDTO> getUserProgress(Long userId);
} 