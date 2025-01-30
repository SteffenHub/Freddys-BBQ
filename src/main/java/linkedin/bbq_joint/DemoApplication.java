package linkedin.bbq_joint;

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
			if (menuItemRepository.count() == 0) { // Prevent duplicate entries
				menuItemRepository.save(new MenuItem(UUID.fromString("00000000-0000-0000-0000-000000000001"),
						"Freddy's Ribs Special", 20, false));
				menuItemRepository.save(new MenuItem(UUID.fromString("00000000-0000-0000-0000-000000000002"),
						"Freddy's Lemonade", 5, true));
				menuItemRepository.save(new MenuItem(UUID.fromString("00000000-0000-0000-0000-000000000003"),
						"Coleslaw Salad", 3, false));
			}
		};
	}

}
