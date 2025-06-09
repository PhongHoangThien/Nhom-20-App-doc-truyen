use android_app;

-- Insert User (mật khẩu đã mã hóa BCrypt)
INSERT INTO users (id, password, email, role) VALUES
                                                  (1, '$2a$10$Dow1Qw6Qw6Qw6Qw6Qw6QwOeQw6Qw6Qw6Qw6Qw6Qw6Qw6Qw6Qw6Qw6', 'alice@example.com', 'USER'),
                                                  (2, '$2a$10$Dow2Qw6Qw6Qw6Qw6Qw6QwOeQw6Qw6Qw6Qw6Qw6Qw6Qw6Qw6Qw6Qw6', 'bob@example.com', 'USER'),
                                                  (3, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin@example.com', 'ADMIN');

-- Insert categories if they don't exist
INSERT IGNORE INTO categories (name, description) VALUES
('Fiction', 'Fictional stories and novels'),
('Classic Literature', 'Classic works of literature'),
('Philosophical Fiction', 'Fiction with philosophical themes'),
('Science Fiction', 'Speculative fiction with scientific elements'),
('Romance', 'Romantic fiction and love stories');

-- Insert books if they don't exist
INSERT IGNORE INTO books (title, author, description, cover_image, price, category_id, rating, view_count, created_at, updated_at)
SELECT 'The Fountainhead', 'Ayn Rand', 'The Fountainhead is a 1943 novel by Russian-American author Ayn Rand, her first major literary success. The novel''s protagonist, Howard Roark, is an individualistic young architect who designs modernist buildings and refuses to compromise with an architectural establishment unwilling to accept innovation. Roark embodies what Rand believed to be the ideal man, and his struggle reflects Rand''s belief that individualism is superior to collectivism.', 'drawable/the_fountainhead.png', 19.99, id, 4.5, 0, NOW(), NOW()
FROM categories WHERE name = 'Philosophical Fiction' AND NOT EXISTS (SELECT 1 FROM books WHERE title = 'The Fountainhead');

INSERT IGNORE INTO books (title, author, description, cover_image, price, category_id, rating, view_count, created_at, updated_at)
SELECT 'The Thorn Birds', 'Colleen McCullough', 'The Thorn Birds is a 1977 novel by Australian author Colleen McCullough. Set primarily on Drogheda—a fictional sheep station in the Australian Outback named after Drogheda, Ireland—the story focuses on the Cleary family and spans the years 1915 to 1969. The novel is the best-selling book in Australian history, and has sold over 33 million copies worldwide.', 'drawable/the_thorn_bird.png', 15.99, id, 4.7, 0, NOW(), NOW()
FROM categories WHERE name = 'Fiction' AND NOT EXISTS (SELECT 1 FROM books WHERE title = 'The Thorn Birds');

INSERT IGNORE INTO books (title, author, description, cover_image, price, category_id, rating, view_count, created_at, updated_at)
SELECT '1984', 'George Orwell', '1984 is a dystopian social science fiction novel and cautionary tale. Thematically, it centres on the consequences of totalitarianism, mass surveillance and repressive regimentation of people and behaviours within society.', 'img/1984.png', 12.99, id, 4.8, 0, NOW(), NOW()
FROM categories WHERE name = 'Science Fiction' AND NOT EXISTS (SELECT 1 FROM books WHERE title = '1984');

INSERT IGNORE INTO books (title, author, description, cover_image, price, category_id, rating, view_count, created_at, updated_at)
SELECT 'Pride and Prejudice', 'Jane Austen', 'Pride and Prejudice is a romantic novel of manners written by Jane Austen in 1813. The novel follows the character development of Elizabeth Bennet, the dynamic protagonist of the book who learns about the repercussions of hasty judgments and comes to appreciate the difference between superficial goodness and actual goodness.', 'img/pride_and_prejudice.png', 9.99, id, 4.6, 0, NOW(), NOW()
FROM categories WHERE name = 'Romance' AND NOT EXISTS (SELECT 1 FROM books WHERE title = 'Pride and Prejudice');

INSERT IGNORE INTO books (title, author, description, cover_image, price, category_id, rating, view_count, created_at, updated_at)
SELECT 'The Great Gatsby', 'F. Scott Fitzgerald', 'The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald. Set in the Jazz Age on Long Island, the novel depicts narrator Nick Carraway''s interactions with mysterious millionaire Jay Gatsby and Gatsby''s obsession to reunite with his former lover, Daisy Buchanan.', 'img/the_great_gatsby.png', 11.99, id, 4.7, 0, NOW(), NOW()
FROM categories WHERE name = 'Classic Literature' AND NOT EXISTS (SELECT 1 FROM books WHERE title = 'The Great Gatsby');

-- Insert chapters if they don't exist
INSERT IGNORE INTO chapters (book_id, title, content, chapter_number, created_at, updated_at)
SELECT id, 'Chapter 1', 'Howard Roark laughed. He stood naked at the edge of a cliff. The lake lay far below him. A frozen explosion of granite burst in flight to the sky over motionless water. The water seemed immovable, the stone flowing. The stone had the stillness of one brief moment in battle when thrust meets thrust and the currents are held in a pause more dynamic than motion. The stone glowed, wet with sunrays.', 1, NOW(), NOW()
FROM books WHERE title = 'The Fountainhead' AND NOT EXISTS (SELECT 1 FROM chapters WHERE book_id = (SELECT id FROM books WHERE title = 'The Fountainhead') AND chapter_number = 1);

INSERT IGNORE INTO chapters (book_id, title, content, chapter_number, created_at, updated_at)
SELECT id, 'Chapter 2', 'The Dean of the Stanton School of Architecture was a man named Peter Keating. He was fifty years old and he had been the Dean for twenty years. He had been a good architect once, but he had not designed a building in a long time. He had been too busy with administrative duties.', 2, NOW(), NOW()
FROM books WHERE title = 'The Fountainhead' AND NOT EXISTS (SELECT 1 FROM chapters WHERE book_id = (SELECT id FROM books WHERE title = 'The Fountainhead') AND chapter_number = 2);

INSERT IGNORE INTO chapters (book_id, title, content, chapter_number, created_at, updated_at)
SELECT id, 'Chapter 1', 'On December 8th, 1915, Meggie Cleary had her fourth birthday. After the breakfast dishes were put away, her mother silently thrust a brown paper parcel into her arms and ordered her outside. So Meggie squatted down behind the gorse bush at the bottom of the garden and hugged the parcel tightly to her chest, too overexcited to open it at once.', 1, NOW(), NOW()
FROM books WHERE title = 'The Thorn Birds' AND NOT EXISTS (SELECT 1 FROM chapters WHERE book_id = (SELECT id FROM books WHERE title = 'The Thorn Birds') AND chapter_number = 1);

INSERT IGNORE INTO chapters (book_id, title, content, chapter_number, created_at, updated_at)
SELECT id, 'Chapter 2', 'The Clearys'' house was a rambling structure of weatherboard and iron, with a wide veranda running around three sides of it. The front door opened directly into the parlor, a room kept for best, with its horsehair furniture and the family Bible on a special table.', 2, NOW(), NOW()
FROM books WHERE title = 'The Thorn Birds' AND NOT EXISTS (SELECT 1 FROM chapters WHERE book_id = (SELECT id FROM books WHERE title = 'The Thorn Birds') AND chapter_number = 2);

-- Insert sample users with BCrypt encoded passwords
-- Password for both users is 'password'
INSERT INTO users (email, password, full_name, role, created_at, updated_at)
VALUES 
('admin@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Admin User', 'ADMIN', NOW(), NOW()),
('user@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Normal User', 'USER', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    password = VALUES(password),
    full_name = VALUES(full_name),
    role = VALUES(role),
    updated_at = NOW(); 