package vn.edu.hcmuaf.fit.springbootserver.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.hcmuaf.fit.springbootserver.entity.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b ORDER BY b.viewCount DESC")
    List<Book> findPopularBooks(Pageable pageable);

    List<Book> findByCategoryId(Long categoryId);
}