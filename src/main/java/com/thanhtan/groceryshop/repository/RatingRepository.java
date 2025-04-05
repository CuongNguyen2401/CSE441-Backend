package com.thanhtan.groceryshop.repository;

import com.thanhtan.groceryshop.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT AVG(r.rate) FROM Rating r WHERE r.product.id = :productId")
    double getAverageRating(Long productId);

    Optional<Rating> findByProductIdAndUserId(Long productId, Long userId);

    @Transactional
    @Modifying
    @Query("update Rating r set r.rate = ?1 where r.id = ?2")
    void updateRate(double newRate, Long id);
}