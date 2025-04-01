package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.Product;
import com.vanlang.webbanhang.service.CategoryService;
import com.vanlang.webbanhang.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping()
    public String Home(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "9") int size,
                       @RequestParam(required = false) String sortBy,
                       @RequestParam(required = false) String category,
                       @RequestParam(required = false) String author,
                       @RequestParam(required = false) Double minPrice,
                       @RequestParam(required = false) Double maxPrice,
                       @RequestParam(required = false) String keyword) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;

        if (keyword != null && !keyword.isEmpty()) {
            productPage = productService.searchProducts(keyword, pageable);
        } else if (category != null && !category.isEmpty()) {
            productPage = productService.getProductsByCategory(category, pageable);
        } else if (author != null && !author.isEmpty()) {
            productPage = productService.getProductsByAuthor(author, pageable);
        } else if (minPrice != null && maxPrice != null) {
            productPage = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);
        } else if (sortBy != null && !sortBy.isEmpty()) {
            productPage = productService.getAllProductsSorted(sortBy, pageable);
        } else {
            productPage = productService.getAllProducts(pageable);
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("authors", productService.getAllDistinctAuthors());
        model.addAttribute("totalProducts", productService.getTotalProductCount());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("category", category);
        model.addAttribute("author", author);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("keyword", keyword);

        return "products/products-listing";
    }
    @GetMapping("/authors")
    public List<String> getAuthors() {
        return productService.getAllDistinctAuthors();
    }



    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories",categoryService.getAllCategories());
        return "/products/add-product";
    }



    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult bindingResult)
                              {
        if (bindingResult.hasErrors()) {
            return "products/add-product";
        }
        productService.addProduct(product);
        return "redirect:/products";
    }



    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        logger.info("Attempting to show edit form for product with id: {}", id);
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                logger.warn("Product with id {} not found", id);
                return "redirect:/products";
            }
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            logger.info("Edit form loaded for product id: {}", id);
            return "products/edit-product";
        } catch (Exception e) {
            logger.error("Error occurred while loading edit form for product id: {}", id, e);
            throw e;
        }
    }


    // Process the form for updating a product
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult bindingResult,
                                Authentication authentication,
                                Model model) {
        logger.info("Attempting to update product with id: {}", id);
        logger.info("User authorities: {}", authentication.getAuthorities());
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors occurred while updating product id: {}", id);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "products/edit-product";
        }

        try {
            productService.updateProduct(product);
            logger.info("Successfully updated product with id: {}", id);
            return "redirect:/products";
        } catch (Exception e) {
            logger.error("Error updating product with id: {}. Error: {}", id, e.getMessage(), e);
            model.addAttribute("errorMessage", "Failed to update product: " + e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "products/edit-product";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }

    @GetMapping("/id/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null){
            return "/products/404";
        }
        model.addAttribute("product",product);
        return "/products/product-detail";
    }


}
