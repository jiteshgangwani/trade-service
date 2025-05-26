package com.tradedemo.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;

@SpringBootApplication
public class TradeServiceApplication {

	@Value(value = "classpath:companies.xml")
	private Resource product;

	public static void main(String[] args) {
		SpringApplication.run(TradeServiceApplication.class, args);
	}

}
