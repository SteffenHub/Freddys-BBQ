package com.freddys_bbq;

import java.util.UUID;

import com.freddys_bbq.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, UUID> {

}