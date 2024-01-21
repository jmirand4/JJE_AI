package com.example.microservicofrontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestControlador {

    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping(value = "/instancias-servico/{nomeservico}")
    public List<ServiceInstance> getAllInstancesServicoByNomeMicroservico(@PathVariable String nomeservico) {
        return this.discoveryClient.getInstances(nomeservico);
    }

    @GetMapping(value = "/servicos")
    public List<String> getAllServicos() {
        return this.discoveryClient.getServices();
    }

    @Value("${spring.application.name}")
    String nome_microservico;

    @GetMapping("/hello")
    public String hello(){
        return "Ola sou o " + nome_microservico;
    }
}
