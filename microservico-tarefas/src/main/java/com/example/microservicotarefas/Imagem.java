package com.example.microservicotarefas;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Table(name="IMAGEM")
@Entity
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "IMAGEM_HASH", length = 999999999)
    private String imagemHash;

    @Column(name = "BASE64", length = 999999999)
    private String base64;

    public Imagem(String nome, String base64, String imagemHash) {
        this.nome = nome;
        this.base64 = base64;
        this.imagemHash = imagemHash;
    }

}
