package com.vanlang.webbanhang.service;

import com.vanlang.webbanhang.model.OrderDetail;
import com.vanlang.webbanhang.model.Product;
import com.vanlang.webbanhang.repository.OrderDetailRepository;
import com.vanlang.webbanhang.repository.ProductRepository;
import groovyjarjarantlr4.v4.runtime.tree.pattern.ParseTreePattern;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product with ID " + id + " does not exist."));
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public void updateProduct(Product product) {
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setAuthor(product.getAuthor());
        existingProduct.setImage(product.getImage());

        productRepository.save(existingProduct);
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



    public long getTotalProductCount() {
        return productRepository.count();
    }

    public List<Product> getTopSellerProducts() {
        // Tính tổng số lượng đơn hàng cho mỗi sản phẩm
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        Map<Product, Integer> productOrderCount = new HashMap<>();

        for (OrderDetail orderDetail : orderDetails) {
            Product product = orderDetail.getProduct();
            productOrderCount.put(product, productOrderCount.getOrDefault(product, 0) + orderDetail.getQuantity());
        }

        // Sắp xếp sản phẩm theo số lượng đơn hàng giảm dần
        return productOrderCount.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<Product> getRecentlyAddedProducts() {
        return productRepository.findTop5ByOrderByCreatedAtDesc();
    }


}