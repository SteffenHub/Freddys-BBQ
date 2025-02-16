package com.freddys_bbq_frontend;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface MenuItemRepository extends CrudRepository<MenuItem, UUID> {

  Iterable<MenuItem> findByOrderByDrinkDescNameDesc();

  Iterable<MenuItem> findByDrinkOrderByNameDesc(boolean drink);

}