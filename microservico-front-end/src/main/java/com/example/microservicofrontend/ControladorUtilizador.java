package com.example.microservicofrontend;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/menu")
public class ControladorUtilizador {

    @Autowired
    ProxyMicroservicoUtilizador proxyMicroservicoUtilizador;

    @Autowired
    ProxyMicroservicoTarefas proxyMicroservicoTarefas;


    @GetMapping("/perfil")
    public String perfil(Model model, Principal principal,  HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("principal",principal);

        ResponseEntity<UtilizadorDTO> response = proxyMicroservicoUtilizador.getUtilizadorByName(principal.getName());
        UtilizadorDTO utilizador = response.getBody();

        RoleDTO role = utilizador.getRole();
        String roleName = role.getName();
        String username=utilizador.getUsername();


        System.out.println("Utilizador: " + utilizador);
        System.out.println("Username: " + username);

        model.addAttribute("nome",username);
        model.addAttribute("role",roleName);

        // No método que lida com a exibição da página /perfil
        String mensagemErro = request.getParameter("erro");
        if (mensagemErro != null)
            model.addAttribute("erro", "Nome Inválido");

        return "perfil";
    }

    @GetMapping("/upload")
    public String getUploadPage() {

        return "upload";
    }


    @GetMapping("/tarefas")
    public String listarTarefas(Model model, Principal principal) {
        try {

            String username = principal.getName();
            Integer idUser = proxyMicroservicoUtilizador.getIdByName(username).getBody();

            // Obtenha a lista original de tarefas
            ArrayList<TarefaDTO> tarefasOriginal = proxyMicroservicoTarefas.listartarefasByIdUser(idUser).getBody();

            // Caso não haja tarefas
            if (tarefasOriginal.isEmpty()) {
                model.addAttribute("mensagem", "O utilizador não tem tarefas criadas.");
                return "tarefas";
            }


            // Crie uma nova lista mantendo os campos desejados
            ArrayList<TarefaDTO> tarefasComCamposDesejados = new ArrayList<>();
            ArrayList<String> imagensBase64 = new ArrayList<>();


            for (TarefaDTO tarefaOriginal : tarefasOriginal) {
                // Buscar imagem através do id da tarefa
                String base64 = tarefaOriginal.getImagem().getBase64();



                // Crie uma cópia da tarefa mantendo os campos desejados
                TarefaDTO tarefaComCamposDesejados = new TarefaDTO(
                        tarefaOriginal.getTarefaId(),
                        tarefaOriginal.getUtilizadorId(),
                        tarefaOriginal.getEstado(),
                        tarefaOriginal.getImagem(),
                        tarefaOriginal.getDataInicio(),
                        tarefaOriginal.getDataFim(),
                        tarefaOriginal.getDuracao(),
                        tarefaOriginal.getObjetosIdentificados()
                );



                // Adicione a tarefa à nova lista
                tarefasComCamposDesejados.add(tarefaComCamposDesejados);

                // Atribua o valor de base64
                imagensBase64.add(base64);

            }

            // Adicione a nova lista e a string base64 ao modelo
            model.addAttribute("tarefas", tarefasComCamposDesejados);
            model.addAttribute("imagens", imagensBase64);
            System.out.println(imagensBase64);
            return "tarefas";
        } catch (Exception e) {
            // Tratar outras exceções, se necessário
            // ...

            // Se for um erro diferente, redirecione para uma página de erro ou retorne uma mensagem apropriada
            model.addAttribute("mensagem", "O utilizador não tem tarefas criadas.");
            return "tarefas";
        }
    }

    @GetMapping("/admin")
    public String adminMenuPage(){
        return "menuAdmin";
    }



    @PostMapping("/perfil/editar")
    public String processarFormularioEdicao(@RequestParam("nome") String novoNome,
                                            @RequestParam("password") String novaPassword,
                                            Principal principal, Model model) {
        try {

            ResponseEntity<UtilizadorDTO> verifyIfUserExist = proxyMicroservicoUtilizador.getUtilizadorByName(novoNome);

            if (verifyIfUserExist.hasBody()){
                model.addAttribute("errorEdit","Nome inválido.");
                return "redirect:/menu/perfil?erro";
            }

            Integer userId = getUserId(principal);
            ResponseEntity<UtilizadorDTO> responseEntity = proxyMicroservicoUtilizador.editarUser(userId, novoNome, novaPassword);




            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("confirmUpdate", "Faça o login novamente para confirmar!");
                return "logout";
            } else {
                throw new IllegalStateException("Erro ao editar o utilizador. Status code: " + responseEntity.getStatusCodeValue());
            }
        } catch (Exception e) {
            model.addAttribute("errorEdit","Ocorreu um erro ao editar os seus dados");
            return "redirect:/menu/perfil";
        }
    }

    private Integer getUserId(Principal principal) {
        try {
            String username = principal.getName();
            ResponseEntity<UtilizadorDTO> responseEntity = proxyMicroservicoUtilizador.getUtilizadorByName(username);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                UtilizadorDTO userDetails = responseEntity.getBody();
                return userDetails.getUserId();  // Assuming UtilizadorDTO has a method getId() for the user ID
            } else {
                throw new IllegalStateException("Unable to retrieve user ID from Principal. Status code: " + responseEntity.getStatusCodeValue());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error retrieving user ID from Principal", e);
        }
    }

}
