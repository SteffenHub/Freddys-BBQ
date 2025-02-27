package com.freddys_bbq_frontend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class AwakeService {

    @Value("${DELIVERY_BACKEND_URL:http://localhost:8081}")
    private String deliveryBackendUrl;

    @Value("${MAP_SERVICE_URL:http://localhost:8090}")
    private String mapServiceUrl;

    @Value("${CUSTOMER_FRONTEND_URL:http://localhost:4200}")
    private String customerFrontendUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 840000) // 14 × 60 × 1000 = 840000 = 14min
    public void callOtherMicroservice() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(this.deliveryBackendUrl+"/delivery", String.class);
            System.out.println("AWAKE Delivery Backend: True");//: " + response.getBody());
        } catch (Exception e) {
            System.err.println("AWAKE: Error calling delivery backend: " + e.getMessage());
        }
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(this.mapServiceUrl+"/ors/v2/directions/driving-car?start=9.993682,53.551086&end=9.978556,53.542708", String.class);
            System.out.println("AWAKE Map Service: True");//: " + response.getBody());
        } catch (Exception e) {
            System.err.println("AWAKE: Error calling map service: " + e.getMessage());
        }
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(this.customerFrontendUrl, String.class);
            System.out.println("AWAKE Customer Frontend: True");//: " + response.getBody());
        } catch (Exception e) {
            System.err.println("AWAKE: Error calling map service: " + e.getMessage());
        }
    }
}