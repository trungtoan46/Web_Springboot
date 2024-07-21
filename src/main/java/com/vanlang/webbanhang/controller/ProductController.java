package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.Product;
import com.vanlang.webbanhang.service.CategoryService;
import com.vanlang.webbanhang.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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
    private CategoryService categoryService;// Đảm bảo bạn đã inject CategoryService
    // Display a list of all products
    @GetMapping
    public String showProducts(@RequestParam(required = false) String sortBy,
                               @RequestParam(required = false) Double minPrice,
                               @RequestParam(required = false) Double maxPrice,
                               @RequestParam(required = false) String author,
                               @RequestParam(required = false) String category,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        Page<Product> productPage;
        List<String> authors = productService.getAllDistinctAuthors();
        if (minPrice != null && maxPrice != null) {
            productPage = productService.getProductsByPriceRange(minPrice, maxPrice, PageRequest.of(page, size));
        } else if (author != null && !author.isEmpty()) {
            productPage = productService.getProductsByAuthor(author, PageRequest.of(page, size));
        } else if (sortBy != null) {
            productPage = productService.getAllProductsSorted(sortBy, PageRequest.of(page, size));
        } else if (category != null && !category.isEmpty()) {
            productPage = productService.getProductsByCategory(category, PageRequest.of(page, size));
        }
        else {
            productPage = productService.getAllProducts(PageRequest.of(page, size));
        }
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());

        model.addAttribute("authors",authors);
        model.addAttribute("selectedAuthor", author);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("selectedCategory", category);

        return "/products/products-list";
    }
    @GetMapping("/authors")
    public List<String> getAuthors() {
        return productService.getAllDistinctAuthors();
    }

    // For adding a new product
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories",categoryService.getAllCategories());// Load categories
        return "/products/add-product";
    }
    // Process the form for adding a new product
    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult bindingResult)
                              {
        if (bindingResult.hasErrors()) {
            return "products/add-product";
        }
        productService.addProduct(product);
        return "redirect:/products";
    }
    // For editing a product
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories",categoryService.getAllCategories());//Load categories
        return "/products/edit-product";
    }
    // Process the form for updating a product
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @Valid Product product, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            product.setId(id);//// set id to keep it in the form in case of errors
            return "edit-product";
        }
        productService.updateProduct(product);
        return "redirect:/products";
    }

    // Handle request to delete a product
    @GetMapping("/delete/{id}")
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
