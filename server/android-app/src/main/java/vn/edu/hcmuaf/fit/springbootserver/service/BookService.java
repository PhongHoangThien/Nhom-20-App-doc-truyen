package vn.edu.hcmuaf.fit.springbootserver.service;

import org.springframework.data.domain.Pageable;
import vn.edu.hcmuaf.fit.springbootserver.entity.Book;
import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    List<Book> getPopularBooks(Pageable pageable);
    List<Book> getBooksByCategory(Long categoryId);
    Book getBookById(Long id);
    Book createBook(Book book);
    Book updateBook(Long id, Book book);
    void deleteBook(Long id);
} 