package com.freddys_bbq_frontend_intern;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@RequestMapping("/intern/map")
public class MapController {

    @Value("${MAP_SERVICE_URL:http://localhost:8090}")
    private String mapServiceUrl;

    private final RestTemplate restTemplate;

    public MapController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String getMap() {
        return "map";
    }

    @GetMapping("/route")
    public ResponseEntity<Map<String, Object>> routeStart(@RequestParam float startLon,
                             @RequestParam float startLat,
                             @RequestParam float endLon,
                             @RequestParam float endLat) {

        System.out.println("call");
        String requestString = String.format(
                "%s/ors/v2/directions/driving-car?start=%s,%s&end=%s,%s",
                this.mapServiceUrl, startLon, startLat, endLon, endLat
        );

        ResponseEntity<String> response = restTemplate.getForEntity(requestString, String.class);

        try {
            // JSON-String in eine Map umwandeln
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonResponse = objectMapper.readValue(response.getBody(), Map.class);

            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
