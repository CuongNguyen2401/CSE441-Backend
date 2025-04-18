package com.cuongnguyen.cse441.repository;

import com.cuongnguyen.cse441.entity.Category;
import com.cuongnguyen.cse441.entity.Order;
import com.cuongnguyen.cse441.entity.Product;
import com.cuongnguyen.cse441.enums.ProductStatus;
import com.cuongnguyen.cse441.repository.custom.CustomProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> , QuerydslPredicateExecutor<Order> , CustomProductRepository {
    List<Product> findByCategoryName(String categoryName);

    Product findByCategory_Name(String name);

    List<Product> findByCategory_Products_Price(double price);

    Optional<Product> findBySlug(String slug);

    List<Product> findByRelatedProductsContains(Product product);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.productStatus = :status WHERE p.id = :id")
    void updateStatusById(@Param("id") Long id, @Param("status") ProductStatus status);


    List<Product> findByCategory(Category category);
}