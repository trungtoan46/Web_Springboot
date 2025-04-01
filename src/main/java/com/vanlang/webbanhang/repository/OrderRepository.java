package com.vanlang.webbanhang.repository;

import com.vanlang.webbanhang.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findTop10ByOrderByOrderDateDesc();
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}