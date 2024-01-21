package com.example.microservicofrontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class MicroservicoFrontEndApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicoFrontEndApplication.class, args);
	}

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	ProxyMicroservicoUtilizador proxyMicroservicoUtilizador;


	@Override
	public void run(ApplicationArguments args) throws Exception {

	}
}
