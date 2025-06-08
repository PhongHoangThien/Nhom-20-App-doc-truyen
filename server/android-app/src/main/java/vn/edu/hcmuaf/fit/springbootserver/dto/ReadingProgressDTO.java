package vn.edu.hcmuaf.fit.springbootserver.dto;

import lombok.Data;

@Data
public class ReadingProgressDTO {
    private Long id;
    private Long userId;
    private Long bookId;
    private Long currentChapterId;
    private String lastReadAt;
} 