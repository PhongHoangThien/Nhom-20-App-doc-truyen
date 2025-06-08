package vn.edu.hcmuaf.fit.springbootserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.springbootserver.dto.ReadingProgressDTO;
import vn.edu.hcmuaf.fit.springbootserver.entity.Book;
import vn.edu.hcmuaf.fit.springbootserver.entity.Chapter;
import vn.edu.hcmuaf.fit.springbootserver.entity.ReadingProgress;
import vn.edu.hcmuaf.fit.springbootserver.entity.User;
import vn.edu.hcmuaf.fit.springbootserver.repository.BookRepository;
import vn.edu.hcmuaf.fit.springbootserver.repository.ChapterRepository;
import vn.edu.hcmuaf.fit.springbootserver.repository.ReadingProgressRepository;
import vn.edu.hcmuaf.fit.springbootserver.repository.UserRepository;
import vn.edu.hcmuaf.fit.springbootserver.service.ReadingProgressService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReadingProgressServiceImpl implements ReadingProgressService {

    @Autowired
    private ReadingProgressRepository readingProgressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Override
    public ReadingProgressDTO saveProgress(Long userId, Long bookId, Long chapterId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        ReadingProgress progress = readingProgressRepository.findByUserIdAndBookId(userId, bookId)
                .orElse(new ReadingProgress());

        progress.setUser(user);
        progress.setBook(book);
        progress.setCurrentChapter(chapter);

        ReadingProgress savedProgress = readingProgressRepository.save(progress);
        return convertToDTO(savedProgress);
    }

    @Override
    public ReadingProgressDTO getProgress(Long userId, Long bookId) {
        ReadingProgress progress = readingProgressRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RuntimeException("Reading progress not found"));
        return convertToDTO(progress);
    }

    @Override
    public List<ReadingProgressDTO> getUserProgress(Long userId) {
        List<ReadingProgress> progresses = readingProgressRepository.findAll().stream()
                .filter(progress -> progress.getUser().getId().equals(userId))
                .collect(Collectors.toList());
        return progresses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ReadingProgressDTO convertToDTO(ReadingProgress progress) {
        ReadingProgressDTO dto = new ReadingProgressDTO();
        dto.setId(progress.getId());
        dto.setUserId(progress.getUser().getId());
        dto.setBookId(progress.getBook().getId());
        dto.setCurrentChapterId(progress.getCurrentChapter().getId());
        dto.setLastReadAt(progress.getLastReadAt().toString());
        return dto;
    }
} 