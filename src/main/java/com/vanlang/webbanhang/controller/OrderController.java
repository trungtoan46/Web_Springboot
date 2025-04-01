package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.CartItem;
import com.vanlang.webbanhang.model.User;
import com.vanlang.webbanhang.service.CartService;
import com.vanlang.webbanhang.service.OrderService;
import com.vanlang.webbanhang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    @GetMapping("/checkout")
    public String checkout(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(authentication.getName());
        model.addAttribute("user",user);
        return "cart/checkout";
    }

    @PostMapping("/submit")
    public String submitOrder(@RequestParam("customerName") String customerName,
                              @RequestParam("customerAddress") String customerAddress,
                              @RequestParam("customerPhoneNumber") String customerPhoneNumber,
                              @RequestParam("customerEmail") String customerEmail,
                              @RequestParam("customerNote") String customerNote,
                              @RequestParam("customerPayment") String customerPayment) {
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Redirect if cart is empty
        }
        orderService.createOrder(customerName, cartItems, customerAddress, customerPhoneNumber, customerEmail, customerNote, customerPayment);
        return "redirect:/order/confirmation";
    }

    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "cart/order-confirmation";
    }

    @PostMapping("/admin/orders/{orderId}/confirm")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> confirmOrder(@PathVariable Long orderId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = orderService.confirmOrder(orderId);
            response.put("success", success);
            if (!success) {
                response.put("message", "Order not found or already confirmed");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}