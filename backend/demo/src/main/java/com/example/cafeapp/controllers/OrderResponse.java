package com.example.cafeapp.controllers;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private String status;
    private String clientName;
    private String clientPhone;
    private String clientEmail;

    private List<MenuItemResponse> items;

    public OrderResponse(Long id, String status, String clientName, String clientPhone, List<MenuItemResponse> items) {
        this.id = id;
        this.status = status;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.items = items;
    }
}
