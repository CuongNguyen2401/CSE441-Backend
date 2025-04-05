package com.thanhtan.groceryshop.repository;

import com.thanhtan.groceryshop.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteByToken(String token);

    boolean existsByToken(String refreshToken);
}
