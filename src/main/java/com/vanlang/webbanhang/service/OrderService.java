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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartService cartService; // Assuming you have a CartService

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
        order = orderRepository.save(order);
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            order.setCustomerAddress(customerAddress);
            order.setCustomerEmail(customerEmail);
            order.setCustomerNote(customerNote);
            order.setCustomerPhoneNumber(customerPhoneNumber);
            order.setCustomerPayment(customerPayment);
            orderDetailRepository.save(detail);
        }
        // Optionally clear the cart after order placement
        cartService.clearCard();
        return order;
    }


}
