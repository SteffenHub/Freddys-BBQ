package com.freddys_bbq_delivery;

import com.freddys_bbq_delivery.model.Delivery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DeliveryRepository {

    private final List<Delivery> deliveries;

    public DeliveryRepository() {
        this.deliveries = new ArrayList<>();
    }

    public List<Delivery> getDeliveries() {
        return this.deliveries;
    }

    public void addDelivery(Delivery delivery) {
        this.deliveries.add(delivery);
    }

    public void removeDelivery(Delivery delivery) {
        this.deliveries.remove(delivery);
    }

    public Optional<Delivery> getDeliveryByOrderId(UUID id) {
        return deliveries.stream()
                .filter(delivery -> delivery.getOrder().getId().equals(id))
                .findFirst();
    }
}