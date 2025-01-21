package com.example.cafeapp.services;

import com.example.cafeapp.entities.District;
import com.example.cafeapp.repositories.DistrictRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CafeService {

    @Autowired
    private DistrictRepository districtRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();

    /**
     * Determines the cafe based on the user's geographic location.
     * Iterates through all districts to check if the user is within a specific district boundary.
     * @param latitude The latitude of the user's location.
     * @param longitude The longitude of the user's location.
     * @return The name of the district containing the user's location, or "Unknown" if no match is found.
     */
    public String getCafeByLocation(double latitude, double longitude) {
        Coordinate userLocation = new Coordinate(latitude, longitude);

        // Iterate through all districts to find a match based on user location
        Optional<District> district = districtRepository.findAll().stream()
                .filter(d -> d.getBoundary().contains(geometryFactory.createPoint(userLocation)))
                .findFirst();

        return district.map(District::getName).orElse("Unknown");
    }
}
