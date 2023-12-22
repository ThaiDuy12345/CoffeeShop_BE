package com.duan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainCoffeeShopApplication {
    
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MainCoffeeShopApplication.class);
		app.setRegisterShutdownHook(false);  // Ngưng Spring Boot tự tắt khi không kết nối được SQL
		app.run(args);
	}

}
