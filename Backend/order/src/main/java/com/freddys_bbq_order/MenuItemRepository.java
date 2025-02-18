package com.freddys_bbq_order;

import java.util.UUID;

import com.freddys_bbq_order.model.MenuItem;
import org.springframework.data.repository.CrudRepository;

public interface MenuItemRepository extends CrudRepository<MenuItem, UUID> {

  Iterable<MenuItem> findByOrderByDrinkDescNameDesc();

  Iterable<MenuItem> findByDrinkOrderByNameDesc(boolean drink);

}