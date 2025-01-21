package com.example.cafeapp.controllers;

import com.example.cafeapp.entities.MenuItem;
import com.example.cafeapp.entities.Order;
import com.example.cafeapp.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cafe/orders")
public class CafeOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Retrieves orders associated with a specific cafe identified by the provided email.
     * @param email The email address of the cafe.
     * @return A list of orders formatted as OrderResponse objects.
     */
    @GetMapping
    public List<OrderResponse> getOrdersForCafe(@RequestParam String email) {
        List<Order> orders = orderService.getOrdersByCafeEmail(email);

        return orders.stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getStatus(),
                        order.getClientName(),
                        order.getClientPhone(),
                        order.getOrderItems().stream()
                                .map(orderItem -> {
                                    MenuItem menuItem = orderItem.getMenuItem();
                                    return new MenuItemResponse(menuItem.getName(), orderItem.getQuantity());
                                })
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all orders available in the system.
     * @return A list of all orders.
     */
    @GetMapping("/all")
    public List<Order> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        System.out.println("Total number of orders: " + orders.size());
        return orders;
    }

    /**
     * Marks a specific order as accepted.
     * @param orderId The ID of the order to accept.
     * @return A response message confirming the action.
     */
    @PostMapping("/{orderId}/accept")
    public ResponseEntity<String> acceptOrder(@PathVariable Long orderId) {
        orderService.acceptOrder(orderId);
        return ResponseEntity.ok("Order accepted!");
    }
}
