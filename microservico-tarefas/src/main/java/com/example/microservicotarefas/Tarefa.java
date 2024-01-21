package com.example.microservicotarefas;





import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="TAREFA")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TAREFA_ID")
    private Integer tarefaId;

    @Column(name="UTILIZADOR_ID", nullable = false)
    private Integer utilizadorId;

    @Column(name="TAREFA_ESTADO", nullable = false)
    private String estado;

    @ManyToOne // Relação muitos para um com a tabela Imagem
    @JoinColumn(name = "IMAGEM_ID")
    private Imagem imagem;

    @Column(name="DATA_INICIO")
    private Timestamp dataInicio;

    @Column(name="DATA_FIM")
    private Timestamp dataFim;

    @Column(name="DURACAO_EM_SEGUNDOS")
    private long duracao;


    @Column(name="OBJETOS_IDENTIFICADOS")
    private ArrayList<String> objetosIdentificados;

    public static long getDateDiff(Timestamp oldTs, Timestamp newTs, TimeUnit timeUnit) {
        long diffInMS = newTs.getTime() - oldTs.getTime();
        return timeUnit.convert(diffInMS, TimeUnit.MILLISECONDS);
    }

    public Tarefa(Integer utilizadorId, Imagem imagem ,ArrayList<String> objetosIdentificados) {
        this.utilizadorId = utilizadorId;
        this.estado = "PROCESSAMENTO";
        this.imagem = imagem;
        this.dataInicio = Timestamp.valueOf(LocalDateTime.now());
        this.objetosIdentificados = objetosIdentificados;
    }

    public Tarefa(Integer utilizadorId,  Imagem imagem ) {
        this.utilizadorId = utilizadorId;
        this.estado = "PROCESSAMENTO";
        this.imagem = imagem;
        this.dataInicio = Timestamp.valueOf(LocalDateTime.now());
    }
}
