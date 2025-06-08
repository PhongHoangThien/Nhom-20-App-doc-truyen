package vn.edu.hcmuaf.fit.springbootserver.repository;

import vn.edu.hcmuaf.fit.springbootserver.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
} 