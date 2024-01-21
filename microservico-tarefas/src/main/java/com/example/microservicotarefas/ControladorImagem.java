package com.example.microservicotarefas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class ControladorImagem {

    @Autowired
    RepositorioTarefa repositorioTarefa;

    @Autowired
    RepositorioImagem repositorioImagem;

    @Autowired
    ServicoDetecaoObjetos servicoDetecaoObjetos;



    @GetMapping("/imagem/")
    ResponseEntity<?> getImagemByHash(@RequestParam String hash){
        try {
            Imagem imagem = repositorioImagem.findByImagemHash(hash);

            if (imagem != null) {
                // Retornar a imagem encontrada
                return ResponseEntity.ok(imagem);
            } else {
                // Se a imagem não for encontrada, lançar uma exceção 404 (NOT FOUND)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagem não encontrada");
            }
        } catch (Exception e) {
            // Se ocorrer uma exceção, lançar uma exceção 500 (INTERNAL SERVER ERROR)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar a imagem", e);
        }
    }


    @PostMapping("/imagem/{imagemId}/detectar-objetos")
    public ResponseEntity<?> detectarObjetos(@PathVariable Integer imagemId) {
        try {
            // Lógica para obter o arquivo de imagem associado à tarefa com ID tarefaId
            String nomeImagem = repositorioImagem.findImageById(imagemId);
            System.out.println(nomeImagem);


            // Chame o serviço de detecção de objetos
            File imageFile = getImageFileByName(nomeImagem);
            DetecaoObjetosResponse response = servicoDetecaoObjetos.detectObjects(imageFile);

            // Acesse os elementos necessários do objeto DetecaoObjetosResponse
            if (response != null && response.isSuccess()) {

                List<String> objetosDetetados = new ArrayList<>();

                List<DetecaoObjetosResponse.Prediction> predictions = response.getPredictions();
                for (DetecaoObjetosResponse.Prediction prediction : predictions) {
                    String label = prediction.getLabel();
                    double confidence = prediction.getConfidence();
                    System.out.println(label);
                    System.out.println(confidence);
                    objetosDetetados.add(label);

                }

                System.out.println(objetosDetetados);

                return ResponseEntity.ok(objetosDetetados);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.emptyList());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    public File getImageFileByName(String name) {
        // Diretório onde as imagens estão armazenadas
        String diretorioImagens = "src/main/resources/static/upload";

        // Construa o caminho completo usando o hash
        String caminhoCompleto = diretorioImagens + "/" + name; // ou o formato da sua imagem

        // Crie e retorne o arquivo
        return new File(caminhoCompleto);
    }
}
