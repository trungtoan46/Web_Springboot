package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.Category;
import com.vanlang.webbanhang.model.Product;
import com.vanlang.webbanhang.service.CategoryService;
import com.vanlang.webbanhang.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController{

    private ProductService productService;
    private CategoryService categoryService;
    @GetMapping("/")
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
            productPage = (sortBy != null)
                    ? productService.getAllProductsSorted(sortBy, PageRequest.of(page, size))
                    : productService.getAllProducts(PageRequest.of(page, size));
        }
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());

        model.addAttribute("newProducts", productService.getRecentlyAddedProducts());

        model.addAttribute("top5",productService.getTopSellerProducts());
        model.addAttribute("authors",authors);
        model.addAttribute("selectedAuthor", author);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("sortBy", sortBy);

        return "/home";
    }


}
