package com.jalian.online_store_order_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
@EnableRetry
public class OnlineStoreOrderManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineStoreOrderManagementApplication.class, args);
	}
}
