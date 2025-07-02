package com.microservice.usuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceUsuarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceUsuarioApplication.class, args);
	}

}
