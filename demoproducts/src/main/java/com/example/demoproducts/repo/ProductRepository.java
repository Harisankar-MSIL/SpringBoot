package com.example.demoproducts.repo;

import com.example.demoproducts.dto.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    @Query("SELECT p FROM ProductEntity p WHERE p.title LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<ProductEntity> searchByProduct(@Param("keyword") String keyword);
    @Query("SELECT p FROM ProductEntity p WHERE p.category LIKE %:keyword%")
    List<ProductEntity> searchByCategory(@Param("keyword") String keyword);
    @Modifying
    @Query("DELETE FROM ProductEntity p WHERE p.id = :id")
    void deleteByCategory(@Param("id") String id);



}
