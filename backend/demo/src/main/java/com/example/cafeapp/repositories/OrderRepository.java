package com.example.cafeapp.repositories;

import com.example.cafeapp.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
    List<Order> findByAssignedCafe(String assignedCafe);
    List<Order> findAll();

    List<Order> findByClientEmail(String clientEmail);
}