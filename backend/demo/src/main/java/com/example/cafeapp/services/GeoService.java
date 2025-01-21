package com.example.cafeapp.services;

import com.example.cafeapp.dto.NominatimResponse;
import com.example.cafeapp.entities.District;
import com.example.cafeapp.repositories.DistrictRepository;
import lombok.Setter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
@Setter
public class GeoService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private DistrictRepository districtRepository;

    private GeometryFactory geometryFactory = new GeometryFactory();
    private static final Logger log = LoggerFactory.getLogger(GeoService.class);

    /**
     * Checks if a given location (latitude and longitude) is within a specific district.
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param osmId The OpenStreetMap ID of the district.
     * @return true if the location is within the district, false otherwise.
     */
    public boolean isLocationInRegion(double latitude, double longitude, String osmId) {
        Coordinate userLocation = new Coordinate(latitude, longitude);

        // Retrieve the district by its OSM ID
        Optional<District> districtOptional = districtRepository.findByOsmId(osmId);
        if (districtOptional.isPresent()) {
            District district = districtOptional.get();

            // Log district boundary and user coordinates
            log.info("District boundaries (WKT): {}", district.getBoundary().toText());
            log.info("User coordinates: Latitude = {}, Longitude = {}", latitude, longitude);

            // Check if the user is within the district boundaries
            boolean isInRegion = district.getBoundary().contains(geometryFactory.createPoint(userLocation));

            log.info("User is within district boundaries: {}", isInRegion);

            return isInRegion;
        } else {
            log.warn("District with OSM ID {} not found!", osmId);
            return false;
        }
    }

    /**
     * Retrieves geographic coordinates for a given address using the Nominatim API.
     * @param address The address to be geocoded.
     * @return Optional containing the coordinates if found, or an empty Optional otherwise.
     */
    public Optional<Coordinate> getCoordinatesByAddress(String address) {
        String url = "https://nominatim.openstreetmap.org/search?q=" + address + "&format=json&limit=1";
        try {
            ResponseEntity<NominatimResponse[]> response = restTemplate.getForEntity(url, NominatimResponse[].class);

            if (response.getBody() != null && response.getBody().length > 0) {
                NominatimResponse location = response.getBody()[0];
                double latitude = Double.parseDouble(location.getLat());
                double longitude = Double.parseDouble(location.getLon());
                return Optional.of(new Coordinate(longitude, latitude));
            } else {
                log.warn("Failed to find coordinates for address: {}", address);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Error while querying Nominatim API for address: {}", address, e);
            return Optional.empty();
        }
    }
}
