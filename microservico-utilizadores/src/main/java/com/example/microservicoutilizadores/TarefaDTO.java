package com.example.microservicoutilizadores;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TarefaDTO {


    private Integer utilizadorId;

    private String estado;

    private String nomeImagem;

    private Timestamp dataInicio;

    private Timestamp dataFim;

    private long duracao;

    private String imagemHash;

    private ArrayList<String> objetosIdentificados;

    public static long getDateDiff(Timestamp oldTs, Timestamp newTs, TimeUnit timeUnit) {
        long diffInMS = newTs.getTime() - oldTs.getTime();
        return timeUnit.convert(diffInMS, TimeUnit.MILLISECONDS);
    }

    public TarefaDTO(Integer utilizadorId,  String url,  String nomeImagem, ArrayList<String> objetosIdentificados) {
        this.utilizadorId = utilizadorId;
        this.estado = "PROCESSAMENTO";
        this.nomeImagem = nomeImagem;
        this.dataInicio = Timestamp.valueOf(LocalDateTime.now());
        this.objetosIdentificados = objetosIdentificados;
    }
}
