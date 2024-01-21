package com.example.microservicofrontend;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@FeignClient(name="microservico-tarefas")
public interface ProxyMicroservicoTarefas {

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFileToBackTier(@RequestPart("imagem") MultipartFile imagem);

    @PostMapping("/tarefa")
    @Headers("Content-Type: application/json")
    ResponseEntity<Integer> inserirTarefa(@RequestBody TarefaDTO tarefaDTO);

    @PostMapping("/imagem/{imagemId}/detectar-objetos")
    ResponseEntity<ArrayList<String>> detectarObjetos(@PathVariable Integer imagemId);

    @PutMapping("/tarefa/atualizar")
    void upgradeTarefaById(@RequestParam Integer id, @RequestParam Timestamp dataFim, @RequestParam long duracao, @RequestParam ArrayList<String> objetos);

    @GetMapping("/tafefas/user")
    ResponseEntity<ArrayList<TarefaDTO>> listartarefasByIdUser(@RequestParam("idUser") Integer userId);

    @GetMapping("/imagem/")
    ResponseEntity<ImagemDTO> getImagemByHash(@RequestParam String hash);

    @GetMapping("/tarefas")
    List<TarefaDTO> listarTarefas();

    @PutMapping("/tarefa/cancelar")
    void cancelarTarefa(@RequestParam Integer tarefaId);

    @PutMapping("/tarefa/banir")
    void banirTarefa(@RequestParam Integer tarefaId);

    @PutMapping("/tarefa/permitir")
    void permitirTarefa(@RequestParam Integer tarefaId);
    


}
