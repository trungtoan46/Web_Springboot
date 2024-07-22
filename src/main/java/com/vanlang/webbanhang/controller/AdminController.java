package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.Product;
import com.vanlang.webbanhang.model.User;
import com.vanlang.webbanhang.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @GetMapping()
    public String adminDashboard(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("recentOrders", orderService.getRecentOrders(10));
        model.addAttribute("salesData", orderService.getSalesData(LocalDate.now().minusDays(30), LocalDate.now()));
        return "admin/dashboard";
    }
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/products")
    public String manageProducts(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.getAllProducts(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());

        return "admin/products_management";
    }

    @PostMapping("/orders/{orderId}/confirm")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> confirmOrder(@PathVariable Long orderId) {
        boolean success = orderService.confirmOrder(orderId);
        return ResponseEntity.ok(Map.of("success", success));
    }

    @GetMapping("/sales-data")
    @ResponseBody
    public List<Map<String, Object>> getSalesData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return orderService.getSalesData(startDate, endDate);
    }
}