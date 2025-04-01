package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.Order;
import com.vanlang.webbanhang.service.OrderService;
import com.vanlang.webbanhang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class OrderAdminController {
    @Autowired
    private OrderService orderService;
    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin/orders";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        System.out.println(order.getTotal());
        return "admin/orders-detail";
    }

    @GetMapping("/edit/{id}")
    public String editOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            // Redirect hoặc trả về trang lỗi nếu không tìm thấy đơn hàng
            return "redirect:/error";
        }

        System.out.println("Order Payment Method: " + order.getCustomerPayment());

        model.addAttribute("order", order);
        return "admin/order-edit";
    }

    @PostMapping("/update")
    public String updateOrder(@ModelAttribute Order order, Model model) {
        try {
            orderService.save(order);
            return "redirect:/admin/orders/" + order.getId(); // Redirect đến trang chi tiết của đơn hàng sau khi cập nhật
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi cập nhật đơn hàng.");
            return "admin/order-edit"; // Trở lại trang chỉnh sửa với thông báo lỗi
        }
    }

}
