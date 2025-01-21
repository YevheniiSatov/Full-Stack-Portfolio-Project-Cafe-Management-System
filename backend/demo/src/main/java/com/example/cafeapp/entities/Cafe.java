package com.example.cafeapp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Cafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;


    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
