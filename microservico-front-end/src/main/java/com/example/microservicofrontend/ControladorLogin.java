package com.example.microservicofrontend;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import java.security.Principal;

@Controller
public class ControladorLogin {

    @Autowired
    ProxyMicroservicoUtilizador proxyMicroservicoUtilizador;

    @Autowired
    private PasswordEncoder passwordEncoder;

/*
    @GetMapping(value={"/"})
    public ModelAndView loginForm(Model model, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("principal",principal);
        model.addAttribute("utilizador",proxyMicroservicoUtilizador.getUtilizadorByName(principal.getName()));

        return new ModelAndView("menu.html");
    }
*/

    @GetMapping(value={"/","/login"})
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/logout")
    public String realizarLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);
        return "logout";
    }


    @GetMapping("/menu")
    public String efetuarLogin(Model model, Principal principal){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("principal",principal);

        ResponseEntity<UtilizadorDTO> response = proxyMicroservicoUtilizador.getUtilizadorByName(principal.getName());
        UtilizadorDTO utilizador = response.getBody();

        String username=utilizador.getUsername();
        RoleDTO role=utilizador.getRole();
        String roleName = role.getName();

        System.out.println("Utilizador: " + utilizador);
        System.out.println("Username: " + username);

        model.addAttribute("role",roleName);
        model.addAttribute("utilizador",username);

        return "menuInicial";
    }

    @GetMapping("/register")
    public String registar (){
        return "registar";
    }



    @PostMapping("/register")
    public ModelAndView criarUtilizador(@RequestParam String username, @RequestParam String password, Model model) throws JsonProcessingException {
        ResponseEntity<UtilizadorDTO> response = proxyMicroservicoUtilizador.getUtilizadorByName(username);
        UtilizadorDTO responseBody = response.getBody();

        if (responseBody != null) {
            // Usuário já existe
            ModelAndView modelAndView = new ModelAndView("registar");
            model.addAttribute("registoJaExiste", true); // Adicione um valor (true) ao atributo
            return modelAndView;
        } else {
            ResponseEntity<RoleDTO> responseRole = proxyMicroservicoUtilizador.getRoleByName("NORMAL");

            if (responseRole.getBody() != null) {
                RoleDTO role = responseRole.getBody();

                // Use PasswordEncoder para codificar a senha
                String encodedPassword = passwordEncoder.encode(password);
                UtilizadorDTO novoUtilizador = new UtilizadorDTO(username, encodedPassword, role);

                System.out.println(novoUtilizador);

                // Use o Feign Client para fazer a solicitação POST
                proxyMicroservicoUtilizador.criarUser(novoUtilizador);

                ModelAndView modelAndView = new ModelAndView("redirect:/");
                model.addAttribute("registoSuccessfull", true); // Adicione um valor (true) ao atributo
                return modelAndView;
            }

            ModelAndView modelAndView = new ModelAndView("registar");
            model.addAttribute("registoError", true); // Adicione um valor (true) ao atributo
            return modelAndView;
        }
    }

    @GetMapping("/acesso-negado")
    public String acessoNegado(){
        return "acessoNegado";
    }


}
