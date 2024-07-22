package com.vanlang.webbanhang.service;

import com.vanlang.webbanhang.model.CartItem;
import com.vanlang.webbanhang.model.Order;
import com.vanlang.webbanhang.model.OrderDetail;
import com.vanlang.webbanhang.repository.OrderDetailRepository;
import com.vanlang.webbanhang.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartService cartService;

    @Transactional
    public Order createOrder(String customerName, List<CartItem> cartItems,
                             String customerAddress,
                             String customerPhoneNumber,
                             String customerEmail,
                             String customerNote,
                             String customerPayment) {
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setCustomerAddress(customerAddress);
        order.setCustomerEmail(customerEmail);
        order.setCustomerNote(customerNote);
        order.setCustomerPhoneNumber(customerPhoneNumber);
        order.setCustomerPayment(customerPayment);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order = orderRepository.save(order);

        double total = 0;
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getProduct().getPrice());
            total += detail.getQuantity() * detail.getPrice();
            orderDetailRepository.save(detail);
        }
        order.setTotal(total);
        orderRepository.save(order);

        cartService.clearCart();
        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getRecentOrders(int limit) {
        return orderRepository.findTop10ByOrderByOrderDateDesc();
    }

    public boolean confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null && order.getStatus().equals("PENDING")) {
            order.setStatus("CONFIRMED");
            order.setConfirmationDate(LocalDate.now());
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    public List<Map<String, Object>> getSalesData(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderRepository.findByOrderDateBetween(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        return orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().toLocalDate(),
                        Collectors.summingDouble(Order::getTotal)
                ))
                .entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("date", entry.getKey());
                    map.put("amount", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }


    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }
}