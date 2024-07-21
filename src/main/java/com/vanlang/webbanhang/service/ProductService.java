package com.vanlang.webbanhang.service;

import com.vanlang.webbanhang.model.Product;
import com.vanlang.webbanhang.repository.ProductRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product with ID " + id + " does not exist."));
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(@NonNull Product product) {
        Product existingProduct = getProductById(product.getId());
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setAuthor(product.getAuthor());
        existingProduct.setImage(product.getImage());
        return productRepository.save(existingProduct);
    }

    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("Product with ID " + id + " does not exist.");
        }
        productRepository.deleteById(id);
    }

    public Page<Product> getAllProductsSorted(String sortBy, Pageable pageable) {
        return switch (sortBy) {
            case "priceAsc" -> productRepository.findAllByOrderByPriceAsc(pageable);
            case "priceDesc" -> productRepository.findAllByOrderByPriceDesc(pageable);
            case "nameAsc" -> productRepository.findAllByOrderByNameAsc(pageable);
            case "nameDesc" -> productRepository.findAllByOrderByNameDesc(pageable);
            default -> productRepository.findAll(pageable);
        };
    }

    public Page<Product> getProductsByAuthor(String author, Pageable pageable) {
        return productRepository.findAllByAuthor(author, pageable);
    }

    public Page<Product> getProductsByCategory(String category, Pageable pageable) {
        return productRepository.findAllByCategory_Name(category, pageable);
    }

    public List<String> getAllDistinctImages() {
        return productRepository.findAllDistinctImages();
    }

    public Page<Product> getProductsByPriceRange(Double minPrice, Double maxPrice, Pageable pageable) {
        return productRepository.findAllByPriceBetweenOrderByPriceAsc(minPrice, maxPrice, pageable);
    }

    public List<String> getAllDistinctAuthors() {
        return productRepository.findAllDistinctAuthors();
    }
}