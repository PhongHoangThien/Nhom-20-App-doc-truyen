-- Insert categories
INSERT INTO categories (name) VALUES 
('Tiểu thuyết'),
('Khoa học'),
('Lịch sử'),
('Văn học'),
('Kinh tế');

-- Insert users (password is 'password' encoded with BCrypt)
INSERT INTO users (username, email, password, full_name, role) VALUES 
('user1', 'user1@example.com', '$2a$10$rDkPvvAFV6GgJjXpYWJhUOQZxJZxJZxJZxJZxJZxJZxJZxJZxJZxJZ', 'Nguyễn Văn A', 'USER'),
('user2', 'user2@example.com', '$2a$10$rDkPvvAFV6GgJjXpYWJhUOQZxJZxJZxJZxJZxJZxJZxJZxJZxJZxJZ', 'Trần Thị B', 'USER'),
('admin', 'admin@example.com', '$2a$10$rDkPvvAFV6GgJjXpYWJhUOQZxJZxJZxJZxJZxJZxJZxJZxJZxJZxJZ', 'Admin', 'ADMIN');

-- Insert books
INSERT INTO books (title, author, description, cover_image, category_id, price, rating) VALUES 
('Đắc Nhân Tâm', 'Dale Carnegie', 'Cuốn sách về nghệ thuật đối nhân xử thế', 'https://example.com/dac-nhan-tam.jpg', 1, 99000, 4.5),
('Nhà Giả Kim', 'Paulo Coelho', 'Câu chuyện về hành trình tìm kiếm kho báu', 'https://example.com/nha-gia-kim.jpg', 1, 89000, 4.7),
('Sapiens', 'Yuval Noah Harari', 'Lược sử loài người', 'https://example.com/sapiens.jpg', 2, 149000, 4.8),
('Đại Việt Sử Ký', 'Lê Văn Hưu', 'Lịch sử Việt Nam thời kỳ phong kiến', 'https://example.com/dai-viet-su-ky.jpg', 3, 129000, 4.6),
('Tư Duy Nhanh và Chậm', 'Daniel Kahneman', 'Phân tích về hai hệ thống tư duy', 'https://example.com/tu-duy-nhanh-va-cham.jpg', 4, 169000, 4.9);

-- Insert chapters
INSERT INTO chapters (book_id, title, content, number) VALUES 
(1, 'Chương 1: Nghệ thuật ứng xử', 'Nội dung chương 1 của Đắc Nhân Tâm...', 1),
(1, 'Chương 2: Cách tạo thiện cảm', 'Nội dung chương 2 của Đắc Nhân Tâm...', 2),
(2, 'Chương 1: Khởi đầu cuộc hành trình', 'Nội dung chương 1 của Nhà Giả Kim...', 1),
(2, 'Chương 2: Những thử thách đầu tiên', 'Nội dung chương 2 của Nhà Giả Kim...', 2),
(3, 'Chương 1: Loài người xuất hiện', 'Nội dung chương 1 của Sapiens...', 1),
(3, 'Chương 2: Cách mạng nhận thức', 'Nội dung chương 2 của Sapiens...', 2);

-- Insert reading progress
INSERT INTO reading_progress (user_id, book_id, current_chapter, last_read_at) VALUES 
(1, 1, 1, CURRENT_TIMESTAMP),
(1, 2, 1, CURRENT_TIMESTAMP),
(2, 3, 1, CURRENT_TIMESTAMP);

-- Insert bookmarks
INSERT INTO bookmarks (user_id, book_id, chapter_id, note, created_at) VALUES 
(1, 1, 1, 'Đoạn hay về cách ứng xử', CURRENT_TIMESTAMP),
(1, 2, 1, 'Ghi chú về hành trình', CURRENT_TIMESTAMP),
(2, 3, 1, 'Điểm quan trọng về lịch sử', CURRENT_TIMESTAMP);