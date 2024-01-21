package com.example.microservicofrontend;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@FeignClient(name="microservico-utilizadores")
public interface ProxyMicroservicoUtilizador {

    @GetMapping("/user/admin")
    ResponseEntity<UtilizadorDTO> getUtilizadorByName(@RequestParam String nome);

    @GetMapping("/user/admin/id")
    ResponseEntity<Integer> getIdByName(@RequestParam String nome);

    @PostMapping("/user")
    ResponseEntity<UtilizadorDTO> criarUser (@RequestBody UtilizadorDTO utilizadorDTO);

    @GetMapping("/role/{nome}")
    ResponseEntity<RoleDTO> getRoleByName(@RequestParam String nome);

    @PutMapping("/user/{id}")
    ResponseEntity<UtilizadorDTO> editarUser(@PathVariable("id") Integer id,
                                             @RequestParam("name") String name,
                                             @RequestParam("password") String password);
    @GetMapping("/users")
    List<UtilizadorDTO> listarUtilizadores();

    @PostMapping("/user/banir")
    ResponseEntity<UtilizadorDTO> banirUser(@RequestParam("userId") Integer userId);

    @PostMapping("/user/desbanir")
    ResponseEntity<UtilizadorDTO> desbanirUser(@RequestParam("userId") Integer userId);

    @GetMapping("/user/{id}")
    ResponseEntity<UtilizadorDTO> getUtilizador( @PathVariable Integer id);
}
