package com.example.cafeapp.services;

import com.example.cafeapp.controllers.OrderRequest;
import com.example.cafeapp.entities.Cafe;
import com.example.cafeapp.entities.MenuItem;
import com.example.cafeapp.entities.Order;
import com.example.cafeapp.entities.OrderItem;
import com.example.cafeapp.repositories.CafeRepository;
import com.example.cafeapp.repositories.MenuItemRepository;
import com.example.cafeapp.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private CafeRepository cafeRepository;

    /**
     * Accepts an order and updates its status to "Accepted" with the current acceptance time.
     * @param orderId The ID of the order to accept.
     */
    public void acceptOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus("Accepted");
        order.setAcceptanceTime(LocalDateTime.now());
        System.out.println("Order acceptance time set to: " + order.getAcceptanceTime());
        orderRepository.save(order);
    }

    /**
     * Retrieves all orders from the repository.
     * @return A list of all orders.
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Processes a new order by creating and saving it to the repository.
     * @param orderRequest The details of the order to process.
     */
    public void processOrder(OrderRequest orderRequest) {
        Order order = new Order();

        order.setClientEmail(orderRequest.getClientEmail());
        order.setAssignedCafe(orderRequest.getCafe());
        order.setClientName(orderRequest.getClientName());
        order.setClientPhone(orderRequest.getClientPhone());
        order.setLatitude(orderRequest.getLatitude());
        order.setLongitude(orderRequest.getLongitude());
        order.setAddress(orderRequest.getAddress());
        order.setStatus("Pending");

        List<OrderItem> orderItems = orderRequest.getItems().stream()
                .map(itemRequest -> {
                    MenuItem menuItem = menuItemRepository.findById(itemRequest.getId())
                            .orElseThrow(() -> new RuntimeException("MenuItem not found"));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setMenuItem(menuItem);
                    orderItem.setOrder(order);
                    orderItem.setQuantity(itemRequest.getQuantity());

                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);
        orderRepository.save(order);
    }

    /**
     * Retrieves all orders associated with a specific cafe's email.
     * @param email The email of the cafe.
     * @return A list of orders associated with the cafe.
     */
    public List<Order> getOrdersByCafeEmail(String email) {
        Cafe cafe = cafeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cafe not found"));
        return orderRepository.findByAssignedCafe(cafe.getName());
    }

    /**
     * Retrieves all orders associated with a specific user's email.
     * @param email The email of the user.
     * @return A list of orders associated with the user.
     */
    public List<Order> getOrdersByUserEmail(String email) {
        return orderRepository.findByClientEmail(email);
    }

    /**
     * Retrieves all orders available for couriers to accept.
     * Filters orders that have been accepted by a cafe but not yet assigned to a courier,
     * and ensures the order was accepted by the cafe within the last 3 minutes.
     * @param courierEmail The email of the courier (not currently used for filtering).
     * @return A list of orders available for couriers.
     */
    public List<Order> getOrdersForCourier(String courierEmail) {
        return orderRepository.findByStatus("Accepted").stream()
                .filter(order -> order.getAssignedCourier() == null)
                .filter(order -> {
                    LocalDateTime acceptanceTime = order.getAcceptanceTime();
                    return acceptanceTime != null && Duration.between(acceptanceTime, LocalDateTime.now()).toMinutes() <= 3;
                })
                .collect(Collectors.toList());
    }

    /**
     * Assigns an order to a courier, updating its status and assigned courier email.
     * @param orderId The ID of the order to accept.
     * @param courierEmail The email of the courier accepting the order.
     */
    public void acceptOrderByCourier(Long orderId, String courierEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!"Accepted".equals(order.getStatus())) {
            throw new RuntimeException("Courier can only accept orders that have been accepted by the cafe");
        }

        order.setStatus("Accepted by Courier");
        order.assignCourier(courierEmail);

        orderRepository.save(order);
    }
}
