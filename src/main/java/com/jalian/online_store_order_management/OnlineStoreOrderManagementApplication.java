package com.jalian.online_store_order_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OnlineStoreOrderManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineStoreOrderManagementApplication.class, args);
	}
}
