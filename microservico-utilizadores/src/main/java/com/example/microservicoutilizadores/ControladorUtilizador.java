package com.example.microservicoutilizadores;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class ControladorUtilizador {

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

    @Autowired
    RepositorioUtilizador repositorioUtilizador;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public ResponseEntity<?> listarUsers() {
        try {
            List<Utilizador> listaUsers = repositorioUtilizador.findAll();
            if (listaUsers.isEmpty()) {
                return new ResponseEntity<>("Nenhum usuário encontrado", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(listaUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao obter usuários", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value="/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> criarUser (@RequestBody Utilizador utilizador){
        try {
            repositorioUtilizador.save(utilizador);
            return ResponseEntity.ok(utilizador);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @DeleteMapping("/user/{id}")
    public void apagarUser (@PathVariable @Valid Integer id){
        try {
            repositorioUtilizador.deleteByUserId(id);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUtilizador (@Valid @PathVariable Integer id){
        try {
            Optional<Utilizador> utilizador = repositorioUtilizador.findById(id);
            return ResponseEntity.ok(utilizador);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/user/admin")
    public ResponseEntity<?> getUtilizadorByName(@RequestParam String nome) {
        try {
            Optional<Utilizador> utilizador = repositorioUtilizador.findByUsername(nome);
            return ResponseEntity.ok(utilizador);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar utilizador");
        }
    }

    @GetMapping("/user/admin/id")
    public ResponseEntity<?> getIdByName(@RequestParam String nome) {
        try {
            Integer utilizadorId = repositorioUtilizador.findIdByUsername(nome);
            return ResponseEntity.ok(utilizadorId);
        } catch (Exception e) {
            // Melhore a resposta de erro incluindo detalhes da exceção
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar id: " + e.getMessage());
        }
    }

    @PutMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editarUser(@PathVariable Integer id, @RequestParam String name, @RequestParam String password) {
        try {
            Optional<Utilizador> userOptional = repositorioUtilizador.findById(id);

            if (userOptional.isPresent()) {
                Utilizador existingUser = userOptional.get();

                // Verifica se a role é "PREMIUM"
                if (!isPremiumUser(existingUser)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("Apenas usuários com a role 'PREMIUM' podem ser editados.");
                }

                // Continua com as atualizações caso seja um usuário "PREMIUM"
                // Atualiza os campos do usuário (nome de usuário e senha)
                existingUser.setUsername(name);

                // Codifica e define a nova senha
                if (password != null && !password.isEmpty()) {
                    String encodedPassword = passwordEncoder.encode(password);
                    existingUser.setPassword(encodedPassword);
                }

                // Atualiza os campos diretamente no banco de dados
                repositorioUtilizador.updateUserInfo(id, existingUser.getUsername(), existingUser.getPassword());

                return ResponseEntity.ok(existingUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao editar o usuário", e);
        }
    }

    @PostMapping("/user/banir")
    public ResponseEntity<?> banirUtilizador(@RequestParam Integer userId) {
        try {
            Optional<Utilizador> userOptional = repositorioUtilizador.findById(userId);

            if (userOptional.isPresent()) {
                Utilizador user = userOptional.get();
                user.setEstaBloqueado(true);

                // Atualiza manualmente o usuário no banco de dados sem usar save
                repositorioUtilizador.updateUserStatus(userId, true);

                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao banir o usuário", e);
        }
    }

    // Método para verificar se o usuário tem a role "PREMIUM"
    private boolean isPremiumUser(Utilizador user) {
        // Certifique-se de que a comparação é case-insensitive, se necessário
        return "PREMIUM".equalsIgnoreCase(user.getRole().getName());
    }

    @PostMapping("/user/desbanir")
    public ResponseEntity<?> desbanirUtilizador(@RequestParam Integer userId) {
        try {
            Optional<Utilizador> userOptional = repositorioUtilizador.findById(userId);

            if (userOptional.isPresent()) {
                Utilizador user = userOptional.get();
                user.setEstaBloqueado(false);

                // Atualiza manualmente o usuário no banco de dados sem usar save
                repositorioUtilizador.updateUserStatus(userId, false);

                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao desbanir o usuário", e);
        }
    }






}
