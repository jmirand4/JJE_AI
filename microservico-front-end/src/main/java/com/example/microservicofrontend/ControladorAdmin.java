package com.example.microservicofrontend;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/menu/admin")
@Controller
public class ControladorAdmin {

    @Autowired
    ProxyMicroservicoTarefas proxyMicroservicoTarefas;

    @Autowired
    ProxyMicroservicoUtilizador proxyMicroservicoUtilizador;

    @GetMapping("/utilizadores")
    public String listarUtilizadores(Model model) {
        try {
            // Consulta os veículos usando o microserviço1
            List<UtilizadorDTO> utilizadores = proxyMicroservicoUtilizador.listarUtilizadores();

            // Adiciona os resultados ao modelo para exibição na página
            model.addAttribute("utilizadores", utilizadores);

            return "utilizadores"; // Nome da sua página HTML para listar utilizadores
        } catch (FeignException feignException) {
            model.addAttribute("erro", "Erro ao consultar utilizadores.");
            return "admin";
        }
    }

    @GetMapping("/tarefas")
    public String listarTarefas(Model model) {
        try {
            // Consulta os veículos usando o microserviço1
            List<TarefaDTO> tarefas = proxyMicroservicoTarefas.listarTarefas();

            // Mapeia os IDs de usuário para os nomes de usuário correspondentes
            Map<Integer, String> nomesUsers = obterNomesUsers(tarefas);

            // Adiciona os resultados ao modelo para exibição na página
            model.addAttribute("tarefas", tarefas);
            model.addAttribute("users",nomesUsers);

            if (tarefas.isEmpty()) {
                // Adiciona um atributo indicando que não há tarefas
                model.addAttribute("semTarefas", true);
            }
            return "tarefasAdmin";

        } catch (FeignException feignException) {
            model.addAttribute("semTarefas", true);
            return "tarefasAdmin";

        }
    }

    private Map<Integer, String> obterNomesUsers(List<TarefaDTO> tarefas) {
        // Obtém os IDs únicos dos usuários
        Set<Integer> idsUsuarios = tarefas.stream()
                .map(TarefaDTO::getUtilizadorId)
                .collect(Collectors.toSet());

        // Consulta os nomes dos usuários usando o microserviço adequado
        Map<Integer, String> nomesUsuarios = new HashMap<>();
        for (Integer idUsuario : idsUsuarios) {
            UtilizadorDTO user = proxyMicroservicoUtilizador.getUtilizador(idUsuario).getBody();
            nomesUsuarios.put(idUsuario, user.getUsername());
        }

        return nomesUsuarios;
    }

    @PostMapping("/utilizadores/editar")
    public String processarFormularioEdicaoUsuario(@RequestParam("userId") Integer userId,
                                                   @RequestParam("nome") String novoNome,
                                                   @RequestParam("senha") String novaSenha) {
        try {


            ResponseEntity<UtilizadorDTO> responseEntity = proxyMicroservicoUtilizador.editarUser(userId, novoNome, novaSenha);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {

                return "redirect:/menu/admin";
            } else {
                throw new IllegalStateException("Erro ao editar o usuário. Status code: " + responseEntity.getStatusCodeValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/menu/admin/utilizadores";
        }
    }

    @PostMapping("/utilizadores/banir")
    public String banirUtilizador(@RequestParam("userId") Integer userId) {
        try {



            ResponseEntity<UtilizadorDTO> responseEntity = proxyMicroservicoUtilizador.banirUser(userId);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {

                return "redirect:/menu/admin/utilizadores";
            } else {
                throw new IllegalStateException("Erro ao banir o usuário. Status code: " + responseEntity.getStatusCodeValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/menu/admin/utilizadores";
        }
    }

    @PostMapping("/utilizadores/desbanir")
    public String desbanirUtilizador(@RequestParam("userId") Integer userId) {
        try {
            ResponseEntity<UtilizadorDTO> responseEntity = proxyMicroservicoUtilizador.desbanirUser(userId);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {

                return "redirect:/menu/admin/utilizadores";
            } else {
                throw new IllegalStateException("Erro ao banir o usuário. Status code: " + responseEntity.getStatusCodeValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/menu/admin/utilizadores";
        }
    }

    @PostMapping("/tarefas/banir")
    public String banirTarefa(@RequestParam("tarefaId") Integer tarefaId) {
        try {
            proxyMicroservicoTarefas.banirTarefa(tarefaId);
        } catch (Exception e) {
            return "redirect:/menu/admin/tarefas";
        }
        return "redirect:/menu/admin/tarefas";
    }

    @PostMapping("/tarefas/permitir")
    public String permitirTarefa(@RequestParam("tarefaId") Integer tarefaId) {
        try {
            proxyMicroservicoTarefas.permitirTarefa(tarefaId);
        } catch (Exception e) {
            return "redirect:/menu/admin/tarefas";
        }
        return "redirect:/menu/admin/tarefas";
    }

}
