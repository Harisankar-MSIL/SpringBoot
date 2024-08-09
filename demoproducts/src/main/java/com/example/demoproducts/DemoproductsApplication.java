package com.example.demoproducts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemoproductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoproductsApplication.class, args);
	}

	@GetMapping("/haii")
	public String hello(){
		return "hello";
	}

}
