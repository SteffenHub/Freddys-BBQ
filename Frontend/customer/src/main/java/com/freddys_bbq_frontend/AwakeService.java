package com.freddys_bbq_frontend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class AwakeService {

    @Value("${ORDER_BACKEND_URL:http://localhost:8080}")
    private String backendUrl;

    @Value("${INTERN_FRONTEND_URL:http://localhost:4300}")
    private String internFrontendUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 840000) // 14 × 60 × 1000 = 840000 = 14min
    public void callOtherMicroservice() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(this.backendUrl+"/menu", String.class);
            System.out.println("AWAKE Order Backend: True");//: " + response.getBody());
        } catch (Exception e) {
            System.err.println("AWAKE: Error calling order backend: " + e.getMessage());
        }

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(this.internFrontendUrl+"/delivery", String.class);
            System.out.println("AWAKE Intern Frontend: True");//: " + response.getBody());
        } catch (Exception e) {
            System.err.println("AWAKE: Error calling Intern Frontend: " + e.getMessage());
        }
    }
}