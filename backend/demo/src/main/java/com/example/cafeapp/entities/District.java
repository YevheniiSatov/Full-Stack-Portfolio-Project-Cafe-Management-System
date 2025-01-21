package com.example.cafeapp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Polygon;

@Entity
@Getter
@Setter
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String osmId;


    @Column(nullable = false, columnDefinition = "geometry")
    private Polygon boundary;
}
