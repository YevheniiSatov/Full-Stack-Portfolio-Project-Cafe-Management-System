package com.example.cafeapp.controllers;

import com.example.cafeapp.entities.Order;
import com.example.cafeapp.services.OrderService;
import com.example.cafeapp.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courier/orders")
public class CourierOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Retrieves available orders for the courier.
     * Extracts the courier's email from the provided JWT token.
     * @param token Authorization header containing the JWT token.
     * @return ResponseEntity with a list of orders available for the courier.
     */
    @GetMapping
    public ResponseEntity<List<Order>> getCourierOrders(@RequestHeader("Authorization") String token) {
        // Extract email from the token
        String jwtToken = token.replace("Bearer ", "");
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);

        // Retrieve orders for the courier
        List<Order> orders = orderService.getOrdersForCourier(email);

        return ResponseEntity.ok(orders);
    }

    /**
     * Allows the courier to accept an order.
     * Extracts the courier's email from the provided JWT token and associates the courier with the order.
     * @param token Authorization header containing the JWT token.
     * @param orderId ID of the order to be accepted.
     * @return ResponseEntity with a confirmation message.
     */
    @PostMapping("/{orderId}/accept")
    public ResponseEntity<String> acceptOrder(@RequestHeader("Authorization") String token, @PathVariable Long orderId) {
        // Extract courier's email from the token
        String jwtToken = token.replace("Bearer ", "");
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);

        // Accept the order on behalf of the courier
        orderService.acceptOrderByCourier(orderId, email);
        return ResponseEntity.ok("Order successfully accepted!");
    }
}
