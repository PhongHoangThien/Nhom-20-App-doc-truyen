package vn.edu.hcmuaf.fit.springbootserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.hcmuaf.fit.springbootserver.entity.UserToken;
import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    @Query("SELECT ut FROM UserToken ut WHERE ut.token = :token AND ut.isValid = true AND ut.expiresAt > CURRENT_TIMESTAMP")
    Optional<UserToken> findValidToken(@Param("token") String token);

    @Query("SELECT ut FROM UserToken ut WHERE ut.user.id = :userId AND ut.isValid = true AND ut.expiresAt > CURRENT_TIMESTAMP")
    Optional<UserToken> findValidTokenByUserId(@Param("userId") Long userId);

    void deleteByUser_Id(Long userId);
} 