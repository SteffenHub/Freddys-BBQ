package com;

import com.freddys_bbq_order.MenuItemRepository;
import com.freddys_bbq_order.model.MenuItem;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;


@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(MenuItemRepository menuItemRepository) {
		return args -> {
			if (menuItemRepository.count() != 0) {
				System.out.println("Found " + menuItemRepository.count() + " menu items in the database");
			}
			if (menuItemRepository.count() == 0) { // Prevent duplicate entries
				System.out.println("The Database is empty -> fill with menu items");
				// Main Courses
				menuItemRepository.save(new MenuItem(
						"Main Course",
						"Freddy's Rib Special",
						21.9,
						"https://unsplash.com/photos/0hOHNA3M6Ds/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8OHx8cmlic3xlbnwwfHx8fDE3MTQyOTU0MjN8MA&force=true&w=640"
				));
				menuItemRepository.save(new MenuItem(
						"Main Course",
						"BBQ Burger and Fries",
						16.5,
						"https://unsplash.com/photos/uVPV_nV17Tw/download?ixid=M3wxMjA3fDB8MXxhbGx8fHx8fHx8fHwxNzE0MzMzMzA4fA&force=true&w=640"
				));
				menuItemRepository.save(new MenuItem(
						"Main Course",
						"Mac and Cheese",
						10.50,
						"https://unsplash.com/photos/4qzaeR_sTYA/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8Mnx8bWFjJTIwYW5kJTIwY2hlZXNlfGVufDB8fHx8MTcxNDMzMzE4MHwy&force=true&w=640"
				));
				// Sides
				menuItemRepository.save(new MenuItem(
						"Side",
						"Coleslaw Salad",
						4.8,
						"https://unsplash.com/photos/btS7sL3jprM/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8N3x8Y29sZXNsYXclMjBzYWxhZHxlbnwwfHx8fDE3MTQzMzMzNDF8Mg&force=true&w=640"
				));
				menuItemRepository.save(new MenuItem(
						"Side",
						"Sweet Potatoe Mash",
						6.8,
						"https://unsplash.com/photos/zEBe2beserI/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8Mnx8bWFzaGVkJTIwcG90YXRvZXN8ZW58MHx8fHwxNzE0MzMzNDEyfDI&force=true&w=640"
				));
				// Drinks
				menuItemRepository.save(new MenuItem(
						"Drink",
						"Lemonade",
						3.5,
						"https://unsplash.com/photos/sSLqRCTJBvU/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8M3x8bGVtb25hZGV8ZW58MHx8fHwxNzE0MzMzNDYyfDI&force=true&w=640"
				));
				menuItemRepository.save(new MenuItem(
						"Drink",
						"Beer",
						4.4,
						"https://unsplash.com/photos/NfjfNQV47OU/download?ixid=M3wxMjA3fDB8MXxzZWFyY2h8MTd8fGJlZXJ8ZW58MHx8fHwxNzE0MjUxMjgwfDI&force=true&w=640"
				));
			}
		};
	}

}
