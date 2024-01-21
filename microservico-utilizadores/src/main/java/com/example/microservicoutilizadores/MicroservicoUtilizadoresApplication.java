package com.example.microservicoutilizadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroservicoUtilizadoresApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicoUtilizadoresApplication.class, args);
	}

	@Autowired
	RepositorioUtilizador repositorioUtilizador;

	@Autowired
	RepositorioRole repositorioRole;


	@Autowired
	private BCryptPasswordEncoder encoder;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//Criar Roles
		Role admin = new Role("ADMIN");
		Role normal = new Role("NORMAL");
		Role premium = new Role("PREMIUM");

		// Crie usuários
		Utilizador user1 = new Utilizador("admin", encoder.encode("admin"),admin);
		Utilizador user2 = new Utilizador("User2", encoder.encode("password2"),premium);
		Utilizador user3 = new Utilizador("User3", encoder.encode("password3"),normal);
		Utilizador user4 = new Utilizador("User4", encoder.encode("password4"),normal);
		Utilizador user5 = new Utilizador("User5",  encoder.encode("password5"),normal);
		Utilizador user6 = new Utilizador("User6",  encoder.encode("password6"),normal);

		// Salve os usuários no banco de dados
		repositorioRole.saveAll(Arrays.asList(admin,normal,premium));
		repositorioUtilizador.saveAll(Arrays.asList(user1, user2, user3, user4, user5, user6));


	}
}
