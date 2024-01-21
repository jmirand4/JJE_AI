package com.example.microservicotarefas;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Service
public class ServicoDetecaoObjetos {

    @Value("${object-detection.api.url}")
    private String apiUrl;

    public DetecaoObjetosResponse detectObjects(File imageFile) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new FileSystemResource(imageFile));
        body.add("min_confidence", 0.4);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<DetecaoObjetosResponse> response = restTemplate.postForEntity(apiUrl, requestEntity, DetecaoObjetosResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            // Trate erros
            System.err.println("Erro na chamada à API de detecção de objetos.");
            return null;
        }
    }



}
