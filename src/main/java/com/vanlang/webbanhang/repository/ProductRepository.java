package com.vanlang.webbanhang.repository;

import com.vanlang.webbanhang.model.Product;
import com.vanlang.webbanhang.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
