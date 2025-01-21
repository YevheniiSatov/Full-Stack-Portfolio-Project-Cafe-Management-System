package com.example.cafeapp.controllers;

import com.example.cafeapp.entities.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtResponse {
    private User user;
    private String token;
    private String role;

    public JwtResponse(User user, String token,String role) {
        this.user = user;
        this.token = token;
        this.role = role;
    }

}
