package com.example.microservicofrontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
public class ControladorUpload {

    @Autowired
    ProxyMicroservicoTarefas proxyMicroservicoTarefas;

    @Autowired
    ProxyMicroservicoUtilizador proxyMicroservicoUtilizador;

    @PostMapping(value = "/menu/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestPart("imagem") MultipartFile file, RedirectAttributes attributes, Principal principal, Model model) throws IOException {
        if (file.isEmpty()) {
            attributes.addFlashAttribute("mensagem", "Por favor, selecione o arquivo para upload.");
            return "redirect:/menu/upload";
        }
        //comeca cronometrar tempo
        long start = System.currentTimeMillis();

        //GET Id do user
        ResponseEntity<Integer> responseEntity = proxyMicroservicoUtilizador.getIdByName(principal.getName());
        Integer userID = responseEntity.getBody();


        try {
        String mensagemBack = proxyMicroservicoTarefas.uploadFileToBackTier(file);



            ObjectMapper objectMapper = new ObjectMapper();
            UploadResponseDTO uploadResponse = objectMapper.readValue(mensagemBack, UploadResponseDTO.class);

            // Agora você pode acessar os dados da resposta
            boolean sucesso = uploadResponse.isSucesso();
            String hash = uploadResponse.getHash();

           if(sucesso){
               //Get id da imagem
               ImagemDTO imagemDTO = proxyMicroservicoTarefas.getImagemByHash(hash).getBody();
               assert imagemDTO != null;
               Integer idImagem = imagemDTO.getId();


               //Criar tarefa
               TarefaDTO tarefaDTO = new TarefaDTO(userID,imagemDTO);
               Integer tarefaId= proxyMicroservicoTarefas.inserirTarefa(tarefaDTO).getBody();

               try {
                   ArrayList<String> obgDetetados = proxyMicroservicoTarefas.detectarObjetos(idImagem).getBody();
                   System.out.println(obgDetetados);

                   long stop = System.currentTimeMillis();
                   Timestamp fim = Timestamp.valueOf(LocalDateTime.now());
                   long duracao = stop - start;

                   try {
                       // Esta linha está agora em um bloco try separado
                       proxyMicroservicoTarefas.upgradeTarefaById(tarefaId, fim, duracao, obgDetetados);
                   } catch (Exception ignored) {
                       // Ignorar
                   }
               } catch(Exception e){
                   // Caso de falhar
                   proxyMicroservicoTarefas.cancelarTarefa(tarefaId);
                   model.addAttribute("erroUpload","Ocorreu um erro ao detetar objetos!");
                   return  "upload";
               }
           }


        } catch (FeignException.FeignClientException ignored) {
            model.addAttribute("erroImagem","A imagem é muito pesada. Tente novamente com outra imagem!");
            return  "upload";
        }

        return "redirect:/menu";
    }

}
