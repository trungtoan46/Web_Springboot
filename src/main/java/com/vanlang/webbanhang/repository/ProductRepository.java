package com.vanlang.webbanhang.repository;

import com.vanlang.webbanhang.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);
    Page<Product> findAllByOrderByPriceDesc(Pageable pageable);
    Page<Product> findAllByOrderByNameAsc(Pageable pageable);
    Page<Product> findAllByOrderByNameDesc(Pageable pageable);
    Page<Product> findAllByAuthor(String author, Pageable pageable);
    Page<Product> findAllByCategory_Name(String category, Pageable pageable);
    Page<Product> findAllByPriceBetweenOrderByPriceAsc(Double minPrice, Double maxPrice, Pageable pageable);

    @Query("SELECT DISTINCT p.image FROM Product p WHERE p.image IS NOT NULL AND p.image <> ''")
    List<String> findAllDistinctImages();

    @Query("SELECT DISTINCT p.author FROM Product p WHERE p.author IS NOT NULL AND p.author <> ''")
    List<String> findAllDistinctAuthors();
}