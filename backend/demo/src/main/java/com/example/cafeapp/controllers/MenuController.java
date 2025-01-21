package com.example.cafeapp.controllers;

import com.example.cafeapp.entities.MenuItem;
import com.example.cafeapp.services.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;



    @PostMapping
    public MenuItem addMenuItem(@RequestBody MenuItem menuItem) {
        return menuService.addMenuItem(menuItem);
    }


    @GetMapping
    public List<MenuItem> getMenuItems(@RequestParam String cafe) {
        return menuService.getMenuItemsByCafe(cafe);
    }
}
