package com.example.microservicofrontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ServicoUtilizadorCustom implements UserDetailsService {

    @Autowired
    private ProxyMicroservicoUtilizador proxyMicroservicoUtilizador;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseEntity<UtilizadorDTO> utilizadorDTO = proxyMicroservicoUtilizador.getUtilizadorByName(username);
        if (!utilizadorDTO.hasBody()) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        return utilizadorDTO.getBody();
    }


}
