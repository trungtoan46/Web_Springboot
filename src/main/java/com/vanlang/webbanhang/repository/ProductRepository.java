package com.vanlang.webbanhang.repository;

import com.vanlang.webbanhang.model.Product;
import com.vanlang.webbanhang.service.ProductService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<ProductService> findByName(String name);
}
