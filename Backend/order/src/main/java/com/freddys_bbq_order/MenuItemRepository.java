package com.freddys_bbq_order;

import java.util.List;
import java.util.UUID;

import com.freddys_bbq_order.model.MenuItemO;
import org.springframework.data.repository.CrudRepository;

public interface MenuItemRepository extends CrudRepository<MenuItemO, UUID> {

    List<MenuItemO> findByCategory(String category);

}
