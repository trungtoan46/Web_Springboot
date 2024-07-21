package com.vanlang.webbanhang.repository;

import com.vanlang.webbanhang.model.Category;
import com.vanlang.webbanhang.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c.name FROM Category c")
    List<String> findAllCategoryNames();

    List<Product> findAllByName(String name);
}
