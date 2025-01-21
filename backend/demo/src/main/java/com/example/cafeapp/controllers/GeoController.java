package com.example.cafeapp.controllers;

import com.example.cafeapp.services.GeoService;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Coordinate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/geo")
@CrossOrigin
public class GeoController {

    @Autowired
    private GeoService geoService;
    private static final Logger logger = LoggerFactory.getLogger(GeoController.class);


    public GeoController(GeoService geoService) {
        this.geoService = geoService;
    }
        /*
    @PostMapping("/location")
    public ResponseEntity<String> getCafeByLocation(@RequestBody LocationRequest locationRequest) {
        double latitude = locationRequest.getLatitude();
        double longitude = locationRequest.getLongitude();

        // Логируем координаты пользователя
        System.out.println("Координаты пользователя: Широта = " + latitude + ", Долгота = " + longitude);

        String cafeName;

        // Проверяем, находится ли пользователь в регионе
        if (geoService.isLocationInRegion(latitude, longitude, "3523746")) {
            System.out.println("Пользователь в
            cafeName = "
        } else if (geoService.isLocationInRegion(latitude, longitude, "3523806")) {
            System.out.println("Пользователь в ");
            cafeName =
        } else {
            // Если не найдено кафе, возвращаем "Unknown"
            System.out.println("Кафе не найдено для данных координат");
            cafeName = "Unknown";
        }

        // Возвращаем простую строку с названием кафе
        return ResponseEntity.ok(cafeName);
    }

         */



    /**
     * stable for react 17.10.2024
     */

    /**
     * Determines the cafe based on the provided latitude and longitude.
     * @param locationRequest Contains latitude and longitude coordinates.
     * @return The name of the cafe in the region or "Unknown" if no cafe is found.
     */
    @PostMapping("/location")
    public String getCafeByLocation(@RequestBody LocationRequest locationRequest) {
        double latitude = locationRequest.getLatitude();
        double longitude = locationRequest.getLongitude();

        // Log user coordinates
        logger.info("User coordinates: Latitude = {}, Longitude = {}", latitude, longitude);

        // Check if the user is in the Kazacok region
        if (geoService.isLocationInRegion(latitude, longitude, "3523746")) {
            logger.info("User is in Kazacok region");
            return "Kazacok";
        }

        // Check if the user is in the MÁJ region
        if (geoService.isLocationInRegion(latitude, longitude, "3523806")) {
            logger.info("User is in MÁJ region");
            return "MÁJ";
        }

        // Return "Unknown" if no cafe is found for the coordinates
        logger.info("No cafe found for the provided coordinates");
        return "Unknown";
    }
    @PostMapping("/address")
    public ResponseEntity<String> getCafeByAddress(@RequestParam String address) {
        logger.info("Received request to determine cafe by address: {}", address);

        try {
            Optional<Coordinate> coordinates = geoService.getCoordinatesByAddress(address);

            if (coordinates.isPresent()) {
                Coordinate coordinate = coordinates.get();
                logger.info("Coordinates for address {}: Latitude = {}, Longitude = {}", address, coordinate.y, coordinate.x);

                if (geoService.isLocationInRegion(coordinate.y, coordinate.x, "3523746")) {
                    logger.info("User is in Kazacok region");
                    return ResponseEntity.ok("Kazacok");
                }

                if (geoService.isLocationInRegion(coordinate.y, coordinate.x, "3523806")) {
                    logger.info("User is in MÁJ region");
                    return ResponseEntity.ok("MÁJ");
                }
            } else {
                logger.warn("Unable to determine coordinates for address: {}", address);
            }

            return ResponseEntity.ok("No cafe found");
        } catch (Exception e) {
            logger.error("Error processing address: {}", address, e);
            return ResponseEntity.status(500).body("Error processing address");
        }
    }

    /**
     * Inner class for accepting location coordinates.
     */
    @Setter
    @Getter
    public static class LocationRequest {
        private double latitude;
        private double longitude;
    }
}
