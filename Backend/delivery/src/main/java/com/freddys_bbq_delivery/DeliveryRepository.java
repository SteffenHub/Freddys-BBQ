package com.freddys_bbq_delivery;

import com.freddys_bbq_delivery.model.DeliveryD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryD, UUID> {
    Optional<DeliveryD> findByOrderId(UUID orderId);
}