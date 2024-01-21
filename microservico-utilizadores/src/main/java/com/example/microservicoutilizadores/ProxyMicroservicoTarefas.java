package com.example.microservicoutilizadores;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="microservico-tarefas")
public interface ProxyMicroservicoTarefas {


}
