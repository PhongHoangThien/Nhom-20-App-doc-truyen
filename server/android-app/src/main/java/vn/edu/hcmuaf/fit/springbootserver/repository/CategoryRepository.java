package vn.edu.hcmuaf.fit.springbootserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmuaf.fit.springbootserver.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
} 