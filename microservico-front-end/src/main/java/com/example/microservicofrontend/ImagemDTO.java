package com.example.microservicofrontend;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImagemDTO {

    private Integer id;

    private String nome;


    private String imagemHash;


    private String base64;

    public ImagemDTO(String base64) {
    }
}
