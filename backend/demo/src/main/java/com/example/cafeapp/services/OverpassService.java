package com.example.cafeapp.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class OverpassService {

    private static final String OVERPASS_API_URL = "https://overpass-api.de/api/interpreter?data=[out:json];relation(%d);out geom;";

    /**
     * Fetches polygon geometry from OpenStreetMap for a given OSM ID.
     * @param osmId The OpenStreetMap relation ID.
     * @return A Polygon representing the geometry of the OSM relation.
     * @throws IOException If the geometry cannot be retrieved or parsed.
     */
    public Polygon getPolygonFromOSM(long osmId) throws IOException {
        String url = String.format(OVERPASS_API_URL, osmId);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        // Parse the response and construct a polygon
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode geometry = jsonNode.findPath("geometry");

        if (geometry.isMissingNode()) {
            throw new IOException("Geometry not found for OSM ID: " + osmId);
        }

        // Create a polygon based on coordinates
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] coordinates = new Coordinate[geometry.size()];
        for (int i = 0; i < geometry.size(); i++) {
            JsonNode point = geometry.get(i);
            coordinates[i] = new Coordinate(point.get("lon").asDouble(), point.get("lat").asDouble());
        }

        LinearRing shell = geometryFactory.createLinearRing(coordinates);
        return geometryFactory.createPolygon(shell);
    }

    /**
     * Checks if a given latitude and longitude are within a specified polygon.
     * @param latitude The latitude of the point.
     * @param longitude The longitude of the point.
     * @param polygon The polygon to check against.
     * @return True if the point is within the polygon, false otherwise.
     */
    public boolean isPointInPolygon(double latitude, double longitude, Polygon polygon) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        return polygon.contains(point);
    }
}
