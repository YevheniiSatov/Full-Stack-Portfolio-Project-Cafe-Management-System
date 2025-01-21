package com.example.cafeapp.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private String status;
    private double latitude;
    private double longitude;
    private String clientName;
    private String clientPhone;
    private String assignedCafe;
    private String clientEmail;
    private String assignedCourier;  // Email of the courier who accepted the order

    // Field for the time when the order was accepted by the cafe
    private LocalDateTime acceptanceTime; // Timestamp of cafe's order acceptance

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems;

    /**
     * Sets the list of order items and establishes the bidirectional relationship.
     * @param orderItems List of order items associated with this order.
     */
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        for (OrderItem item : orderItems) {
            item.setOrder(this);
        }
    }

    /**
     * Assigns a courier to this order.
     * @param courierEmail Email of the courier accepting the order.
     */
    public void assignCourier(String courierEmail) {
        this.assignedCourier = courierEmail;
    }
}
