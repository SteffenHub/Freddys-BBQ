package com.freddys_bbq_order;

import java.util.UUID;

import com.freddys_bbq_order.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, UUID> {

}