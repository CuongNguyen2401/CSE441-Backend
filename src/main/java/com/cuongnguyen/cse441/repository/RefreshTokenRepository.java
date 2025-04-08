package com.cuongnguyen.cse441.repository;

import com.cuongnguyen.cse441.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteByToken(String token);

    boolean existsByToken(String refreshToken);
}
