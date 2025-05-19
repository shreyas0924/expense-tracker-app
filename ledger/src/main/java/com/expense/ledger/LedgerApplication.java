package com.expense.ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.expense.ledger.repository")
@EntityScan(basePackages = "com.expense.ledger.entities")
@ComponentScan(basePackages = { "com.expense.ledger.controller", "com.expense.ledger.service",
		"com.expense.ledger.consumer" })
public class LedgerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LedgerApplication.class, args);
	}

}
