package com.example.microservicofrontend;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.OneToOne;
import lombok.*;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TarefaDTO {

    private Integer tarefaId;

    private Integer utilizadorId;

    private String estado;

    private ImagemDTO imagem;

    private Timestamp dataInicio;

    private Timestamp dataFim;

    private long duracao;

    private ArrayList<String> objetosIdentificados;

    public TarefaDTO(String nomeImagem, ArrayList<String> objetosIdentificados) {
    }

    public static long getDateDiff(Timestamp oldTs, Timestamp newTs, TimeUnit timeUnit) {
        long diffInMS = newTs.getTime() - oldTs.getTime();
        return timeUnit.convert(diffInMS, TimeUnit.MILLISECONDS);
    }


    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TarefaDTO(
            @JsonProperty("utilizadorId") Integer utilizadorId,
            @JsonProperty("imagem") ImagemDTO imagem) {
        this.utilizadorId = utilizadorId;
        this.estado = "PROCESSAMENTO";
        this.imagem = imagem;
        this.dataInicio = Timestamp.valueOf(LocalDateTime.now());
    }

}
