package com.example.microservicotarefas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import com.example.microservicotarefas.DetecaoObjetosResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class ControladorTarefa {


    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    RepositorioTarefa repositorioTarefa;

    @Autowired
    RepositorioImagem repositorioImagem;

    @Autowired
    ServicoDetecaoObjetos servicoDetecaoObjetos;

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

    @GetMapping("/tarefas")
    public List<Tarefa> listarTarefas() {
        try {
            List<Tarefa> tarefas = repositorioTarefa.findAll();
            if (tarefas.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhuma tarefa encontrada");
            }
            return tarefas;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Erro ao obter tarefas", e);
        }
    }

    @GetMapping("/tarefa/{id}")
    public Optional<Tarefa> consultaPorId(@PathVariable("id") Integer id){
        try {
            Optional<Tarefa> tarefa = repositorioTarefa.findById(id);
            if (tarefa.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhuma tarefa encontrada");
            }
            return tarefa;
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/tarefaPorUtilizadorId/{utilizadorId}")
    public List<Tarefa> getTarefasPorUtilizadorId(@PathVariable("utilizadorId") Integer utilizadorId){
        try {
            List<Tarefa> tarefas =  repositorioTarefa.getTarefasPorUtilizadorId(utilizadorId);
            if (tarefas.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhuma tarefa encontrada");
            }
            return tarefas;
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/apagarTarefaById/{id}")
    public void apagarTarefaPorId(@PathVariable("id") Integer id){
        try {
            repositorioTarefa.deleteByTarefaId(id);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tarefa")
    public ResponseEntity<?> inserirTarefa(@RequestBody Tarefa tarefa){
        try {
            repositorioTarefa.save(tarefa);
            return ResponseEntity.ok(tarefa.getTarefaId());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
    }




    @RequestMapping(value = "/tarefa/atualizar", method = RequestMethod.PUT)
    void upgradeTarefaById(@RequestParam Integer id, @RequestParam Timestamp dataFim, @RequestParam long duracao, @RequestParam ArrayList<String> objetos) {
        try {
            repositorioTarefa.atualizarTarefa(id, dataFim, duracao, objetos);
            throw new ResponseStatusException(HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tafefas/user")
    public ResponseEntity<?> listartarefasByIdUser(@RequestParam Integer idUser){
        try {
            ArrayList<Tarefa> tarefas =repositorioTarefa.findByUserId(idUser);
            return ResponseEntity.ok(tarefas);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/tarefa/cancelar")
    void cancelarTarefa(@RequestParam Integer tarefaId) {
        repositorioTarefa.cancelarTarefa(tarefaId,"CANCELADA");
    }

    @PutMapping("/tarefa/banir")
    void banirTarefa(@RequestParam Integer tarefaId) {
        repositorioTarefa.banirTarefa(tarefaId,"BANIDA");
    }

    @PutMapping("/tarefa/permitir")
    void permitirTarefa(@RequestParam Integer tarefaId) {
        repositorioTarefa.permitirTarefa(tarefaId,"CONCLUIDA");
    }



}
