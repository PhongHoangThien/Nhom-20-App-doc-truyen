package vn.edu.hcmuaf.fit.springbootserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmuaf.fit.springbootserver.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
} 