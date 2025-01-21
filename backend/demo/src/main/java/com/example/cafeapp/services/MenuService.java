package com.example.cafeapp.services;

import com.example.cafeapp.entities.MenuItem;
import com.example.cafeapp.repositories.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<MenuItem> getMenuItems() {
        return menuItemRepository.findAll();
    }

    public MenuItem addMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getMenuItemsByCafe(String cafeName) {
        return menuItemRepository.findByCafeName(cafeName);
    }
}
