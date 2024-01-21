package com.example.microservicotarefas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@RestController
public class ControladorUpload {


    @Autowired
    RepositorioImagem repositorioImagem;

    private final Path root = Paths.get("src/main/resources/static/upload");

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadResponse uploadFile(@RequestPart("imagem") MultipartFile file) throws IOException, NoSuchAlgorithmException {
        UploadResponse response = new UploadResponse();

        if (file.isEmpty()) {
            response.setSucesso(false);
            response.setNome("");
            response.setTamanho(0);
        } else {

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String hash = calculateImageHash(file.getInputStream());



            // Lógica para lidar com a imagem
            byte[] bytes = file.getBytes();

            // Converta os bytes para Base64
            String base64 = java.util.Base64.getEncoder().encodeToString(bytes);

            Imagem existingImage = repositorioImagem.findByImagemHash(hash);

            if (existingImage==null) {
                //Adiciona Imagem a tabela Imagem
                Imagem imagem = new Imagem(fileName, base64, hash );

                repositorioImagem.save(imagem);
                Path path = Paths.get(root + "/" + fileName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);


                response.setSucesso(true);
                response.setNome(fileName);
                response.setTamanho(file.getSize());
                response.setHash(hash);
                response.setBytes(base64);
            } else {
                response.setSucesso(true);
                response.setNome(fileName);
                response.setTamanho(file.getSize());
                response.setHash(hash);
                response.setBytes(base64);
            }
        }
        return response;
    }

    private String calculateImageHash(InputStream inputStream) throws IOException, NoSuchAlgorithmException {
        // Implemente a lógica para calcular o hash a partir do InputStream
        // Exemplo usando MessageDigest e DigestInputStream:
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        try (DigestInputStream dis = new DigestInputStream(inputStream, md)) {
            while (dis.read() != -1) {
                // Não é necessário fazer nada aqui, apenas ler o stream
            }
        }

        // Converte o hash para uma representação hexadecimal
        byte[] hashBytes = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            hexString.append(String.format("%02X", hashByte));
        }

        return hexString.toString();
    }




    // Método para criar uma resposta de sucesso
    private UploadResponse createSuccessResponse(String fileName) {
        UploadResponse response = new UploadResponse();
        response.setSucesso(true);
        response.setNome(fileName);
        response.setTamanho(1024); // Defina o tamanho conforme necessário
        return response;
    }

    // Método para criar uma resposta de erro
    private UploadResponse createErrorResponse(String message) {
        UploadResponse response = new UploadResponse();
        response.setSucesso(false);
        response.setNome("");
        response.setTamanho(0);
        return response;
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }






}
