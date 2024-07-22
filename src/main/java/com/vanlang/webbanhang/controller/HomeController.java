package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.Product;
import com.vanlang.webbanhang.service.CategoryService;
import com.vanlang.webbanhang.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;


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
}
