package com.prog4.libraryManagement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
public class LibraryManagementApplication {

	@Value("${DB_URL}")
	private String jdbcUrl;

		@Value("${DB_USER}")
		private String user;

		@Value("${DB_PASSWORD}")
		private String password;

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(JdbcTemplate jdbcTemplate) {
		return (args) -> {
			try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password)) {
				System.out.println("Connexion avec succès.");
			} catch (SQLException e) {
				System.err.println("Connexion echouée");
				e.printStackTrace();
			}
		};
	}
}
