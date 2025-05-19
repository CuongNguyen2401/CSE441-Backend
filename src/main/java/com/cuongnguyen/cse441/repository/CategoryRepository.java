package com.cuongnguyen.cse441.repository;

import com.cuongnguyen.cse441.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.name = ?1")
    long countProductByCategory(String categoryName);

    Optional<Category> findByName(String name);
}

