package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.CartItem;
import com.vanlang.webbanhang.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        return "/cart/cart";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(@RequestParam Long productId,
                                                         @RequestParam int quantity) {
        cartService.addToCart(productId, quantity);
        List<CartItem> cartItems = cartService.getCartItems();

        Map<String, Object> response = new HashMap<>();
        response.put("cartItems", cartItems);
        response.put("message", "Sản phẩm đã được thêm vào giỏ hàng thành công!");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove/{productId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        List<CartItem> cartItems = cartService.getCartItems();

        Map<String, Object> response = new HashMap<>();
        response.put("cartItems", cartItems);
        response.put("message", "Sản phẩm đã được xóa khỏi giỏ hàng!");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/clear")
    @ResponseBody
    public ResponseEntity<Map<String, String>> clearCart() {
        cartService.clearCart();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Giỏ hàng đã được xóa thành công!");
        return ResponseEntity.ok(response);
    }
}
