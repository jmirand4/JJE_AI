package com.example.microservicoutilizadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class ControladorRole {

    @Autowired
    RepositorioRole repositorioRole;

    @GetMapping("/role")
    public List<Role> getRoles () {
        try {
            List<Role> listaRoles = repositorioRole.findAll();
            if (listaRoles.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhuma role encontrada");
            }
            return listaRoles;
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Erro ao obter roles", e);
        }
    }

    @GetMapping("/role/{nome}")
    public ResponseEntity<?> getRoleByName(@RequestParam String nome) {
        try {
            Optional<Role> role = repositorioRole.findByName(nome);
            return ResponseEntity.ok(role);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar role");
        }
    }
}
