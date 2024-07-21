package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.Category;
import com.vanlang.webbanhang.model.Product;
import com.vanlang.webbanhang.service.CategoryService;
import com.vanlang.webbanhang.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/products")
public class ProductApiController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Page<Product> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllProducts(pageable);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestParam("image") MultipartFile image,
                                           @RequestParam("name") String name,
                                           @RequestParam("price") Double price,
                                           @RequestParam("description") String description,
                                           @RequestParam("author") String author,
                                           @RequestParam("categoryId") Long categoryId) throws IOException {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setAuthor(author);

        Category category = categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        product.setCategory(category);



        Product savedProduct = productService.addProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok().body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @RequestParam("name") String name,
                                           @RequestParam("price") Double price,
                                           @RequestParam("description") String description,
                                           @RequestParam("author") String author,
                                           @RequestParam("categoryId") Long categoryId) throws IOException {
        Product product = productService.getProductById(id);
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setAuthor(author);

        Category category = categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        product.setCategory(category);

        Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        productService.deleteProductById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/authors")
    public ResponseEntity<List<String>> getAllAuthor() {
        return ResponseEntity.ok(productService.getAllDistinctAuthors());
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/images")
    public ResponseEntity<List<String>> getAllImages() {
        List<String> images = productService.getAllDistinctImages();
        return ResponseEntity.ok(images);
    }

}