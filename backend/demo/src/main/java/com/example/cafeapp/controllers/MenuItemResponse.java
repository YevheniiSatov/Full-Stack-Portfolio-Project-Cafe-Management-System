package com.example.cafeapp.controllers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemResponse {
    private String name;
    private int quantity;

    public MenuItemResponse(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }


}